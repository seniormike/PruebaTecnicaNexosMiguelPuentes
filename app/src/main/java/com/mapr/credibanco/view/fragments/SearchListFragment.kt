package com.mapr.credibanco.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mapr.credibanco.databinding.FragmentSearchListBinding
import com.mapr.credibanco.model.db.DataAuthorization
import com.mapr.credibanco.repositories.TransactionsRepository
import com.mapr.credibanco.services.requests.RequestCancellation
import com.mapr.credibanco.services.responses.ResponseCancellation
import com.mapr.credibanco.tools.Constants
import com.mapr.credibanco.tools.DialogFactory
import com.mapr.credibanco.view.adapters.AdapterAuthorizations
import com.mapr.credibanco.view.dialogs.DetailAuthCustomDialog
import com.mapr.credibanco.viewmodel.SearchListViewModel

class SearchListFragment : Fragment() {

    private lateinit var searchListViewModel: SearchListViewModel
    private var _binding: FragmentSearchListBinding? = null

    private lateinit var recyclerAuth: RecyclerView
    private lateinit var adapterAuth: AdapterAuthorizations

    // Search
    private lateinit var searchInput: EditText
    private lateinit var clicksearch: TextView
    private lateinit var emptyMsg: TextView
    private lateinit var viewList: LinearLayout

    //Components
    private lateinit var progress: AlertDialog

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchListViewModel =
            ViewModelProvider(this)[SearchListViewModel::class.java]

        _binding = FragmentSearchListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Search
        searchInput = binding.editSearch
        clicksearch = binding.clickSearch
        emptyMsg = binding.emptyMsg
        viewList = binding.viewList

        context?.let {
            progress = DialogFactory().setProgress(it)
            progress.show()
        }
        // Recycler de authorization
        recyclerAuth = binding.recyclerAuthorizations
        recyclerAuth.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerAuth.setHasFixedSize(true)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Adapter
        initializeAuthAdapter()
        searchListViewModel.authList.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                viewList.visibility = View.GONE
                emptyMsg.visibility = View.VISIBLE
            } else if (it.isNotEmpty()) {
                viewList.visibility = View.VISIBLE
                emptyMsg.visibility = View.GONE
                adapterAuth.setData(it)
                adapterAuth.notifyDataSetChanged()
            }
            progress.dismiss()
        })

        clicksearch.setOnClickListener {
            searchListViewModel.searchByReceiptId(requireContext(), searchInput.text.toString())
        }
    }

    private fun initializeAuthAdapter() {
        adapterAuth = AdapterAuthorizations(
            requireContext(),
            ArrayList<DataAuthorization>(),
            object : AdapterAuthorizations.OnClickDetail {
                override fun onClickDetail(item: DataAuthorization) {
                    val dialogDetail = DetailAuthCustomDialog(item)
                    dialogDetail.show(
                        childFragmentManager,
                        Constants.CUSTOM_DIALOG_DETAIL
                    )
                }

                override fun onClickCancel(item: DataAuthorization) {
                    requestCancellation(item)
                }
            })
        recyclerAuth.adapter = adapterAuth
    }

    /**
     *
     */
    private fun requestCancellation(item: DataAuthorization) {
        val auth: String = "${item.commerceCode}${item.terminalCode}"
        val receiptId: String = item.receiptId
        val rrn: String = item.rrn
        val request = RequestCancellation(receiptId, rrn)
        searchListViewModel.makeCancellation(
            auth,
            request,
            object : TransactionsRepository.OnListenerResponseCancellation {
                override fun onResponseCancellation(responseCancellation: ResponseCancellation) {
                    progress.dismiss()
                    if (responseCancellation.statusCode == Constants.AUTH_APPROVED_STATUS) {
                        val msg =
                            "La transacción ha sido anulada correctamente."
                        val dialog = DialogFactory().getDialog(msg, requireContext())
                        dialog.show()
                    } else if (responseCancellation.statusCode == Constants.AUTH_DECLINED_STATUS) {
                        val msg =
                            "Oops! La transacción no pudo ser anulada."
                        val dialog = DialogFactory().getDialog(msg, requireContext())
                        dialog.show()
                    }
                    searchListViewModel.getAuthInDb(requireContext())
                    searchInput.setText("")
                }

                override fun onFailedCancellation() {
                    val msg =
                        "Oops! Parece que hubo un error, verifica tu conexión."
                    val dialog = DialogFactory().getDialog(msg, requireContext())
                    dialog.show()
                    progress.dismiss()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        searchListViewModel.getAuthInDb(requireContext())
        searchInput.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
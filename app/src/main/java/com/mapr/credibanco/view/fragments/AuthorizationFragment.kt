package com.mapr.credibanco.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mapr.credibanco.databinding.FragmentAuthorizationBinding
import com.mapr.credibanco.repositories.TransactionsRepository
import com.mapr.credibanco.services.requests.RequestAuthorization
import com.mapr.credibanco.services.responses.ResponseAuthorization
import com.mapr.credibanco.tools.Constants
import com.mapr.credibanco.tools.DialogFactory
import com.mapr.credibanco.tools.Utils
import com.mapr.credibanco.viewmodel.AuthorizationViewModel

class AuthorizationFragment : Fragment() {

    private lateinit var authorizationViewModel: AuthorizationViewModel
    private var _binding: FragmentAuthorizationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Components
    private lateinit var progress: AlertDialog
    private lateinit var uuid: TextView
    private lateinit var commerce: TextView
    private lateinit var terminal: TextView
    private lateinit var amount: TextView
    private lateinit var card: TextView
    private lateinit var sendButton: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        context?.let {
            progress = DialogFactory().setProgress(it)
        }
        authorizationViewModel =
            ViewModelProvider(this@AuthorizationFragment)[AuthorizationViewModel::class.java]

        uuid = binding.txtUuid
        commerce = binding.txtCommerceCode
        terminal = binding.txtTerminalCode
        amount = binding.txtAmount
        card = binding.txtCard
        sendButton = binding.requestAuthorization

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Components Initialization
        uuid.text = Utils().generateUUID()
        commerce.text = Constants.DEFAULT_COMMERCE_CODE
        terminal.text = Constants.DEFAULT_TERMINAL_CODE
        amount.text = Constants.DEFAULT_AMOUNT
        card.text = Constants.DEFAULT_CARD

        sendButton.setOnClickListener {
            progress.show()
            requestAuthorization()
        }
    }

    /**
     *
     */
    private fun requestAuthorization() {
        val auth: String = "${commerce.text}${terminal.text}"
        val uuid: String = uuid.text.toString()
        val commerce: String = commerce.text.toString()
        val terminal: String = terminal.text.toString()
        val amount: String = amount.text.toString()
        val card: String = card.text.toString()
        val request = RequestAuthorization(uuid, commerce, terminal, amount, card)
        authorizationViewModel.makeAuthorization(
            auth,
            request,
            object : TransactionsRepository.OnListenerResponseAuthorization {
                override fun onResponseAuth(responseAuthorization: ResponseAuthorization) {
                    progress.dismiss()
                    if (responseAuthorization.statusCode == Constants.AUTH_APPROVED_STATUS) {
                        val msg =
                            "La transacción ha sido aprobada, se ha guardado en la base de datos y la podrás consultar en la lista de transacciones."
                        val dialog = DialogFactory().getDialog(msg, requireContext())
                        dialog.show()
                    } else if (responseAuthorization.statusCode == Constants.AUTH_DECLINED_STATUS) {
                        val msg =
                            "Oops! La transacción no fue aprobada."
                        val dialog = DialogFactory().getDialog(msg, requireContext())
                        dialog.show()
                    }

                }

                override fun onFailedAuth() {
                    progress.dismiss()
                    val msg =
                        "Oops! Parece que hubo un error, verifica tu conexión."
                    val dialog = DialogFactory().getDialog(msg, requireContext())
                    dialog.show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
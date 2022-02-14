package com.mapr.credibanco.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.mapr.credibanco.R
import com.mapr.credibanco.model.db.DataAuthorization


class DetailAuthCustomDialog(private var item: DataAuthorization) : DialogFragment() {

    // Components
    private lateinit var closeDialog: TextView

    private lateinit var terminal: TextView
    private lateinit var amount: TextView
    private lateinit var card: TextView
    private lateinit var uuid: TextView
    private lateinit var commerce: TextView

    private lateinit var receiptId: TextView
    private lateinit var rrn: TextView
    private lateinit var statusCode: TextView
    private lateinit var statusDescription: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View =
            inflater.inflate(R.layout.item_authorization_detail, container, false)

        closeDialog = rootView.findViewById(R.id.click_close_dialog)
        terminal = rootView.findViewById(R.id.txt_terminal_code)
        amount = rootView.findViewById(R.id.txt_amount)
        card = rootView.findViewById(R.id.txt_card)
        uuid = rootView.findViewById(R.id.txt_uuid)
        commerce = rootView.findViewById(R.id.txt_commerce_code)

        receiptId = rootView.findViewById(R.id.txt_receipt_id)
        rrn = rootView.findViewById(R.id.txt_rrn)
        statusCode = rootView.findViewById(R.id.txt_status_code)
        statusDescription = rootView.findViewById(R.id.txt_status_description)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeDialog.setOnClickListener {
            dismiss()
        }
        updateItemsInformation()
    }

    private fun updateItemsInformation() {
        receiptId.text = item.receiptId
        rrn.text = item.rrn
        statusCode.text = item.statusCode
        statusDescription.text = item.statusDescription

        uuid.text = item.uuid
        terminal.text = item.terminalCode
        amount.text = item.amount
        card.text = item.card
        commerce.text = item.commerceCode
    }
}
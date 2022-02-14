package com.mapr.credibanco.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mapr.credibanco.model.db.DataAuthorization
import com.mapr.credibanco.R

class AdapterAuthorizations(
    private val context: Context,
    private var list: List<DataAuthorization>,
    private var listener: OnClickDetail
) :
    RecyclerView.Adapter<AdapterAuthorizations.ViewHolder>() {

    fun setData(list: List<DataAuthorization>) {
        this.list = list
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        var receiptId: TextView = mView.findViewById(R.id.txt_receipt_id)
        var terminal: TextView = mView.findViewById(R.id.txt_terminal_code)
        var amount: TextView = mView.findViewById(R.id.txt_amount)
        var card: TextView = mView.findViewById(R.id.txt_card)
        var detail: TextView = mView.findViewById(R.id.click_detail)
        var cancel: TextView = mView.findViewById(R.id.click_cancel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_authorization, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.receiptId.text = item.receiptId
        holder.terminal.text = item.terminalCode
        holder.amount.text = item.amount
        holder.card.text = item.card

        //holder.descriptionSale.text = item.description
        holder.detail.setOnClickListener {
            listener.onClickDetail(item)
        }
        holder.cancel.setOnClickListener {
            listener.onClickCancel(item)
        }
    }

    interface OnClickDetail {
        fun onClickDetail(item: DataAuthorization)
        fun onClickCancel(item: DataAuthorization)
    }
}
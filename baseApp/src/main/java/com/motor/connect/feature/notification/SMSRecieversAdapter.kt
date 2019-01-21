package com.motor.connect.feature.notification

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.feature.area.R
import com.motor.connect.feature.adapter.BindableAdapter
import com.motor.connect.feature.model.SmsModel

class SMSRecieversAdapter(val onClick: (SmsModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindableAdapter<SmsModel> {

    private var smsRecievers = emptyList<SmsModel>()

    override fun setData(items: List<SmsModel>) {
        smsRecievers = items
        notifyDataSetChanged()
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sms_recievers, parent, false))
    }

    override fun getItemCount() = smsRecievers.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {

            //(holder as ItemViewHolder).container.setBackgroundColor(R.drawable.border_rect_bg_selected)
            onClick(smsRecievers[position])
        }

        (holder as ItemViewHolder).name.text = smsRecievers[position].contactName
        holder.phone.text = smsRecievers[position].phoneNumber
        holder.content.text = smsRecievers[position].messageContent
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container: LinearLayout = itemView.findViewById(R.id.item_container) as LinearLayout
        var name: TextView = itemView.findViewById(R.id.txt_contact) as TextView
        var phone: TextView = itemView.findViewById(R.id.txt_phone) as TextView
        var content: TextView = itemView.findViewById(R.id.txt_content) as TextView
    }
}
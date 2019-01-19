package com.motor.connect.feature.data

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.feature.area.R
import com.motor.connect.feature.model.AreaModel
import kotlinx.android.synthetic.main.item_data.view.*


class UserAdapter(val onClick: (AreaModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindableAdapter<AreaModel> {

    val HEADER_TYPE = 0
    val ITEM_TYPE = 1

    override fun setData(items: List<AreaModel>) {
        areas = items
        notifyDataSetChanged()
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    var areas = emptyList<AreaModel>()

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return UserHolder(inflater.inflate(R.layout.item_data, parent, false))
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View
        when (viewType) {
            HEADER_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.header_item_view, parent, false)
                return HeaderViewHolder(view)
            }
            ITEM_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
                return ItemViewHolder(view)
            }

        }
        return  view

    }

    override fun getItemCount() = areas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            Log.d("hqdat", "===  Item Click ==== ")

            onClick(areas[position])
        }

        when (position) {
            HEADER_TYPE -> {
                (holder as HeaderViewHolder).txtTitle.text = "Title"
            }
            ITEM_TYPE -> {
                (holder as ItemViewHolder).txtTitle.text = "Title"
            }

        }
    }

//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.bind(areas[position])
//
//        holder.itemView.setOnClickListener {
//            Log.d("hqdat", "===  Item Click ==== ")
//
//            onClick(areas[position])
//        }
//    }

//    override fun onBindViewHolder(holder: UserHolder, position: Int) {
//        holder.bind(areas[position])
//
//        holder.itemView.setOnClickListener {
//            Log.d("hqdat", "===  Item Click ==== ")
//
//            onClick(areas[position])
//        }
//    }


    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtTitle: TextView

        init {

            this.txtTitle = itemView.findViewById(R.id.txt_title) as TextView

        }

    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView
        var phone: TextView
        var status: TextView
        var schedule: TextView

        init {

            this.name = itemView.findViewById(R.id.txt_name) as TextView
            this.phone = itemView.findViewById(R.id.txt_phone) as TextView
            this.status = itemView.findViewById(R.id.txt_status) as TextView
            this.schedule = itemView.findViewById(R.id.txt_schedule) as TextView

        }
    }

    override fun getItemViewType(position: Int): Int {

        when (position) {
            0 -> return HEADER_TYPE
            1 -> return ITEM_TYPE
            else -> return ITEM_TYPE
        }


    }


    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(areaInfo: AreaModel) {

            itemView.txt_name.text = "Name: ${areaInfo.getAreaName()}"
            itemView.txt_phone.text = "Phone: ${areaInfo.getAreaPhone()}"
            itemView.txt_status.text = "Status: ${areaInfo.getStatus()}"

            if (areaInfo.getSchedule().isNullOrEmpty())
                itemView.txt_schedule.visibility = View.GONE

            itemView.txt_schedule.text = "Status: ${areaInfo.getSchedule()}"
        }
    }
}
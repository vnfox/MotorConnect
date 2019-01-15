package com.motor.connect.feature.data

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.feature.area.R
import com.motor.connect.feature.model.AreaModel
import kotlinx.android.synthetic.main.item_data.view.*


class UserAdapter(val onClick: (AreaModel) -> Unit) : RecyclerView.Adapter<UserAdapter.UserHolder>(), BindableAdapter<AreaModel> {

    override fun setData(items: List<AreaModel>) {
        areas = items
        notifyDataSetChanged()
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    var areas = emptyList<AreaModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserHolder(inflater.inflate(R.layout.item_data, parent, false))
    }

    override fun getItemCount() = areas.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(areas[position])

        holder.itemView.setOnClickListener {
            Log.d("hqdat", "===  Item Click ==== ")

            onClick(areas[position])
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
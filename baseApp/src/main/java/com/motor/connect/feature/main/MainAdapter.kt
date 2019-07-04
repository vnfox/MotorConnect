package com.motor.connect.feature.main

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.motor.connect.R
import com.motor.connect.feature.adapter.BindableAdapter
import com.motor.connect.feature.model.AreaModel

class MainAdapter(val onClick: (AreaModel, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindableAdapter<AreaModel> {

    private var areas = emptyList<AreaModel>()

    override fun setData(items: List<AreaModel>) {
        areas = items
        notifyDataSetChanged()
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_area, parent, false))
    }

    override fun getItemCount() = areas.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            onClick(areas[position], position)
        }
        (holder as ItemViewHolder).name.text = areas[position].areaName
        holder.phone.text = areas[position].areaPhone
        holder.vanused.text = "Số van sử dụng: " + areas[position].areaVans.size.toString()

        if(areas[position].areaVans.size > 0){
            areas[position].areaVans.forEach {
                if(it.schedule.isNotEmpty()){
                    holder.status.text = "Đang hoạt động"
                }else{
                    holder.status.text = "Chưa có lịch tưới"
                }
            }
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.txt_name) as TextView
        var phone: TextView = itemView.findViewById(R.id.txt_phone) as TextView
        var status: TextView = itemView.findViewById(R.id.txt_status) as TextView
        var vanused: TextView = itemView.findViewById(R.id.txt_van_number) as TextView

    }
}
package com.motor.connect.feature.setting.schedule

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.feature.area.R
import com.motor.connect.feature.adapter.BindableAdapter
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.utils.StringUtil
import com.motor.connect.utils.StringUtils

class SettingScheduleAdapter(val onClick: (AreaModel, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindableAdapter<AreaModel> {

    private var areas = emptyList<AreaModel>()

    override fun setData(items: List<AreaModel>) {
        areas = items
        notifyDataSetChanged()
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.schedule_item_view, parent, false))
    }

    override fun getItemCount() = areas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            onClick(areas[position], position)
        }
        (holder as ItemViewHolder).name.text = areas[position].areaName
        holder.vanused.text = "Sử dụng: " + areas[position].areaVans.size.toString() + " van"

        if (StringUtils.isNullOrEmpty(areas[position].areaSchedule))
            holder.schedule.text = "Chưa cài đặt lịch tưới"
        else
            holder.schedule.text = "Ngày tưới " + StringUtil.getCountWorkingDay(areas[position].areaSchedule) + " lần"
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.txt_name) as TextView
        var vanused: TextView = itemView.findViewById(R.id.txt_van_number) as TextView
        var schedule: TextView = itemView.findViewById(R.id.txt_schedule) as TextView
    }
}
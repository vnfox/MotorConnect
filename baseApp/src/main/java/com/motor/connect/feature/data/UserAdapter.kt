package com.motor.connect.feature.data

import android.annotation.SuppressLint
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

class UserAdapter(val onClick: (AreaModel, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindableAdapter<AreaModel> {

    private var areas = emptyList<AreaModel>()

    override fun setData(items: List<AreaModel>) {
        areas = items
        notifyDataSetChanged()
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false))
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

        if (StringUtils.isNullOrEmpty(areas[position].areaStatus))
            holder.status.text = "Trạng thái: Đang tắt "
        else
            holder.status.text = "Trạng thái: " + areas[position].areaStatus

        if (StringUtils.isNullOrEmpty(areas[position].areaSchedule)) {
            holder.schedule.text = "Lịch tưới: Chưa cài đặt lịch tưới"
            holder.repeat.visibility = View.GONE
        } else {
            holder.schedule.text = "Lịch tuới: ngày tưới " + StringUtil.getCountWorkingDay(areas[position].areaSchedule) + " lần"
            holder.repeat.visibility = View.VISIBLE
            holder.repeat.text = StringUtil.getScheduleRepeatDay(areas[position].areaSchedule)
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.txt_name) as TextView
        var phone: TextView = itemView.findViewById(R.id.txt_phone) as TextView
        var status: TextView = itemView.findViewById(R.id.txt_status) as TextView
        var vanused: TextView = itemView.findViewById(R.id.txt_van_number) as TextView
        var schedule: TextView = itemView.findViewById(R.id.txt_schedule) as TextView
        var repeat: TextView = itemView.findViewById(R.id.txt_repeat) as TextView

    }
}
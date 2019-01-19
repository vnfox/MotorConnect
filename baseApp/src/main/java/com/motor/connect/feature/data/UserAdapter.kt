package com.motor.connect.feature.data

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.feature.area.R
import com.motor.connect.feature.model.AreaModel
import kotlinx.android.synthetic.main.item_data.view.*

class UserAdapter(val onClick: (AreaModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindableAdapter<AreaModel> {

    private val HEADER_TYPE = 0
    private val ITEM_TYPE = 1

    private var areas = emptyList<AreaModel>()

    override fun setData(items: List<AreaModel>) {
        areas = items
        notifyDataSetChanged()
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            HEADER_TYPE -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.header_item_view, parent, false))
            // item view
            else -> ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false))
        }
    }

    override fun getItemCount() = areas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            onClick(areas[position])
        }

        when (position) {
            HEADER_TYPE -> {
                (holder as HeaderViewHolder).txtTitle.text = "Title"
            }
            ITEM_TYPE -> {
                (holder as ItemViewHolder).name.text = areas[position].getAreaName()
                holder.phone.text = areas[position].getAreaPhone()
                holder.status.text = areas[position].getStatus()
                holder.schedule.text = areas[position].getSchedule()
            }
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtTitle: TextView = itemView.findViewById(R.id.txt_title) as TextView
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.txt_name) as TextView
        var phone: TextView = itemView.findViewById(R.id.txt_phone) as TextView
        var status: TextView = itemView.findViewById(R.id.txt_status) as TextView
        var schedule: TextView = itemView.findViewById(R.id.txt_schedule) as TextView
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER_TYPE
            1 -> ITEM_TYPE
            else -> ITEM_TYPE
        }
    }
}
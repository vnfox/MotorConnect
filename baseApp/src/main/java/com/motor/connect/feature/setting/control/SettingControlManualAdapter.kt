package com.motor.connect.feature.setting.control

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.feature.area.R
import com.motor.connect.feature.adapter.BindableAdapter
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk


class SettingControlManualAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        BindableAdapter<VanModel> {

    private var areaVan = emptyList<VanModel>()

    override fun setData(items: List<VanModel>) {
        areaVan = items
        notifyDataSetChanged()
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_manual_control, parent, false))
    }

    override fun getItemCount() = areaVan.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).vanNumber.text = "0" + (position + 1).toString()
        holder.vanId.text = "Van " + areaVan[position].vanId

        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            when {
                isChecked -> holder.switchStatus.text = "ON"
                else -> holder.switchStatus.text = "OFF"
            }
            areaVan[position].manual = isChecked
            Hawk.put(MotorConstants.KEY_PUT_LIST_VAN_MODEL, areaVan)
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var vanId: TextView = itemView.findViewById(R.id.van_id) as TextView
        var vanNumber: TextView = itemView.findViewById(R.id.area) as TextView
        var switch: SwitchCompat = itemView.findViewById(R.id.switch_control) as SwitchCompat
        var switchStatus: TextView = itemView.findViewById(R.id.switch_status) as TextView
    }
}
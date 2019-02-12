package com.motor.connect.feature.setting.van

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.feature.area.R
import com.motor.connect.feature.adapter.BindableAdapter
import com.motor.connect.feature.model.VanModel

class SettingAreaVanAdapter(val onClick: (VanModel, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindableAdapter<VanModel> {

    private var areaVan = emptyList<VanModel>()

    override fun setData(items: List<VanModel>) {
        areaVan = items
        notifyDataSetChanged()
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_vans_used, parent, false))
    }

    override fun getItemCount() = areaVan.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            onClick(areaVan[position], position)
        }


        (holder as ItemViewHolder).vanId.text = "Van " + areaVan[position].vanId
        holder.vanUsed.isChecked = areaVan[position].vanStatus

        holder.vanUsed.setOnClickListener {

            Log.d("hqdat", ">>>    " + holder.vanUsed.isChecked)
            areaVan[position].vanStatus = holder.vanUsed.isChecked
        }
    }

    fun getDataView(): List<VanModel> {
        return areaVan
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var vanId: TextView = itemView.findViewById(R.id.van_id) as TextView
        var vanUsed: SwitchCompat = itemView.findViewById(R.id.sw_van) as SwitchCompat
    }
}
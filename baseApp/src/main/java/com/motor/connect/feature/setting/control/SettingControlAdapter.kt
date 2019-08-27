package com.motor.connect.feature.setting.control

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.motor.connect.R
import com.motor.connect.feature.adapter.BindableAdapter
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.feature.setting.van.SettingAreaVanAdapter
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.getVanId
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.item_view_manual_control.view.*


class SettingControlAdapter(val onClick: SettingControlActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
		BindableAdapter<VanModel> {
	
	private var areaVan = emptyList<VanModel>()
	lateinit var itemClick: ItemListener
	
	override fun setData(items: List<VanModel>) {
		areaVan = items
		notifyDataSetChanged()
		itemClick = onClick
	}
	
	override fun changedPositions(positions: Set<Int>) {
		positions.forEach(this::notifyItemChanged)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		
		return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_manual_control, parent, false))
	}
	
	override fun getItemCount() = areaVan.size
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		(holder as ItemViewHolder).vanNumber.text = getVanId(position + 1)
		holder.vanId.text = "Van " + areaVan[position].vanId
		holder.switch.isChecked = areaVan[position].manual
		holder.switch.setOnCheckedChangeListener { _, isChecked ->
			when {
				isChecked -> {
					holder.switchStatus.text = "ON"
					holder.switch.isChecked = true
				}
				else -> {
					holder.switchStatus.text = "OFF"
					holder.switch.isChecked = false
				}
			}
			itemClick.onChangeStatus(position, holder)
		}
		holder.setIsRecyclable(false)
	}
	
	class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		
		var vanId: TextView = itemView.findViewById(R.id.van_id) as TextView
		var vanNumber: TextView = itemView.findViewById(R.id.area) as TextView
		var switch: SwitchCompat = itemView.findViewById(R.id.switch_control) as SwitchCompat
		var switchStatus: TextView = itemView.findViewById(R.id.switch_status) as TextView
	}
	
	interface ItemListener {
		
		fun onChangeStatus(position: Int, holder: ItemViewHolder)
		
	}
}
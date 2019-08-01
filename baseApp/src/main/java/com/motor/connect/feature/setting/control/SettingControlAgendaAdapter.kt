package com.motor.connect.feature.setting.control

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.motor.connect.R
import com.motor.connect.feature.adapter.BindableAdapter
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.StringUtil
import com.motor.connect.utils.getVanId
import com.orhanobut.hawk.Hawk

class SettingControlAgendaAdapter(val onClick: SettingControlActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
		BindableAdapter<VanModel> {
	
	lateinit var itemClick: ItemListener
	private var areaVan = emptyList<VanModel>()
	
	private var maxProgress: Int = 1800
	private var currentProgress: Int = 0
	
	override fun setData(items: List<VanModel>) {
		areaVan = items
		notifyDataSetChanged()
		itemClick = onClick
	}
	
	override fun changedPositions(positions: Set<Int>) {
		positions.forEach(this::notifyItemChanged)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		
		return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_agenda_control, parent, false))
	}
	
	override fun getItemCount() = areaVan.size
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		(holder as ItemViewHolder).vanNumber.text =  getVanId(position + 1)
		holder.vanId.text = "Van " + areaVan[position].vanId
		
		holder.timeWorking.setOnClickListener {
			itemClick.onSetDuration(position, holder)
		}
		
		if (areaVan[position].duration != null && areaVan[position].duration.isNotEmpty()) {
			holder.vanId.text = StringUtil.getNameAndStatusVan(areaVan[position].vanId, true)
			holder.timeWorking.text = areaVan[position].duration
			
			maxProgress = areaVan[position].duration.toInt() * 60
			holder.workingProgress?.maxProgress = maxProgress.toDouble()
			holder.workingProgress?.setCurrentProgress(currentProgress.toDouble())
			holder.workingProgress?.setProgressTextAdapter(timeText)
			
			//onWorkingProgress(holder)
		} else {
			//show status OFF
			holder.vanId.text = StringUtil.getNameAndStatusVan(areaVan[position].vanId, false)
			holder.timeWorking.text = "00"
		}
		
		areaVan[position].vanId = (position + 1).toString()
		Hawk.put(MotorConstants.KEY_PUT_VAN_MODEL, areaVan[position])
		Hawk.put(MotorConstants.KEY_PUT_LIST_VAN_MODEL, areaVan)
	}
	
	fun updateDuration(position: Int, holder: ItemViewHolder, value: String) {
		holder.timeWorking.text = value
		holder.vanId.text = StringUtil.getNameAndStatusVan(areaVan[position].vanId, true)
		
		areaVan[position].duration = value
		Hawk.put(MotorConstants.KEY_PUT_VAN_MODEL, areaVan[position])
		Hawk.put(MotorConstants.KEY_PUT_LIST_VAN_MODEL, areaVan)
	}
	
	private fun onWorkingProgress(holder: ItemViewHolder) {
		val handler = android.os.Handler()
		Thread(Runnable {
			while (currentProgress < maxProgress) {
				currentProgress += 1
				
				handler.post {
					holder.workingProgress?.setCurrentProgress(currentProgress.toDouble())
				}
				try {
					Thread.sleep(1000)
				} catch (e: InterruptedException) {
					e.printStackTrace()
				}
			}
		}).start()
	}
	
	private val timeText = CircularProgressIndicator.ProgressTextAdapter { time ->
		var time = time
		time %= 3600.0
		
		val minutes = (time / 60).toInt()
		val seconds = (time % 60).toInt()
		val sb = StringBuilder()
		if (minutes < 10) {
			sb.append(0)
		}
		sb.append(minutes).append(":")
		if (seconds < 10) {
			sb.append(0)
		}
		sb.append(seconds)
		sb.toString()
	}
	
	class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		
		var vanId: TextView = itemView.findViewById(R.id.van_id) as TextView
		var vanNumber: TextView = itemView.findViewById(R.id.area) as TextView
		var timeWorking: TextView = itemView.findViewById(R.id.time_working)
		var workingProgress: CircularProgressIndicator = itemView.findViewById(R.id.working_progress) as CircularProgressIndicator
		
	}
	
	interface ItemListener {
		fun onSetDuration(position: Int, holder: ItemViewHolder)
	}
}
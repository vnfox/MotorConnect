package com.motor.connect.feature.setting.agenda

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.motor.connect.R
import com.motor.connect.feature.adapter.BindableAdapter
import com.motor.connect.feature.model.RepeatModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.getVanId
import com.orhanobut.hawk.Hawk.put
import java.util.*

class SettingAgendaAdapter(val onClick: SettingAgendaActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindableAdapter<VanModel> {

    lateinit var itemClick: ItemListener
    private var areaVan = emptyList<VanModel>()
    private var vanModel = VanModel()

    var stepSchedule = 0
    private var currentPosition = -1

    override fun setData(items: List<VanModel>) {
        areaVan = items
        notifyDataSetChanged()
        itemClick = onClick
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_agenda_view, parent, false))
    }

    override fun getItemCount() = areaVan.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        vanModel = areaVan[position]
        (holder as ItemViewHolder).areaName.text = "Van " + getVanId(position + 1)
        holder.areaId.text = getVanId(position + 1)

        //=======Update Schedule =====
        if (areaVan[position].schedule != null) {

            holder.duration.text = areaVan[position].duration
            stepSchedule = areaVan[position].schedule.size

            updateSchedule(stepSchedule, holder)
        } else {
            //Default hide
            holder.schedule1.visibility = View.GONE
            holder.schedule2.visibility = View.GONE
            holder.schedule3.visibility = View.GONE
            holder.schedule4.visibility = View.GONE

            //disable button remove
            holder.btnRemove.isEnabled = false
        }

        holder.btnAdd.setOnClickListener {
            if (currentPosition != position) {
                stepSchedule = areaVan[position].schedule.size
                currentPosition = position

                checkStatusButton(stepSchedule, holder)
                itemClick.onAddSchedule(position, stepSchedule, holder)

                var schedule = ArrayList<String>()
                schedule.addAll(areaVan[position].schedule)
                schedule.add("09:00")
                areaVan[position].schedule = schedule

                put(MotorConstants.KEY_PUT_VAN_MODEL, areaVan[position])

            } else {
                stepSchedule = areaVan[currentPosition].schedule.size
                checkStatusButton(stepSchedule, holder)
                itemClick.onAddSchedule(currentPosition, stepSchedule, holder)

                var schedule = ArrayList<String>()
                schedule.addAll(areaVan[currentPosition].schedule)
                schedule.add("09:00")
                areaVan[currentPosition].schedule = schedule

                put(MotorConstants.KEY_PUT_VAN_MODEL, areaVan[currentPosition])
            }
        }
        holder.btnRemove.setOnClickListener {
            if (currentPosition != position) {
                stepSchedule = areaVan[position].schedule.size
                currentPosition = position
                checkStatusButton(stepSchedule, holder)
                areaVan[position].schedule.removeAt(areaVan[position].schedule.size - 1)
                put(MotorConstants.KEY_PUT_VAN_MODEL, areaVan[position])
            } else {
                stepSchedule = areaVan[currentPosition].schedule.size
                checkStatusButton(stepSchedule, holder)
                areaVan[position].schedule.removeAt(areaVan[currentPosition].schedule.size - 1)
                put(MotorConstants.KEY_PUT_VAN_MODEL, areaVan[currentPosition])
            }

            itemClick.onRemoveSchedule(position, stepSchedule, holder)
        }

        holder.duration.setOnClickListener {
            itemClick.onSetDuration(position, holder, currentPosition)
            currentPosition = position
        }
        holder.schedule1.setOnClickListener { itemClick.onSchedule(position, holder.schedule1, 0) }
        holder.schedule2.setOnClickListener { itemClick.onSchedule(position, holder.schedule2, 1) }
        holder.schedule3.setOnClickListener { itemClick.onSchedule(position, holder.schedule3, 2) }
        holder.schedule4.setOnClickListener { itemClick.onSchedule(position, holder.schedule4, 3) }
        
    }

    private fun checkStatusButton(step: Int, holder: ItemViewHolder) {
        when (step) {
            0 -> {
                holder.btnAdd.isEnabled = true
                holder.btnRemove.isEnabled = false
            }
            4 -> {
                holder.btnAdd.isEnabled = false
                holder.btnRemove.isEnabled = true
            }
            else -> {
                holder.btnAdd.isEnabled = true
                holder.btnRemove.isEnabled = true
            }
        }
    }

    private fun updateSchedule(step: Int, holder: ItemViewHolder) {
        stepSchedule = step
        //Update show UI
        var schedule: MutableList<String> = vanModel.schedule
        when (step) {
            1 -> {
                holder.schedule1.visibility = View.VISIBLE
                holder.schedule2.visibility = View.GONE
                holder.schedule3.visibility = View.GONE
                holder.schedule4.visibility = View.GONE

                holder.schedule1.text = schedule[0]
            }
            2 -> {
                holder.schedule1.visibility = View.VISIBLE
                holder.schedule2.visibility = View.VISIBLE
                holder.schedule3.visibility = View.GONE
                holder.schedule4.visibility = View.GONE

                holder.schedule1.text = schedule[0]
                holder.schedule2.text = schedule[1]
            }
            3 -> {
                holder.schedule1.visibility = View.VISIBLE
                holder.schedule2.visibility = View.VISIBLE
                holder.schedule3.visibility = View.VISIBLE
                holder.schedule4.visibility = View.GONE

                holder.schedule1.text = schedule[0]
                holder.schedule2.text = schedule[1]
                holder.schedule3.text = schedule[2]
            }
            4 -> {
                holder.schedule1.visibility = View.VISIBLE
                holder.schedule2.visibility = View.VISIBLE
                holder.schedule3.visibility = View.VISIBLE
                holder.schedule4.visibility = View.VISIBLE

                holder.btnAdd.isEnabled = false
                holder.btnRemove.isEnabled = true

                holder.schedule1.text = schedule[0]
                holder.schedule2.text = schedule[1]
                holder.schedule3.text = schedule[2]
                holder.schedule4.text = schedule[3]
            }
            else -> {
                holder.btnAdd.isEnabled = true
                holder.btnRemove.isEnabled = false
                holder.schedule1.visibility = View.GONE
                holder.schedule2.visibility = View.GONE
                holder.schedule3.visibility = View.GONE
                holder.schedule4.visibility = View.GONE
            }
        }
    }

    fun updateScheduleAdded(step: Int, holder: ItemViewHolder) {
        stepSchedule = step
        //Update show UI
        when (step) {
            1 -> {
                holder.schedule1.visibility = View.VISIBLE
                holder.schedule2.visibility = View.GONE
                holder.schedule3.visibility = View.GONE
                holder.schedule4.visibility = View.GONE
            }
            2 -> {
                holder.schedule1.visibility = View.VISIBLE
                holder.schedule2.visibility = View.VISIBLE
                holder.schedule3.visibility = View.GONE
                holder.schedule4.visibility = View.GONE
            }
            3 -> {
                holder.schedule1.visibility = View.VISIBLE
                holder.schedule2.visibility = View.VISIBLE
                holder.schedule3.visibility = View.VISIBLE
                holder.schedule4.visibility = View.GONE
            }
            4 -> {
                holder.schedule1.visibility = View.VISIBLE
                holder.schedule2.visibility = View.VISIBLE
                holder.schedule3.visibility = View.VISIBLE
                holder.schedule4.visibility = View.VISIBLE

                holder.btnAdd.isEnabled = false
                holder.btnRemove.isEnabled = true
            }
            else -> {
                holder.btnAdd.isEnabled = true
                holder.btnRemove.isEnabled = false
                holder.schedule1.visibility = View.GONE
                holder.schedule2.visibility = View.GONE
                holder.schedule3.visibility = View.GONE
                holder.schedule4.visibility = View.GONE
            }
        }
        holder.setIsRecyclable(false)
    }


    //============== Update Schedule ================

    fun updateDuration(position: Int, holder: ItemViewHolder, positionItem: Int, duration: String) {
        holder.duration.text = duration

        areaVan[position].duration = duration
        put(MotorConstants.KEY_PUT_VAN_MODEL, areaVan[position])
    }

    fun updateTimeSchedule(position: Int, textView: TextView, positionItem: Int, time: String) {
        textView.text = time

        areaVan[position].schedule[positionItem] = textView.text.toString()
        put(MotorConstants.KEY_PUT_VAN_MODEL, areaVan[position])
    }

    //=================== End =======================

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var areaId: TextView = itemView.findViewById(R.id.area_id) as TextView
        var areaName: TextView = itemView.findViewById(R.id.area_name) as TextView

        var duration: TextView = itemView.findViewById(R.id.timeDuration) as TextView

        var schedule1: TextView = itemView.findViewById(R.id.schedule1) as TextView
        var schedule2: TextView = itemView.findViewById(R.id.schedule2) as TextView
        var schedule3: TextView = itemView.findViewById(R.id.schedule3) as TextView
        var schedule4: TextView = itemView.findViewById(R.id.schedule4) as TextView

        var btnRemove: ImageView = itemView.findViewById(R.id.btnRemove) as ImageView
        var btnAdd: ImageView = itemView.findViewById(R.id.btnAdd) as ImageView
    }

    interface ItemListener {

        fun onAddSchedule(position: Int, stepSchedule: Int, holder: ItemViewHolder)

        fun onRemoveSchedule(position: Int, stepSchedule: Int, holder: ItemViewHolder)

        fun onSetDuration(position: Int, holder: ItemViewHolder, positionItem: Int)

        fun onSchedule(position: Int, textView: TextView, positionItem: Int)
    }

}
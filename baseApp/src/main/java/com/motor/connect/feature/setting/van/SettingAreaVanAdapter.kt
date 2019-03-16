package com.motor.connect.feature.setting.van

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.feature.area.R
import com.motor.connect.feature.adapter.BindableAdapter
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.RepeatModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class SettingAreaVanAdapter(val onClick: SettingAreaVanActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BindableAdapter<VanModel> {

    lateinit var itemClick: ItemListener
    private var areaVan = emptyList<VanModel>()

    private var vanModel = VanModel()

    var stepSchedule = 0
    private var currentPosition = 0

    override fun setData(items: List<VanModel>) {
        areaVan = items
        notifyDataSetChanged()
        itemClick = onClick
    }

    override fun changedPositions(positions: Set<Int>) {
        positions.forEach(this::notifyItemChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_vans_used, parent, false))
    }

    override fun getItemCount() = areaVan.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        vanModel = areaVan[position]

        (holder as ItemViewHolder).areaName.text = "Van " + areaVan[position].vanId

        holder.areaId.text = "0" + (position + 1).toString()


        //=======Update Schedule =====
        if (areaVan[position].schedule != null) {
            Log.d("hqdat", "=========")
            holder.duration.text = areaVan[position].duration
            stepSchedule = areaVan[position].schedule.size

            updateSchedule(position, stepSchedule, holder)
        } else {
            //Default hide
            holder.schedule1.visibility = View.GONE
            holder.schedule2.visibility = View.GONE
            holder.schedule3.visibility = View.GONE
            holder.schedule4.visibility = View.GONE

            //disable button remove
            holder.btnRemove.isEnabled = false
        }

        //======= Update Repeat ===============
        val repeat = areaVan[position]?.repeatModel as RepeatModel

        holder.t2.isChecked = repeat.statusT2
        holder.t3.isChecked = repeat.statusT3
        holder.t4.isChecked = repeat.statusT4
        holder.t5.isChecked = repeat.statusT5
        holder.t6.isChecked = repeat.statusT6
        holder.t7.isChecked = repeat.statusT7
        holder.cn.isChecked = repeat.statusCN

        holder.btnAdd.setOnClickListener {
            if (position != currentPosition) {
                stepSchedule = 0
                //Check when have schedule
            }
            itemClick.onAddSchedule(position, stepSchedule, holder)
            currentPosition = position

        }
        holder.btnRemove.setOnClickListener {
            if (position != currentPosition) {
                stepSchedule = 0
                //Check when have schedule
            }
            itemClick.onRemoveSchedule(position, stepSchedule, holder)
            currentPosition = position
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

    fun updateSchedule(position: Int, step: Int, holder: ItemViewHolder) {
        stepSchedule = step
        //Update show UI
        var schedule: MutableList<String> = vanModel.schedule
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

        if (vanModel.schedule.size > 0) {
            var schedule: MutableList<String> = vanModel.schedule

            schedule[0] = holder.schedule1.text.toString()
            schedule[1] = holder.schedule2.text.toString()
            schedule[2] = holder.schedule3.text.toString()
            schedule[3] = holder.schedule4.text.toString()

//            vanModel.schedule = schedule
//            updateDataArea(position, vanModel)vanModel
        }
    }

    //============== Update Schedule ================

    fun updateDuration(position: Int, holder: ItemViewHolder, positionItem: Int, duration: String) {
        holder.duration.text = duration

        //Update data
//        vanModel.duration = duration
//        var areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)


        //areaModels[position].areaVans[positionItem].duration = vanModel.duration
        //Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
    }

    fun updateTimeSchedule(position: Int, textView: TextView, positionItem: Int, time: String) {

        textView.text = time

        //Update data with position
//        vanModel.schedule[positionItem] = time
//
//        var areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
//        areaModels[position].areaVans[positionItem].schedule = vanModel.schedule
//        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
    }


    private fun updateDataArea(position: Int, vanModel: VanModel, positionItem: Int) {
        var areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)

        areaModels[position].areaVans[positionItem].duration = vanModel.duration

        areaModels[position].areaVans[positionItem].schedule = vanModel.schedule
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)

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

        var t2: CheckBox = itemView.findViewById(R.id.t2) as CheckBox
        var t3: CheckBox = itemView.findViewById(R.id.t3) as CheckBox
        var t4: CheckBox = itemView.findViewById(R.id.t4) as CheckBox
        var t5: CheckBox = itemView.findViewById(R.id.t5) as CheckBox
        var t6: CheckBox = itemView.findViewById(R.id.t6) as CheckBox
        var t7: CheckBox = itemView.findViewById(R.id.t7) as CheckBox
        var cn: CheckBox = itemView.findViewById(R.id.cn) as CheckBox

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
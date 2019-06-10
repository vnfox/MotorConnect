package com.motor.connect.utils

import com.motor.connect.feature.model.RepeatModel


fun decimal2ATSSexagesimal(value: Int): String {
	var sexagesimalLetters = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k')
	var result: String = String()
	var num: Int
	num = value
	var sexagesimals: MutableList<Char> = mutableListOf<Char>()
	do {
		sexagesimals.add(sexagesimalLetters[num % 60])
		num = (num / 60)
	} while (num > 0)
	for (i in sexagesimals.size downTo 1) {
		result += sexagesimals.get(i - 1)
	}
	return result
}

fun getAvailableATS(zone: Int): String {
	val result = decimal2ATSSexagesimal(zone)
	if (result.length == 1) {
		return "0$result"
	}
	return result
}

fun getDayOfWeekATS(day: Int): String {
	val day = decimal2ATSSexagesimal(day)
	if (day.length == 1) {
		return "0$day"
	}
	return day
}

fun getScheduleTimeATS(schedule: String): String {
	val result = StringBuilder()
	val array = schedule.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
	result.append(decimal2ATSSexagesimal(array[0].toInt()))
	var minutes = decimal2ATSSexagesimal(array[1].toInt())
	if (minutes.contentEquals("0")) {
		minutes = "1"
	}
	result.append(minutes)
	
	return result.toString()
}

fun getTimeDurationATS(duration: String): String {
	var result: String = decimal2ATSSexagesimal(duration.toInt())
	if (result.length == 1) {
		return "0$result"
	}
	return result
}

var bit_mask = 0

fun getZoneAvailable(index: Int): Int {
	bit_mask = bit_mask or (1 shl (index - 1))
	
	return bit_mask
}

fun clearZone(index: Int) {
	bit_mask = bit_mask and (1 shl (index - 1)).inv()
}

fun getDecimalValueMon(value: Boolean): Int {
	if (value) {
		return 1
	}
	return 0
}

fun getDecimalValueTue(value: Boolean): Int {
	if (value) {
		return 2
	}
	return 0
}
fun getDecimalValueWed(value: Boolean): Int {
	if (value) {
		return 4
	}
	return 0
}
fun getDecimalValueThu(value: Boolean): Int {
	if (value) {
		return 8
	}
	return 0
}
fun getDecimalValueFri(value: Boolean): Int {
	if (value) {
		return 16
	}
	return 0
}
fun getDecimalValueSat(value: Boolean): Int {
	if (value) {
		return 32
	}
	return 0
}
fun getDecimalValueSun(value: Boolean): Int {
	if (value) {
		return 64
	}
	return 0
}

fun getDayOfWeek(position: Int, repeatModel: RepeatModel): Int {
	var scheduleRepeat = 0
	
	when (position) {
		0 -> scheduleRepeat = getDecimalValueMon(repeatModel.statusT2)
		1 -> scheduleRepeat = getDecimalValueTue(repeatModel.statusT3)
		2 -> scheduleRepeat = getDecimalValueWed(repeatModel.statusT4)
		3 -> scheduleRepeat = getDecimalValueThu(repeatModel.statusT5)
		4 -> scheduleRepeat = getDecimalValueFri(repeatModel.statusT6)
		5 -> scheduleRepeat = getDecimalValueSat(repeatModel.statusT7)
		6 -> scheduleRepeat = getDecimalValueSun(repeatModel.statusCN)
	}
	return scheduleRepeat
}
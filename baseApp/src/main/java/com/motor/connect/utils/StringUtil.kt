package com.motor.connect.utils

object StringUtil {
	
	fun comparePrefixPhone(phone: String): String {
		var result = phone
		if (result.contains("+84")) {
			result = result.replace("+84", "0")
		}
		return result
	}
	
	//==================== New ==============================================
	
	fun getNameAndStatusVan(vanId: String, on: Boolean): String {
		val result = StringBuilder()
		result.append("Van ")
		result.append(vanId)
		if (on) {
			result.append("          ON")
		} else {
			result.append("          OFF")
		}
		return result.toString()
	}
	
	fun getScheduleArea(schedule: List<String>, vanId: String, duration: String): Any? {
		val result = StringBuilder()
		result.append("Lịch Tưới ")
		result.append("khu vực ").append(vanId).append("\n")
		for (element in schedule) {
			result.append(getTime(element)).append("\n")
		}
		result.append(getDuration(duration)).append("\n")
		return result.toString()
	}
	
	private fun getTime(value: String): String {
		val result = StringBuilder()
		val array = value.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		result.append(" * Bắt đầu ").append(array[0]).append(" giờ ")
		result.append(array[1]).append(" phút")
		return result.toString()
	}
	
	private fun getDuration(value: String): String {
		val result = StringBuilder()
		result.append(" * Thời gian tưới ")
		if (!value.isNullOrEmpty()) {
			result.append(value)
			result.append(" phút")
		} else {
			result.append("chưa thêm")
		}
		return result.toString()
	}
}

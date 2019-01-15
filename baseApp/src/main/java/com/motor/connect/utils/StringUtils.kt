package com.motor.connect.utils

class StringUtils {
    companion object {
        private val EMPTY_STRING = ""

        fun isNullOrEmpty(string: String?) = string == null || EMPTY_STRING == string

        fun toEmptyIfNull(input: String?) = input ?: EMPTY_STRING
    }
}

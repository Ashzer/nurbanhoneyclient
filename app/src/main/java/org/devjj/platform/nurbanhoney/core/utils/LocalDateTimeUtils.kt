package org.devjj.platform.nurbanhoney.core.utils

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class LocalDateTimeUtils {
    companion object{
        private val parsingPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.000'Z'")
        fun parse(date: String) : LocalDateTime = LocalDateTime.parse(date,parsingPattern)
        private val displayPattern: DateTimeFormatter = DateTimeFormatter.ofPattern("yy년 MM월 dd일 HH:mm:ss")
        fun toString(date: LocalDateTime) = date.format(displayPattern).toString()
    }
}

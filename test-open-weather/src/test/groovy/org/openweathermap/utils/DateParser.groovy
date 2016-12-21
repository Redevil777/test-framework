package org.openweathermap.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by andrey on 19.12.16.
 */
class DateParser {
    static LocalDateTime parse (Object param) {
        println "dateparser" + param
        String parsingDate = param
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime date = LocalDateTime.parse(parsingDate, formatter)
        println date
        return date
    }
}

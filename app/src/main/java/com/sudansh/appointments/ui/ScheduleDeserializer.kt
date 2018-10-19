package com.sudansh.appointments.ui

import android.annotation.SuppressLint
import com.google.gson.*
import com.sudansh.apimodule.network.Schedule
import org.json.JSONException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Deserialize the response to list of schedules
 */
class ScheduleDeserializer : JsonDeserializer<Schedule> {

    @Throws(JsonParseException::class)
    override fun deserialize(element: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Schedule {
        val schedule = Schedule(mutableListOf())
        val listAvailable = element.asJsonObject.get("available") as JsonArray
        val iterator = listAvailable.iterator()
        while (iterator.hasNext()) {
            iterator.next().asJsonObject.entrySet().forEach { (key, value) ->
                convert(value.asJsonArray).map { getDateFormat("$key:$it") }.apply {
                    schedule.dates.addAll(this)
                }
            }
        }
        return schedule
    }
}

@SuppressLint("SimpleDateFormat")
fun getDateFormat(input: String): Date {
    try {
        return SimpleDateFormat("yyyy-MM-dd:HH").parse(input)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return Date()
}

fun convert(jArr: JsonArray): List<Any> {
    val list = ArrayList<Any>()
    try {
        jArr.forEach {
            list.add(it)
        }
    } catch (e: JSONException) {
    }

    return list
}
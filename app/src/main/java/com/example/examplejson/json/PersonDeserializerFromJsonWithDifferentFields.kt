package com.example.examplejson.json

import com.example.examplejson.data.Person
import com.example.examplejson.data.Student
import com.example.examplejson.data.Teacher
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type


class PersonDeserializerFromJsonWithDifferentFields :
    JsonDeserializer<Person?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        jElement: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Person {
        val jsonObject = jElement.asJsonObject

        return when (val type = jsonObject.get("type").asString) {
            "student" -> Person.Student(context!!.deserialize(jsonObject, Student::class.java))
            "teacher" -> Person.Teacher(context!!.deserialize(jsonObject, Teacher::class.java))
            else -> throw JsonParseException("Unknown type in json: $type")
        }

    }
}
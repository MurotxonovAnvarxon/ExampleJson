package com.example.examplejson

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.examplejson.data.Person
import com.example.examplejson.json.PersonDeserializerFromJsonWithDifferentFields
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // JSON faylini o'qish
        val jsonFileName = "MyJson.json"
        val jsonObject = readJsonFromAssets(this, jsonFileName)

        // JSONdan ma'lumotlarni olish
        val value = jsonObject?.getString("name")

        Log.d("TTT", "json:${value} ")

        setContent {
            val items = getSampleData()
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(5) {
                    PersonItems(persons = items)
                }
            }
        }
    }
}

@Composable
fun PersonItems(persons: List<Person>) {
    Column(modifier = Modifier.padding(top = 15.dp)) {
        persons.forEach { item ->
            when (item) {
                is Person.Student -> Text(text = "Student: \n name:${item.student.name}\n age:${item.student.age}\ncity:${item.student.city} \n")
                is Person.Teacher -> Text(text = "Teacher: \n name:${item.teacher.name}\n age:${item.teacher.age}\ncity:${item.teacher.city} ")
            }
        }
    }
}

private fun getSampleData(): List<Person> {
    val jsonString = """[
            {
                "type": "student",
                "name": "Harry Potter",
                "age": "25",
                "city":"Toshkent",
                "isStudent":"true",
                "grades":"85"
            },
            {
               "type": "teacher",
                "name": "Mark Harry",
                "age": "45",
                "city":"Farg'ona",
                "isStudent":"false",
                "grades":"125"
            }
        ]"""


    val gson = GsonBuilder()
        .registerTypeAdapter(Person::class.java, PersonDeserializerFromJsonWithDifferentFields())
        .create()

    val itemType = object : TypeToken<List<Person>>() {}.type
    return gson.fromJson(jsonString, itemType)
}


fun readJsonFromAssets(context: Context, fileName: String): JSONObject? {
    var json: String? = null

    try {
        // Aktivlarning papkasidagi fayl nomi
        val inputStream = context.assets.open(fileName)
        // Fayldan ma'lumotni olish
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        // Fayldagi ma'lumotni stringga o'zlashtirish
        json = String(buffer, Charset.defaultCharset())
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }

    // Stringdan JSONObject yaratish
    return JSONObject(json)
}


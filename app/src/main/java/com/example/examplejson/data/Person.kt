package com.example.examplejson.data

sealed class Person {
    data class Student(
        val student: com.example.examplejson.data.Student
    ) : Person()

    data class Teacher(
        val teacher: com.example.examplejson.data.Teacher
    ) : Person()
}
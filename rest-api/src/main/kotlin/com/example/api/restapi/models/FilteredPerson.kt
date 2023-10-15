package com.example.api.restapi.models

//Imports
import jakarta.persistence.*
import java.util.Date

data class FilteredPerson(

    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val dateOfBirth: Date,
    val age: Int

)
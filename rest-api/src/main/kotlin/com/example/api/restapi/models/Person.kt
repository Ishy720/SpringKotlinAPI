//Packages
package com.example.api.restapi.models

//Imports
import jakarta.persistence.*
import java.util.Date

@Entity
data class Person(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val name: String,
        val surname: String,
        val email: String,
        val phone: String,
        val dateOfBirth: Date,
        val age: Int,
        val username: String,
        val password: String
)
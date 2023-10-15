//Packages
package com.example.api.restapi.repositories

//Imports
import com.example.api.restapi.models.Person
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Repository
interface PersonRepository : JpaRepository<Person, Long> {

    fun findByUsername(username: String): Person?

}

/*
//Create a Person Repository to interact with the database
@Repository
interface PersonRepository : JpaRepository<Person, Long>*/
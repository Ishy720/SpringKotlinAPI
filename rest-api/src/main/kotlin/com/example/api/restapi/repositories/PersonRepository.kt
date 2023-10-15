//Packages
package com.example.api.restapi.repositories

//Imports
import com.example.api.restapi.models.Person
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Repository
interface PersonRepository : JpaRepository<Person, Long>
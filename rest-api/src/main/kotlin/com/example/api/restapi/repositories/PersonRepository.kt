//Packages
package com.example.api.restapi.repositories

//Imports
import com.example.api.restapi.models.Person
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Repository
interface PersonRepository : JpaRepository<Person, Long> {

    override fun findAll(pageable: Pageable): Page<Person>

    fun findById(id: Long, pageable: Pageable): Page<Person>

}
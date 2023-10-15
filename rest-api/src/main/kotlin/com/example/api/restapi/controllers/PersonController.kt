//Packages
package com.example.api.restapi.controllers

//Imports
import com.example.api.restapi.services.PersonService
import com.example.api.restapi.models.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


@RestController
class PersonController(

    //Plug in PersonService
    private val personService: PersonService

) {

    //CREATE ENDPOINT (admin)
    @PostMapping("/person")
    fun createPerson(@RequestBody payload: Person): ResponseEntity<Any> {
        try {
            val createdPerson = personService.createPerson(payload) //Make new person
            return ResponseEntity.ok(createdPerson) //Successful creation
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to create the new Person!") //Failed to create
        }
    }

    //RETRIEVE ENDPOINTS (GET ALL, GET ONE USER) (admin)
    @GetMapping("/persons")
    fun getAllPersons(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Any> {
        val pageable: Pageable = PageRequest.of(page, size)

        try {
            val page = personService.getAllPersons(pageable) //Get all Persons

            if (!page.isEmpty) {
                return ResponseEntity.ok(page) //Successfully retrieved Persons
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SERVER MESSAGE: No Persons found!") //Nobody in the database
            }
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to retrieve persons!") //Failed to get Persons
        }
    }

    @GetMapping("/person/{id}")
    fun getPersonById(
        @PathVariable id: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "1") size: Int
    ): ResponseEntity<Any> {
        val pageable: Pageable = PageRequest.of(page, size)
        try {
            val page = personService.getPersonById(id, pageable) //Try get Person by their ID

            if (!page.isEmpty) {
                return ResponseEntity.ok(page) //Successfully found the Person and return them
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SERVER MESSAGE: Person with ID $id not found!") //Person doesn't exist with that ID
            }
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to retrieve person with ID $id!") //Failed to get Person
        }
    }


    //UPDATE ENDPOINT (admin)
    @PutMapping("/person/{id}")
    fun updatePerson(@PathVariable id: Long, @RequestBody updatedPerson: Person): ResponseEntity<String> {
        try {
            val updated = personService.updatePerson(id, updatedPerson) //Update the Person using their ID to find them
            if (updated != null) {
                return ResponseEntity.ok("SERVER MESSAGE: Person with id $id updated successfully!") //Successfully updated the Person
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SERVER ERROR: Person with id $id not found!") //Person doesn't exist with that ID
            }
        } catch (e : Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to update person with ID $id!") //Failed to update Person
        }

    }

    //DELETE ENDPOINT (admin)
    @DeleteMapping("/person/{id}")
    fun deletePerson(@PathVariable id: Long): ResponseEntity<Any> {
        try {
            personService.deletePerson(id) //Try delete the Person by locating them with their ID
            return ResponseEntity.ok("SERVER MESSAGE: Person with ID $id deleted successfully") //Successful deletion
        }
        catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to delete person with ID $id!") //Couldn't delete the Person
        }
    }


    //RETURN USER LIST FILTERED BY PARTIAL NAME MATCHES AND AGE (admin + guest)
    @GetMapping("/filteredPersons")
    fun getFilteredPersons(
        @RequestParam(required = false) partialName: String?,
        @RequestParam(required = false) age: Int?
    ): ResponseEntity<Any> {

        val filteredPersons: List<Person> = personService.getFilteredPersons(partialName, age) //Get a List of type Person that match the partial name or exact age

        if (filteredPersons.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to find any matches!") //Couldn't find anyone that matched the terms
        }

        //Remove the username and password by mapping the list of Persons to a new one
        val filteredPeople = filteredPersons.map { person ->
            FilteredPerson(
                id = person.id!!,
                name = person.name,
                surname = person.surname,
                email = person.email,
                phone = person.phone,
                dateOfBirth = person.dateOfBirth,
                age = person.age
            )
        }

        //Return the new list that doesn't contain username and password information
        return ResponseEntity.ok(filteredPeople)

    }



}
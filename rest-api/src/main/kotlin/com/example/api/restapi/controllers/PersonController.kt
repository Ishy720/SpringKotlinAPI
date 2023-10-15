//Packages
package com.example.api.restapi.controllers

//Imports
import com.example.api.restapi.services.PersonService
import com.example.api.restapi.models.*
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


@RestController
class PersonController(

    private val personService: PersonService

) {

    //CREATE ENDPOINT (admin)
    @PostMapping("/person")
    fun createPerson(@RequestBody payload: Person): ResponseEntity<Any> {
        try {
            val createdPerson = personService.createPerson(payload)
            return ResponseEntity.ok(createdPerson)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to create the new Person!")
        }
    }

    //RETRIEVE ENDPOINTS (GET ALL, GET ONE USER) (admin)
    @GetMapping("/person")
    fun getAllPersons(): ResponseEntity<Any> {
         try {
            val persons = personService.getAllPersons()
            if (persons.isNotEmpty()) {
                return ResponseEntity.ok(persons)
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SERVER MESSAGE: No Persons found!")
            }
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to retrieve persons!")
        }
    }

    @GetMapping("/person/{id}")
    fun getPersonById(@PathVariable id: Long): ResponseEntity<Any> {
         try {
            val person = personService.getPersonById(id)
            if (person != null) {
                return ResponseEntity.ok(person)
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SERVER MESSAGE: Person with ID $id not found!")
            }
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to retrieve person with ID $id!")
        }
    }


    //UPDATE ENDPOINT (admin)
    @PutMapping("/person/{id}")
    fun updatePerson(@PathVariable id: Long, @RequestBody updatedPerson: Person): ResponseEntity<String> {
        try {
            val updated = personService.updatePerson(id, updatedPerson)
            if (updated != null) {
                return ResponseEntity.ok("SERVER MESSAGE: Person with id $id updated successfully!")
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SERVER ERROR: Person with id $id not found!")
            }
        } catch (e : Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to update person with ID $id!")
        }

    }

    //DELETE ENDPOINT (admin)
    @DeleteMapping("/person/{id}")
    fun deletePerson(@PathVariable id: Long): ResponseEntity<Any> {
        try {
            personService.deletePerson(id)
            return ResponseEntity.ok("SERVER MESSAGE: Person with ID $id deleted successfully")
        }
        catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to delete person with ID $id!")
        }
    }


    //RETURN USER LIST FILTERED BY PARTIAL NAME MATCHES AND AGE (admin + guest)
    @GetMapping("/filteredPersons")
    fun getFilteredPersons(
        @RequestParam(required = false) partialName: String?,
        @RequestParam(required = false) age: Int?
    ): ResponseEntity<Any> {
        //return personService.getFilteredPersons(partialName, age)
        val filteredPersons: List<Person> = personService.getFilteredPersons(partialName, age)

        if (filteredPersons.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR: Failed to find any matches!")
        }

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

        return ResponseEntity.ok(filteredPeople)

    }



}
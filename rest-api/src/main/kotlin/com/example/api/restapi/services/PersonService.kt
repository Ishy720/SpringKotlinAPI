//Packages
package com.example.api.restapi.services

//Imports
import com.example.api.restapi.repositories.PersonRepository
import org.springframework.stereotype.Service
import com.example.api.restapi.exceptions.*
import com.example.api.restapi.models.Person
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Service
class PersonService(private val personRepository: PersonRepository) {

    //Creating a Person
    fun createPerson(person: Person): Person {

        //Attempt to save the Person to the repo
        try {
            return personRepository.save(person)
        }
        //Handle exception if unsuccessful
        catch (e : Exception) {
            throw PersonNotCreatedException("ERROR: Could not save person!")
        }

    }

    //Return all Persons
    fun getAllPersons(pageable: Pageable): Page<Person> {
        return personRepository.findAll(pageable)
    }

    //Return Person by id
    fun getPersonById(id: Long, pageable: Pageable): Page<Person> {
        return personRepository.findById(id, pageable)
    }


    //Update Person by id
    fun updatePerson(id: Long, updatedPerson: Person): Person? {
        return personRepository.findById(id).map { existingPerson ->
            val updated = existingPerson.copy(
                name = updatedPerson.name,
                surname = updatedPerson.surname,
                email = updatedPerson.email,
                phone = updatedPerson.phone,
                dateOfBirth = updatedPerson.dateOfBirth,
                age = updatedPerson.age,
                username = updatedPerson.username,
                password = updatedPerson.password
            )
            personRepository.save(updated)
        }.orElse(null)
    }

    //Delete Person by id
    fun deletePerson(id: Long) {
        try {
            if (personRepository.existsById(id)) {
                personRepository.deleteById(id)
            } else {
                throw PersonNotRetrievedException("ERROR: Person with ID $id not found")
            }
        } catch (e: Exception) {
            throw PersonNotDeletedException("ERROR: Could not delete person with ID $id")
        }
    }

    //Get a filtered result of Persons based on partial name match or exact age match
    fun getFilteredPersons(partialName: String?, age: Int?): List<Person> {
        var filteredPersons = personRepository.findAll()

        //Filter by partial name if provided
        if (partialName != null) {
            filteredPersons = filteredPersons.filter {
                it.name.contains(partialName, ignoreCase = true) || it.surname.contains(partialName, ignoreCase = true) //Check both names using contains method
            }
        }

        //Filter by age if provided
        if (age != null) {
            filteredPersons = filteredPersons.filter { it.age == age } //Get only the Persons that match the age
        }

        //Return the collective results
        return filteredPersons
    }



}
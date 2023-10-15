//Packages
package com.example.api.restapi.services

//Imports
import com.example.api.restapi.repositories.PersonRepository
import org.springframework.stereotype.Service
import com.example.api.restapi.exceptions.*
import com.example.api.restapi.models.Person

@Service
class PersonService(private val personRepository: PersonRepository) {

    //Creating a Person
    fun createPerson(person: Person): Person {

        //Attempt to save the Person to the repo
        try {
            return personRepository.save(person)
        }
        //Handle exceptions if unsuccessful
        catch (e : Exception) {
            throw PersonNotCreatedException("ERROR: Could not save person!")
        }

    }

    //Return all Persons
    fun getAllPersons(): List<Person> = personRepository.findAll()

    //Return Person by id
    fun getPersonById(id: Long): Person? {
        return personRepository.findById(id).orElse(null)
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

    fun getFilteredPersons(partialName: String?, age: Int?): List<Person> {
        var filteredPersons = getAllPersons()

        //filter by partial name
        if (partialName != null) {
            filteredPersons = filteredPersons.filter {
                it.name.contains(partialName, ignoreCase = true) || it.surname.contains(partialName, ignoreCase = true)
            }
        }

        //filter by age
        if (age != null) {
            filteredPersons = filteredPersons.filter { it.age == age }
        }

        return filteredPersons
    }


}
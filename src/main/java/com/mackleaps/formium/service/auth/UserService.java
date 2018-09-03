package com.mackleaps.formium.service.auth;

import com.mackleaps.formium.model.auth.Person;

/**
 * This interface exists taking a different approach from AuthService
 * 
 * Here all objects are create on the service layer, so that errors can be 
 * transmitted throught their interface contracts
 * 
 * And invalid attributes are treated by throwing RuntimeExceptions
 * For it is expected from the controllers that the information given is right on a individual field level
 * Except from field with duplicated information
 * */
public interface UserService {

	/**
	 * return Person registered with given email
	 * in case the given email is not registered, return null
	 * */
	Person loadPersonByEmailAddress(String email);
	
	/**
	 * returns a Person with the updated information if update goes ok
	 * else, returns null
	 * */
	Person updatePersonData (String email, String name, String lastName);
}

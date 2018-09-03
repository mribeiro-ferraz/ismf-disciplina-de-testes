package com.mackleaps.formium.service.auth;

import com.mackleaps.formium.model.auth.Person;
import com.mackleaps.formium.repository.auth.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

	private PersonRepository repo;
	
	@Autowired
	public UserServiceImp(PersonRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public Person loadPersonByEmailAddress(String email) {
		return repo.findByUserEmailAddress(email);
	}

	@Override
	public Person updatePersonData(String email, String name, String lastName) {
		
		Person person = repo.findByUserEmailAddress(email);
		if(person == null)
			throw new UsernameNotFoundException("Usuário com o email informado não foi encontrado.");
		
		person.setFirstName(name);
		person.setLastName(lastName);
		return repo.saveAndFlush(person);
		
	}

}

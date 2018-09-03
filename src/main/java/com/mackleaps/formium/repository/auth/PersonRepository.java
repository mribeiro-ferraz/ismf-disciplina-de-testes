package com.mackleaps.formium.repository.auth;

import com.mackleaps.formium.model.auth.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByCpf(String cpf);
    Person findByUserEmailAddress(String email);
    boolean existsByCpf(String cpf);
    boolean existsByUserEmailAddress (String email);

}

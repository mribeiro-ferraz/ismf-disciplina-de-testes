package com.mackleaps.formium.repository.auth;

import com.mackleaps.formium.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT U FROM User U WHERE U.emailAddress = :emailAddress")
    User findByEmailAddress(@Param("emailAddress") String emailAddress);
	
}

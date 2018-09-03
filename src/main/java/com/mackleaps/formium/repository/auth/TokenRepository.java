package com.mackleaps.formium.repository.auth;

import com.mackleaps.formium.model.auth.User;
import com.mackleaps.formium.model.auth.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

}

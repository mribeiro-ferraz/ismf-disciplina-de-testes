package com.mackleaps.formium.model.auth;

import com.mackleaps.formium.exceptions.InvalidTokenException;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "token")
public class VerificationToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expiryDate;
    private boolean used;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id_user")
    private User user;

    public VerificationToken() {
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public VerificationToken(User user, int expirationInMinutes) {
        this.token = UUID.randomUUID().toString();
        this.user = user;
        this.used = false;
        expiryDate = calculateExpiryDate(expirationInMinutes);
    }

    public boolean isExpired(){
        Calendar currentInstant = Calendar.getInstance();
        return(this.getExpiryDate().getTime() - currentInstant.getTime().getTime() <= 0);
    }

    public boolean isValid() {
        return isExpired() || used;
    }

    public void useToken() throws InvalidTokenException {

        if(isValid()) {
            this.getUser().setEnabled(true);
            this.used = true;
        }
        throw new InvalidTokenException();
    }

    public Long getId() {
        return this.id;
    }

    public String getToken() {
        return this.token;
    }

    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public boolean isUsed() {
        return this.used;
    }

    public User getUser() {
        return this.user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

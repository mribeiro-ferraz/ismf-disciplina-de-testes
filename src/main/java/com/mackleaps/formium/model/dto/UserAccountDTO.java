package com.mackleaps.formium.model.dto;

import com.mackleaps.formium.annotations.ValidCPF;
import com.mackleaps.formium.annotations.ValidEmail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class UserAccountDTO {
	
    @NotNull
    @Size(min = 1)
    private String firstName;

    @NotNull
    @Size(min = 1)
    private String lastName;

    @NotNull
    @Size(min = 11, max = 11)
    @ValidCPF(message = "CPF é inválido")
    private String cpf;

    @NotNull
    @Size(min = 5)
    @ValidEmail(message = "Há algo de errado com seu endereço de e-mail")
    private String email;
    
    @NotNull
    private LocalDateTime register;

    @java.beans.ConstructorProperties({"firstName", "lastName", "cpf", "email", "register"})
    public UserAccountDTO(String firstName, String lastName, String cpf, String email, LocalDateTime register) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cpf = cpf;
        this.email = email;
        this.register = register;
    }

    public UserAccountDTO() {
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getEmail() {
        return this.email;
    }

    public LocalDateTime getRegister() {
        return this.register;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRegister(LocalDateTime register) {
        this.register = register;
    }
}

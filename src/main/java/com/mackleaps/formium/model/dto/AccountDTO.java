package com.mackleaps.formium.model.dto;


import com.mackleaps.formium.annotations.ValidCPF;
import com.mackleaps.formium.annotations.ValidEmail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountDTO {

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
    @Size(min = 1)
    private String password;

    @java.beans.ConstructorProperties({"firstName", "lastName", "cpf", "email", "password"})
    public AccountDTO(String firstName, String lastName, String cpf, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
    }

    public AccountDTO() {
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

    public String getPassword() {
        return this.password;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
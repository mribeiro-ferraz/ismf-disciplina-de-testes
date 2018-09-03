package com.mackleaps.formium.model.auth;

import com.mackleaps.formium.exceptions.CPFFormatException;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, length = 11)
    private String cpf;

	@Column(nullable = false)
    private LocalDateTime register;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;

    public Person(String firstName, String lastName, String cpf, User user) {

        Validate.notNull(firstName, "firstName can't be null");
        Validate.notNull(lastName, "lastName can't be null");
        Validate.notNull(cpf, "cpf can't be null");

        if(!isValidCPF(cpf))
        	throw new CPFFormatException("CPF é inválido.");

        this.firstName = firstName;
        this.lastName = lastName;
        this.cpf = cpf;
        this.register = LocalDateTime.now(); //TODO remove this
        this.user = user;
    }

    public Person() {
    }

    /**
     * @param candidateCpf A candidate CPF for verification
     * @return is this candidate CPF valid or not
     */
    public static boolean isValidCPF(String candidateCpf) {

        final String cpf = candidateCpf.replaceAll("\\s", "");

        if (!cpf.matches("^[0-9]*$") || cpf.length() != 11) {
        	return false;
        }

        final List<String> INVALID_ARRAY = Arrays.asList
                ("11111111111", "22222222222", "33333333333",
                        "44444444444", "55555555555", "66666666666",
                        "77777777777", "88888888888", "99999999999",
                        "00000000000");

        if (INVALID_ARRAY.stream().anyMatch(e -> e.equals(cpf))) {
            return false;
        }

        final List<String> serializedCpf = Arrays.asList(cpf.split(""));

        AtomicInteger firstOrdinal = new AtomicInteger(10);

        int sum;
        sum = serializedCpf.stream()
                .limit(9).mapToInt(Integer::parseInt)
                .map(i -> i * firstOrdinal.getAndDecrement())
                .sum();

        if (!compare(sum, serializedCpf, 10)) {
            return false;
        }

        AtomicInteger secondOrdinal = new AtomicInteger(11);

        sum = serializedCpf.stream()
                .limit(10).mapToInt(Integer::parseInt)
                .map(i -> i * secondOrdinal.getAndDecrement())
                .sum();

        return (compare(sum, serializedCpf, 11));

    }

    private static boolean compare(int value, List<String> cpf, int position) {
        position = position - 1;

        int toCompare = Integer.parseInt(cpf.get(position));
        int compare = 11 - (value % 11);

        if (compare == toCompare)
            return true;

        return (compare == 10 || compare == 11 && 0 == toCompare);
    }

    public void setCpf(String cpf) {
    	
    	if(!Person.isValidCPF(cpf))
    		throw new CPFFormatException("Invalid CPF");
    	else
    		this.cpf = cpf;
    }

    public Long getId() {
        return this.id;
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

    public LocalDateTime getRegister() {
        return this.register;
    }

    public User getUser() {
        return this.user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRegister(LocalDateTime register) {
        this.register = register;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

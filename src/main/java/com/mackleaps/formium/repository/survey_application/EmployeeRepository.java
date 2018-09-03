package com.mackleaps.formium.repository.survey_application;

import com.mackleaps.formium.model.survey_application.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByCpf(String cpf);
    Employee findByUserEmailAddress(String email);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM Employee e WHERE e.cpf = :cpf")
    boolean existsByCpf(@Param("cpf") String cpf);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM Employee e WHERE e.user.emailAddress = :email")
    boolean existsByEmail(@Param("email") String email);

    Employee findEmployeeByUserId(Long id);
    boolean existsEmployeeByUserId(Long id);
}

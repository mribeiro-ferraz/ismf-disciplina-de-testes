package com.mackleaps.formium.repository.survey;

import com.mackleaps.formium.model.survey.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContainerRepository extends JpaRepository<Container, Long> {

}

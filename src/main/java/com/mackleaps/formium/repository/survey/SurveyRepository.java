package com.mackleaps.formium.repository.survey;

import com.mackleaps.formium.model.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey,Long> {

}

package com.mackleaps.formium.repository.survey_application;

import com.mackleaps.formium.model.survey_application.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {

    @Query("SELECT A " +
           "FROM Answer as A " +
           "WHERE A.correspondingResults.id = :resultsId AND " +
                 "A.correspondingQuestion.parent.id = :questionParentId")
    List<Answer> findAnswersByCorrespondingResultsIdAndCorrespondingQuestionParentId(@Param("resultsId") Long correspondingResultsId,
                                                                                     @Param("questionParentId")  Long correspondingParentId);

}

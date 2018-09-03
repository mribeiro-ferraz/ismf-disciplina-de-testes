package com.mackleaps.formium.repository.survey;

import com.mackleaps.formium.model.survey.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    @Query("SELECT CASE " +
            "WHEN COUNT(q) > 0 " +
                "THEN TRUE " +
            "ELSE " +
                "FALSE " +
            "END " +
            "FROM Question q " +
            "WHERE TYPE(q) IN :classType AND q.id = :id")
    <T extends Question> boolean existsByIdAndType (@Param("id") Long id, @Param("classType") Class<T> ... classType);

    @Query("SELECT q " +
            "FROM Question q " +
            "WHERE q.id = :id AND TYPE(q) IN :classType" +
            "")
    <T extends Question> T findByIdAndType(@Param("id") Long id, @Param("classType") Class<T> ... classType);

}

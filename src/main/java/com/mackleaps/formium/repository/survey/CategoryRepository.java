package com.mackleaps.formium.repository.survey;

import com.mackleaps.formium.model.survey.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository <Category, Long> {

}

package com.endava.endavibe.question.questionConfig;

import com.endava.endavibe.question.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionConfigRepository extends JpaRepository<QuestionConfigEntity, Long> {

    List<QuestionConfigEntity> findByQuestion(QuestionEntity question);
}

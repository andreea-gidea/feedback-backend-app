
package com.endava.endavibe.question;

import com.endava.endavibe.question.questionCategory.QuestionCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

    Optional<QuestionEntity> findByUuid(UUID uuid);

    @Query(value = " select q from QuestionEntity as q, QuestionQuizEntity as qq where q.id = qq.question.id and qq.quiz.uuid = ?1")
    List<QuestionEntity> findQuestionsByQuizId(UUID quizUuid);

}

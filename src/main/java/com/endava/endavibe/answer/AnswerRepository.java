
package com.endava.endavibe.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

    @Query(value = " select a from AnswerEntity as a, QuestionConfigEntity as qc where a.id = qc.answer.id and qc.question.uuid = ?1" )
    List<AnswerEntity> findAnswersByQuestionId (UUID questionUuid);
    Optional<AnswerEntity> findByUuid(UUID answerUuid);
}

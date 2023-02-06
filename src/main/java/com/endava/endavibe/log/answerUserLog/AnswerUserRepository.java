package com.endava.endavibe.log.answerUserLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerUserRepository extends JpaRepository<AnswerUserEntity, Long> {

    List<AnswerUserEntity> findByQuizIdAndAppUserIdAndProjectId(Long quizId, Long userId, Long projectId);
}

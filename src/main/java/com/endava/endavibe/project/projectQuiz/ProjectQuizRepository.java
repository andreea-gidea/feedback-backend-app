package com.endava.endavibe.project.projectQuiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import java.util.UUID;

@Repository
public interface ProjectQuizRepository extends JpaRepository<ProjectQuizEntity, Long> {

    Optional<ProjectQuizEntity> findByUuid(UUID uuid);

    List<ProjectQuizEntity> findAllByIsSent(boolean isSent);
}

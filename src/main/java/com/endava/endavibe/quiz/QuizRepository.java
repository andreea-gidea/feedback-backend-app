package com.endava.endavibe.quiz;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Long> {
    Optional<QuizEntity> findByUuid(UUID uuid);

}

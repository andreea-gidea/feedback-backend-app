package com.endava.endavibe.question.questionCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategoryEntity, Long> {

    Optional<QuestionCategoryEntity> findByUuid(UUID uuid);
}

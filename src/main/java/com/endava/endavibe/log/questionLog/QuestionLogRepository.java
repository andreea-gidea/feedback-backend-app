package com.endava.endavibe.log.questionLog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionLogRepository extends CrudRepository<QuestionLogEntity, Long> {
    @Procedure(value= " public.question_log_by_project_init")
    Boolean questionLogByProjectInit(@Param("project_id_param") Long projectId);

    @Procedure(value= " public.question_log_insert")
    Boolean questionLogInsert(@Param("project_id_para") Long projectId,
                              @Param("question_id_param") Long questionId,
                              @Param("question_category_id_param") Long categoryId);

    List<QuestionLogEntity> getAllByProjectId(Long projectId);

    @Query(nativeQuery = true, value = "SELECT * FROM public.random_question_by_proj_and_cat(:projectId, :categoryId);")
    QuestionLogEntity getRandomQuestionByProjectAndCategory(Long projectId, Long categoryId);
}

package com.endava.endavibe.log.answerLog;

import com.endava.endavibe.common.dto.stats.AnswerStatDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AnswerLogRepository extends JpaRepository<AnswerLogEntity, Long> {
    /***
     * @return all logs grouped as stats
     */
    @Query("SELECT new com.endava.endavibe.common.dto.stats.AnswerStatDto(ale.projectId, ale.questionCategoryId, " +
            "AVG(ale.answerValue), COUNT(ale.answerValue), MIN(ale.insDate), MAX(ale.insDate)) " +
            "FROM AnswerLogEntity ale " +
            "GROUP BY ale.projectId, ale.questionCategoryId")
    List<AnswerStatDto> findAllAnswersGroupByProjectAndQuestionCategory();

    /***
     * @param startDate is the starting point of processing stats from Answers logs
     * @return stats from a given date to the most recent date based on logs
     */
    @Query("SELECT new com.endava.endavibe.common.dto.stats.AnswerStatDto(ale.projectId, ale.questionCategoryId, " +
            "AVG(ale.answerValue), COUNT(ale.answerValue), MIN(ale.insDate), MAX(ale.insDate)) " +
            "FROM AnswerLogEntity ale " +
            "WHERE ale.insDate > :startDate " +
            "GROUP BY ale.projectId, ale.questionCategoryId")
    List<AnswerStatDto> findAllAnswersGroupByProjectAndQuestionCategoryAfter(LocalDateTime startDate);

    /***
     * @param startDate is the starting point of processing stats from Answers logs
     * @param endDate is the end point of processing stats from Answers logs
     * @return stats from a given interval
     */
    @Query("SELECT new com.endava.endavibe.common.dto.stats.AnswerStatDto(ale.projectId, ale.questionCategoryId, " +
            "AVG(ale.answerValue), COUNT(ale.answerValue), MIN(ale.insDate), MAX(ale.insDate)) " +
            "FROM AnswerLogEntity ale " +
            "WHERE ale.insDate > :startDate AND ale.insDate < :endDate " +
            "GROUP BY ale.projectId, ale.questionCategoryId")
    List<AnswerStatDto> findAllAnswersGroupByProjectAndQuestionCategoryBetween(LocalDateTime startDate,
                                                                               LocalDate endDate);
}

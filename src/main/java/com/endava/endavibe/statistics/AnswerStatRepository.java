package com.endava.endavibe.statistics;

import com.endava.endavibe.reporting.WeeklyReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/* Some queries must be native, because 'Sub-queries may be used in the WHERE or HAVING clause.' only (JPQL docs) */
@Repository
public interface AnswerStatRepository extends JpaRepository<AnswerStatEntity, Long> {
    @Query("SELECT max(ase.toDate) FROM AnswerStatEntity ase")
    LocalDateTime getLastStatDate();

    /***
     * Any AnswerStatEntity that was not analyzed has the member isVerified false.
     * @return a list of last stats calculated
     */
    @Query(nativeQuery = true, value =
            "SELECT * FROM public.answer_stat as ans " +
                    "INNER JOIN " +
                    "    (SELECT project_id, question_category_id, MAX(to_date) as to_date " +
                    "        FROM public.answer_stat " +
                    "        WHERE is_verified = false " +
                    "        GROUP BY project_id, question_category_id) AS grouped_ans " +
                    "ON (" +
                    "    ans.to_date = grouped_ans.to_date AND " +
                    "    ans.project_id = grouped_ans.project_id AND " +
                    "    ans.question_category_id = grouped_ans.question_category_id)")
    Optional<List<AnswerStatEntity>> getLastUnverifiedAnswerStats();

    /***
     * @return a list of next to last stats calculated
     */
    @Query(nativeQuery = true, value =
            "SELECT * FROM public.answer_stat as ans " +
                    "INNER JOIN " +
                    "    (SELECT project_id, question_category_id, MAX(to_date) as to_date " +
                    "        FROM public.answer_stat " +
                    "        WHERE is_verified = true " +
                    "        GROUP BY project_id, question_category_id) AS grouped_ans " +
                    "ON ( " +
                    "    ans.to_date = grouped_ans.to_date AND " +
                    "    ans.project_id = grouped_ans.project_id AND " +
                    "    ans.question_category_id = grouped_ans.question_category_id) ")
    Optional<List<AnswerStatEntity>> getNextToLastAnswerStat();

    @Query(nativeQuery = true, value =
            "SELECT AVG(avg_value) FROM public.answer_stat as ans " +
                    "INNER JOIN " +
                    "    (SELECT project_id, question_category_id, MAX(to_date) as to_date " +
                    "        FROM public.answer_stat " +
                    "        WHERE is_verified = false AND project_id= :projectId " +
                    "        GROUP BY project_id, question_category_id) AS grouped_ans " +
                    "ON ( " +
                    "    ans.to_date = grouped_ans.to_date AND " +
                    "    ans.project_id = grouped_ans.project_id AND " +
                    "    ans.question_category_id = grouped_ans.question_category_id) ")
    Double getLastUnverifiedStatsProjectAvgValue(Long projectId);

    @Query(nativeQuery = true, value =
            "SELECT AVG(avg_value) FROM public.answer_stat as ans " +
                    "INNER JOIN " +
                    "    (SELECT project_id, question_category_id, MAX(to_date) as to_date " +
                    "        FROM public.answer_stat " +
                    "        WHERE is_verified = true AND project_id= :projectId " +
                    "        GROUP BY project_id, question_category_id) AS grouped_ans " +
                    "ON ( " +
                    "    ans.to_date = grouped_ans.to_date AND " +
                    "    ans.project_id = grouped_ans.project_id AND " +
                    "    ans.question_category_id = grouped_ans.question_category_id) ")
    Double getLastVerifiedStatsProjectAvgValue(Long projectId);

    @Query("SELECT ase FROM AnswerStatEntity ase where ase.isVerifiedByWeekly=:isVerifiedByWeekly")
    List<AnswerStatEntity> getAnswerStatEntitiesByIsVerifiedByWeekly(Boolean isVerifiedByWeekly);

    List<AnswerStatEntity> getAnswerStatEntitiesByWeeklyReport(WeeklyReportEntity weeklyReportEntity);


    @Query("SELECT ase FROM AnswerStatEntity ase WHERE ase.projectId=:projectId AND ase.weekAndYearOfStat BETWEEN :dateMin and :dateMax")
    List<AnswerStatEntity> findAllBetweenWeekYear(Long projectId, String dateMin, String dateMax);


}

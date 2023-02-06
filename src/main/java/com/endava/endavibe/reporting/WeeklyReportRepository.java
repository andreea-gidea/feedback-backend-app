package com.endava.endavibe.reporting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WeeklyReportRepository extends JpaRepository<WeeklyReportEntity, Long> {


    @Query(nativeQuery = true, value ="SELECT * FROM weekly_report " +
            "INNER JOIN  " +
            "(SELECT DISTINCT week_year_stat FROM weekly_report ORDER BY week_year_stat DESC FETCH FIRST 4 ROWS ONLY) " +
            "AS criteria ON weekly_report.week_year_stat = criteria.week_year_stat ORDER BY weekly_report.week_year_stat DESC;")
    List<WeeklyReportEntity> findLastWeeklyReports();

    @Query(nativeQuery = true, value =
            " SELECT * FROM weekly_report  " +
                    " JOIN " +
                    " (SELECT DISTINCT week_year_stat FROM " +
                    " weekly_report ORDER BY week_year_stat DESC FETCH FIRST 4 ROWS ONLY) AS criteria " +
                    " ON weekly_report.week_year_stat = criteria.week_year_stat " +
                    " JOIN " +
                    " (SELECT DISTINCT project_id FROM weekly_report " +
                    " INNER JOIN (SELECT project.id as user_projs_id FROM public.project " +
                    " INNER JOIN public.project_app_user ON project_app_user.project_id = project.id " +
                    " WHERE project_app_user.app_user_id = :id) as user_projs " +
                    " ON weekly_report.project_id = user_projs.user_projs_id) AS distinct_user_projs " +
                    " ON weekly_report.project_id = distinct_user_projs.project_id " +
                    " ORDER BY weekly_report.week_year_stat DESC; ")
    List<WeeklyReportEntity> findLastWeeklyReportsByAppUser(Long id);

    Optional<WeeklyReportEntity> findByUuid(UUID uuid);


}

package com.endava.endavibe.reporting;

import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.appUser.role.RoleService;
import com.endava.endavibe.common.dto.reporting.*;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.question.questionCategory.QuestionCategoryEntity;
import com.endava.endavibe.question.questionCategory.QuestionCategoryRepository;
import com.endava.endavibe.statistics.AnswerStatEntity;
import com.endava.endavibe.statistics.AnswerStatRepository;
import com.endava.endavibe.statistics.AnswerStatService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class WeeklyReportService {

    Logger logger = LoggerFactory.getLogger(AnswerStatService.class);

    private final AnswerStatRepository answerStatRepository;
    private final ProjectRepository projectRepository;
    private final WeeklyReportRepository weeklyReportRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final RoleService roleService;


    private final ModelMapper modelMapper;

    public void saveWeeklyReports() {
        /* Check if there are any answer stats in the database */
        long countAnswerStats = answerStatRepository.count();

        if (countAnswerStats == 0) {
            logger.info("There are no stats to work with it");
            return;
        }

        /* Get list of stats not put in weeklyStats*/
        List<AnswerStatEntity> answerStatsToCheck = answerStatRepository.getAnswerStatEntitiesByIsVerifiedByWeekly(Boolean.FALSE);

        if (ObjectUtils.isEmpty(answerStatsToCheck)) {
            logger.info("There are no stats that are not verified");
            return;
        }

        Map<AnswerStatEntity.ProjectWeekYear, List<AnswerStatEntity>> statsGrouped = answerStatsToCheck.stream()
                .collect(groupingBy(answerStat -> new AnswerStatEntity.ProjectWeekYear(answerStat.getProjectId(), answerStat.getWeekAndYearOfStat())));

        statsGrouped.forEach((key, value) -> {

            WeeklyReportEntity weeklyReport = createAndSaveWeeklyReport(key, value);

            saveLinkAnswerStatToWeeklyReport(value, weeklyReport);
        });
    }

    private void saveLinkAnswerStatToWeeklyReport(List<AnswerStatEntity> answerStats, WeeklyReportEntity weeklyReport) {

        for (AnswerStatEntity aStat : answerStats) {

            aStat.setWeeklyReport(weeklyReport);
            aStat.setIsVerifiedByWeekly(Boolean.TRUE);
            answerStatRepository.save(aStat);

        }
    }

    private WeeklyReportEntity createAndSaveWeeklyReport(AnswerStatEntity.ProjectWeekYear key, List<AnswerStatEntity> value) {

        return weeklyReportRepository.save(WeeklyReportEntity
                .builder()
                .projectId(key.projectId())
                .nrOfTeamMembers((long) projectRepository.findById(key.projectId()).orElseThrow(() -> {
                            throw new BusinessException("No project with Id " + key.projectId());
                        }).getProjectAppUserList()
                        .stream()
                        .filter(projectUser -> roleService.getTeamMember().equals(projectUser.getRole()))
                        .toList()
                        .size())
                .nrOfRespondents(Long.valueOf(value.stream()
                        .map(answerStat -> answerStat.getNrOfRespondents().intValue()).reduce(Integer::max).orElse(0)))
                .overallScore(value.stream()
                        .map(AnswerStatEntity::getAvgValue)
                        .reduce(0.0, Double::sum) / (long) value.size())
                .weekAndYearOfStat(key.weekAndYearOfStat())
                .build());
    }

    private List<ProjectInfoOverallDto> getProjectInfoOverallDtos(Map<WeeklyReportEntity.ProjectId, List<WeeklyReportEntity>> weeklyReportsGroupedByProject) {

        List<ProjectInfoOverallDto> projectInfos = new ArrayList<>();

        weeklyReportsGroupedByProject.forEach((project, weeklyReportEntities) -> {

            ProjectInfoOverallDto projectInfo = new ProjectInfoOverallDto();

            ProjectEntity projectEntity = projectRepository.findById(project.projectId()).orElseThrow(() -> {
                throw new BusinessException("No quiz with Id " + project.projectId());
            });
            projectInfo.setProjectName(projectEntity.getName());
            projectInfo.setProjectUuid(projectEntity.getUuid());

            projectInfo.setLastReports(getWeeklyReportDtos(weeklyReportEntities));

            projectInfos.add(projectInfo);
        });
        return projectInfos;
    }

    private List<WeeklyReportDto> getWeeklyReportDtos(List<WeeklyReportEntity> weeklyReportEntities) {
        List<WeeklyReportDto> projectReports = new ArrayList<>();

        for (WeeklyReportEntity weeklyReport : weeklyReportEntities) {
            WeeklyReportDto reportDto = modelMapper.map(weeklyReport, WeeklyReportDto.class);
            projectReports.add(reportDto);
        }
        return projectReports;
    }

    public ProjectWeeklyReportDto getDetailedWeeklyReports(UUID weeklyReportDtoUuid) {

        List<AnswerStatEntity> answerStatEntities =
                answerStatRepository.getAnswerStatEntitiesByWeeklyReport(weeklyReportRepository.findByUuid(weeklyReportDtoUuid)
                        .orElseThrow(() -> {
                            throw new BusinessException("No weekly report for Uuid " + weeklyReportDtoUuid);
                        }));
        ProjectWeeklyReportDto projectReport = new ProjectWeeklyReportDto();
        projectReport.setReportsRelated(getReportingDtosFromEntities(answerStatEntities));

        return setProjectDetails(projectReport, weeklyReportDtoUuid);

    }

    private List<ReportingAnswerStatDto> getReportingDtosFromEntities(List<AnswerStatEntity> answerStatEntities) {

        List<ReportingAnswerStatDto> reportingAnswerStatDtos = new ArrayList<>();

        for (AnswerStatEntity answerStat : answerStatEntities) {

            ReportingAnswerStatDto reportingDto = modelMapper.map(answerStat, ReportingAnswerStatDto.class);

            reportingDto.setQuestionCategoryTitle(questionCategoryRepository.findById(answerStat.getQuestionCategoryId()).orElseThrow(() -> {
                throw new BusinessException("No question category for id " + answerStat.getQuestionCategoryId());
            }).getTitle());
            reportingAnswerStatDtos.add(reportingDto);

        }
        return reportingAnswerStatDtos;
    }
    private List<ReportingAnswerStatForGraphDto> getReportingForGraphDtosFromEntities(List<AnswerStatEntity> answerStatEntities) {

        List<ReportingAnswerStatForGraphDto> reportingAnswerStatForGraphDtos = new ArrayList<>();

        for (AnswerStatEntity answerStat : answerStatEntities) {

            ReportingAnswerStatForGraphDto reportingForGraphDto = modelMapper.map(answerStat, ReportingAnswerStatForGraphDto.class);

            reportingForGraphDto.setQuestionCategoryTitle(questionCategoryRepository.findById(answerStat.getQuestionCategoryId()).orElseThrow(() -> {
                throw new BusinessException("No question category for id " + answerStat.getQuestionCategoryId());
            }).getTitle());
            reportingForGraphDto.setNrOfProjectUsers(answerStat.getWeeklyReport().getNrOfTeamMembers());
            reportingAnswerStatForGraphDtos.add(reportingForGraphDto);

        }
        return reportingAnswerStatForGraphDtos;
    }

    private ProjectWeeklyReportDto setProjectDetails(ProjectWeeklyReportDto projectWeeklyReport, UUID weeklyReportDtoUuid) {

        WeeklyReportEntity weeklyReport = weeklyReportRepository.findByUuid(weeklyReportDtoUuid).orElseThrow(() -> {
            throw new BusinessException("No weekly report for Uuid " + weeklyReportDtoUuid);
        });

        projectWeeklyReport.setNrOfProjectUsers(weeklyReport.getNrOfTeamMembers());
        projectWeeklyReport.setWeekAndYearOfStat(weeklyReport.getWeekAndYearOfStat());

        projectWeeklyReport.setProjectName(projectRepository.findById(weeklyReport.getProjectId()).orElseThrow(() -> {
            throw new BusinessException("No weekly project for ID " + weeklyReport.getProjectId());
        }).getName());
        projectWeeklyReport.setProjectUuid(projectRepository.findById(weeklyReport.getProjectId()).orElseThrow(() -> {
            throw new BusinessException("No weekly project for ID " + weeklyReport.getProjectId());
        }).getUuid());

        return projectWeeklyReport;
    }

    public ProjectInfoGraphDto getReportsForGraph(String projectUuid, String period) {

        ProjectEntity project = projectRepository.findByUuid(UUID.fromString(projectUuid)).orElseThrow(() -> {
            throw new BusinessException("No project for ID " + projectUuid);
        });

        String dateMaxFormatted = formatWeekYearFromDate(LocalDateTime.now());


        LocalDateTime dateMin = switch (period) {
            case "last-month" -> LocalDateTime.now().minusMonths(1);
            case "last-6-months" -> LocalDateTime.now().minusMonths(6);
            case "last-year" -> LocalDateTime.now().minusYears(1);
            default -> throw new BusinessException("No stats available for period of time " + period);
        };

        List<AnswerStatEntity> answerStatEntities = answerStatRepository.findAllBetweenWeekYear(project.getId(), formatWeekYearFromDate(dateMin), dateMaxFormatted);

        ProjectInfoGraphDto projectInfoGraphDto = ProjectInfoGraphDto.builder()
                .projectUuid(project.getUuid())
                .projectName(project.getName())
                .build();

        Map<AnswerStatEntity.Category, List<AnswerStatEntity>> statsGrouped = answerStatEntities.stream()
                .collect(groupingBy(answerStat -> new AnswerStatEntity.Category(answerStat.getQuestionCategoryId())));

        List<ScoresByCategoryDto> scoresByCategoryDtos = new ArrayList<>();

        List<String> labels = getYearWeekLabelsForDateMaxAndMin(LocalDateTime.now(), dateMin);

        statsGrouped.forEach((key, value) -> {

            QuestionCategoryEntity questionCategory = questionCategoryRepository.findById(key.questionCategoryId()).orElseThrow(() -> {
                throw new BusinessException("No category for ID " + key.questionCategoryId());
            });

            ScoresByCategoryDto scoreByCategoryDto = ScoresByCategoryDto.builder()
                    .categoryTitle(questionCategory.getTitle())
                    .categoryUuid(questionCategory.getUuid())
                    .build();

            scoreByCategoryDto.setStats(getReportingForGraphDtosFromEntities(value)
                    .stream()
                    .sorted(Comparator.comparing(ReportingAnswerStatForGraphDto::getWeekAndYearOfStat))
                    .collect(Collectors.toList()));

            scoresByCategoryDtos.add(scoreByCategoryDto);

        });

        projectInfoGraphDto.setScoresByCategories(scoresByCategoryDtos);
        projectInfoGraphDto.setLabels(labels.stream().sorted().collect(Collectors.toCollection(TreeSet::new)));

        return projectInfoGraphDto;
    }

    private String formatWeekYearFromDate(LocalDateTime date) {

        long week = date.get(WeekFields.ISO.weekOfWeekBasedYear());
        String formattedWeek = String.format("%02d", week);
        return "Year " + String.valueOf(date.get ( IsoFields.WEEK_BASED_YEAR )).concat(" Week ").concat(formattedWeek);
    }
    private List<String> getYearWeekLabelsForDateMaxAndMin(LocalDateTime dateMax, LocalDateTime dateMin) {

        List<String> yearWeek = new ArrayList<>();

        while (dateMax.isAfter(dateMin)){
            yearWeek.add(formatWeekYearFromDate(dateMax));
            dateMax = dateMax.minusWeeks(1);
        }
        return yearWeek;


    }

    public OverallReportDto getAllWeeklyReportsByCurrentUser() {
        AppUserEntity appUserEntity = (AppUserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<WeeklyReportEntity> lastWeeklyReportEntities = weeklyReportRepository.findLastWeeklyReportsByAppUser(appUserEntity.getId());

        if(CollectionUtils.isEmpty(lastWeeklyReportEntities)){
            return  null;
        }

        OverallReportDto lastFourWeeksReports = new OverallReportDto();


        lastFourWeeksReports.setLabels(getYearWeekLabelsForDateMaxAndMin(LocalDateTime.now(),LocalDateTime.now().minusWeeks(4))
                .stream().sorted().collect(Collectors.toCollection(TreeSet::new)));

        Map<WeeklyReportEntity.ProjectId, List<WeeklyReportEntity>> weeklyReportsGroupedByProject = lastWeeklyReportEntities.stream()
                .collect(groupingBy(weeklyReport -> new WeeklyReportEntity.ProjectId(weeklyReport.getProjectId())));

        lastFourWeeksReports.setProjects(getProjectInfoOverallDtos(weeklyReportsGroupedByProject));

        return lastFourWeeksReports;
    }
}



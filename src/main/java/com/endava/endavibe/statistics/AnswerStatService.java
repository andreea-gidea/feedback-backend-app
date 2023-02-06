package com.endava.endavibe.statistics;

import com.endava.endavibe.appUser.role.RoleService;
import com.endava.endavibe.log.answerLog.AnswerLogRepository;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.AlertProjectCategoryDto;
import com.endava.endavibe.common.dto.stats.AnswerStatDto;
import com.endava.endavibe.common.dto.WrapProjectAlert;
import com.endava.endavibe.common.dto.WrapTemplate;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserRepository;
import com.endava.endavibe.question.questionCategory.QuestionCategoryRepository;
import com.endava.endavibe.mail.MailService;
import com.endava.endavibe.common.util.enums.AlertType;
import com.endava.endavibe.common.util.enums.SatisfactionType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerStatService {

    Logger logger = LoggerFactory.getLogger(AnswerStatService.class);

    @Value("${client.url}")
    private String clientUrl;

    private final QuestionCategoryRepository questionCategoryRepository;
    private final AnswerStatRepository answerStatRepository;
    private final ProjectAppUserRepository projectAppUserRepository;
    private final RoleService roleService;

    private final AnswerLogRepository answerLogRepository;
    private final ProjectRepository projectRepository;
    private final MailService mailService;

    public void saveAnswersStat() {
        /* Check if there are any answer logs in the database */
        long countAnswerLogs = answerLogRepository.count();

        if (countAnswerLogs == 0) {
            logger.info("There are no logs to work with it");
            return;
        }

        /* Check if there are any stats in the database */
        long countAnswerStats = answerStatRepository.count();
        List<AnswerStatDto> groupedAnswers;
        /* If this is the first stat ever */
        if (countAnswerStats == 0) {
            /* Obtain all stats ever */
            groupedAnswers = answerLogRepository.findAllAnswersGroupByProjectAndQuestionCategory();
            logger.info("First time calculating stats. All logs present will be saved in this stat row");
        } else {
            /* From the last one */
            LocalDateTime lastStatDate = answerStatRepository.getLastStatDate();
            groupedAnswers = answerLogRepository.findAllAnswersGroupByProjectAndQuestionCategoryAfter(lastStatDate);
            logger.info("Calculating stat from answer logs from " + lastStatDate + " to the most recent one");
        }

        groupedAnswers
                .stream()
                .map(AnswerStatDto::toEntity)
                .map(this::saveWeekAndYearToStat)
                .forEach(answerStatRepository::save);
    }

    public void alert() throws Exception {
        List<AlertProjectCategoryDto> lastStatsAlert;
        List<AlertProjectCategoryDto> nextToLastStatsAlert;
        List<AlertProjectCategoryDto> mergedStatsAlert = new ArrayList<>();

        lastStatsAlert = this.searchUndervalue();
        nextToLastStatsAlert = this.searchDeclining();

        mergedStatsAlert.addAll(lastStatsAlert);
        mergedStatsAlert.addAll(nextToLastStatsAlert);

        /* If there are no new data */
        boolean isOnLastDataVerified = CollectionUtils.isEmpty(mergedStatsAlert); /* kind of a default backup */
        if (isOnLastDataVerified) {
            /* Calculate the last already analyzed stats */
            mergedStatsAlert.addAll(this.searchLastUndervaluedVerifiedStat());
        }

        /* If problematic projects are found send email to project subscribers */
        if (!CollectionUtils.isEmpty(mergedStatsAlert)) {
            logger.info("Problematic projects found. The projects are: " + mergedStatsAlert);
            /* Merge based on project */
            Map<UUID, List<AlertProjectCategoryDto>> mergedAlertsGrouped = mergedStatsAlert.stream()
                    .collect(Collectors.groupingBy(alertProjectCategoryDto -> alertProjectCategoryDto.getProjectDto().getUuid()));

            for (Map.Entry<UUID, List<AlertProjectCategoryDto>> entry : mergedAlertsGrouped.entrySet()) {
                UUID projectUuid = entry.getKey();
                List<AlertProjectCategoryDto> alertProjectCategoryDtos = entry.getValue();
                /* Merge based on category */
                Map<UUID, List<AlertProjectCategoryDto>> categoriesGrouped = alertProjectCategoryDtos.stream()
                        .collect(Collectors.groupingBy(alertProjectCategoryDto -> alertProjectCategoryDto
                                .getQuestionCategoryDto().getUuid()));

                WrapProjectAlert wrapProjectAlert = new WrapProjectAlert();
                categoriesGrouped.forEach((categoryUuid, categories) -> {
                    AlertProjectCategoryDto alertProjectCategoryDto = new AlertProjectCategoryDto();
                    alertProjectCategoryDto.setValue("");

                    categories.forEach(alert -> {
                        String value = alertProjectCategoryDto.getValue();
                        if (!Objects.isNull(alert.getValue()))
                            value = value + alert.getValue();
                        alertProjectCategoryDto.setValue(value);
                        alertProjectCategoryDto.getAlertsType().addAll(alert.getAlertsType());
                        alertProjectCategoryDto.setQuestionCategoryDto(alert.getQuestionCategoryDto());
                        alertProjectCategoryDto.setProjectDto(alert.getProjectDto());

                        wrapProjectAlert.setProjectDto(alert.getProjectDto());
                    });

                    wrapProjectAlert.getAlertProjectCategories().add(alertProjectCategoryDto);
                });

                Long projectId = projectRepository.findByUuid(projectUuid).get().getId();
                Double lastStatsProjectAvgValue;

                if (isOnLastDataVerified)
                    lastStatsProjectAvgValue = answerStatRepository.getLastVerifiedStatsProjectAvgValue(projectId);
                else
                    lastStatsProjectAvgValue = answerStatRepository.getLastUnverifiedStatsProjectAvgValue(projectId);

                wrapProjectAlert.setAverageValue(lastStatsProjectAvgValue);

                /* Set email list */
                List<String> projectSubscribers = new ArrayList<>();
                projectAppUserRepository.findAllByProjectAndRole(projectRepository.findByUuid(projectUuid).get(), roleService.getSubscriber())
                        .forEach(sub -> projectSubscribers.add(sub.getAppUser().getEmail()));

                WrapTemplate templateProject = new WrapTemplate("project", wrapProjectAlert);
                WrapTemplate templateClientUrl = new WrapTemplate("clientUrl", clientUrl);

                String template = mailService.generateTemplate("warning_template.ftl",
                        templateProject, templateClientUrl);
/*                try {
                    mailService.sendHtmlMessage(
                            projectSubscribers
                            , "Project alert: " + wrapProjectAlert.getProjectDto().getName()
                            , template);
                } catch (MessagingException e) {
                    logger.error("Mail could not be sent");
                }*/
            }

            this.markAsVerified();
        }
    }

    public List<AnswerStatDto> getAll() {
        return answerStatRepository.findAll().stream().map(AnswerStatEntity::toDto).collect(Collectors.toList());
    }

    /* TODO: Resolve duplicate code in searchLastUndervaluedVerifiedStat and searchUndervalue methods */
    private List<AlertProjectCategoryDto> searchLastUndervaluedVerifiedStat() {
        List<AlertProjectCategoryDto> lastStatsAlert = new ArrayList<>();

        List<AnswerStatEntity> lastAnswerStatEntities = answerStatRepository.getNextToLastAnswerStat().orElseThrow(() -> {
            logger.error("Can not retrieve data from database");
            throw new BusinessException("Can not retrieve data from database");
        });

        /* Check if are any stats presents in db */
        if (ObjectUtils.isEmpty(lastAnswerStatEntities)) {
            logger.info("There are not any NEW stats in database in order to process undervalued stats");
            return new ArrayList<>();
        }

        processAlerts(lastStatsAlert, lastAnswerStatEntities);
        return lastStatsAlert;
    }

    private void processAlerts(List<AlertProjectCategoryDto> lastStatsAlert, List<AnswerStatEntity> lastAnswerStatEntities) {
        /* Iterate all last stats (by projects and categories) */
        lastAnswerStatEntities.forEach(answerStatEntity -> {
            if (answerStatEntity.getAvgValue() < SatisfactionType.AVERAGE.getValue()) {
                /* Check if the last stat is under the value 3 (medium satisfaction) */
                AlertProjectCategoryDto alertProjectCategoryDto = AlertProjectCategoryDto.builder()
                        .questionCategoryDto(questionCategoryRepository
                                .findById(answerStatEntity.getQuestionCategoryId())
                                .get().toDto())
                        .projectDto(projectRepository
                                .findById(answerStatEntity.getProjectId())
                                .get().toDto())
                        .value(answerStatEntity.getAvgValue().toString())
                        .alertsType(Collections.singletonList(AlertType.AVG_TOO_LOW))
                        .build();
                lastStatsAlert.add(alertProjectCategoryDto);
            }
        });
    }

    private List<AlertProjectCategoryDto> searchUndervalue() {
        List<AlertProjectCategoryDto> lastStatsAlert = new ArrayList<>();

        List<AnswerStatEntity> lastAnswerStatEntities = answerStatRepository.getLastUnverifiedAnswerStats().orElseThrow(() -> {
            throw new BusinessException("Can not retrieve data from database");
        });

        if (ObjectUtils.isEmpty(lastAnswerStatEntities)) {
            logger.info("There are not any NEW stats in database in order to process undervalued stats");
            return new ArrayList<>();
        }

        processAlerts(lastStatsAlert, lastAnswerStatEntities);
        return lastStatsAlert;
    }

    private List<AlertProjectCategoryDto> searchDeclining() {
        List<AlertProjectCategoryDto> nextToLastStatsAlert = new ArrayList<>();

        /* Check if are any stats presents in db */
        List<AnswerStatEntity> lastAnswerStatEntities = answerStatRepository.getLastUnverifiedAnswerStats().orElseThrow(() -> {
            throw new BusinessException("There are errors in retrieving stats data from database");
        });

        if (ObjectUtils.isEmpty(lastAnswerStatEntities)) {
            logger.info("There are not any NEW stats in database in order to process declining values, from previous stats");
            return new ArrayList<>();
        }

        /* Check if there are any next to last stats */
        List<AnswerStatEntity> nextToLastAnswerStatEntities = answerStatRepository.getNextToLastAnswerStat()
                .orElseThrow(() -> {
                    throw new BusinessException("There are errors in retrieving stats data from database");
                });

        if (ObjectUtils.isEmpty(nextToLastAnswerStatEntities)) {
            logger.info("There are not next to last stats in database in order to process declining values");
            return new ArrayList<>();
        }

        nextToLastAnswerStatEntities.addAll(lastAnswerStatEntities);
        Map<Long, Map<Long, List<AnswerStatEntity>>> collect = nextToLastAnswerStatEntities.stream()
                .collect(
                        Collectors.groupingBy(AnswerStatEntity::getProjectId,
                                Collectors.groupingBy(AnswerStatEntity::getQuestionCategoryId)));

        collect.forEach((projectId, projects) -> {
            /* Iterate projects  */
            projects.forEach((questionCategoryId, questionCategories) -> {
                /* Compare the previous stat with the current one by subtraction */
                List<AnswerStatEntity> sorted = questionCategories.stream()
                        .sorted(Comparator.comparing(AnswerStatEntity::getToDate))
                        .toList();

                /* Firstly set the first value from our list as initial data in order to do subtraction */
                double dif = sorted.get(0).getAvgValue() -
                        sorted.get(sorted.size() - 1).getAvgValue();

                /* Maximum vibe swing allowed */
                double maxVibeSwing = 1D;
                if (dif >= maxVibeSwing) {
                    AlertProjectCategoryDto alertProjectCategoryDto = AlertProjectCategoryDto.builder()
                            .questionCategoryDto(questionCategoryRepository
                                    .findById(questionCategoryId)
                                    .get().toDto())
                            .projectDto(projectRepository
                                    .findById(projectId)
                                    .get().toDto())
                            //.value(dif)
                            .alertsType(Collections.singletonList(AlertType.DECLINING_VALUE))
                            .build();
                    nextToLastStatsAlert.add(alertProjectCategoryDto);
                }
            });
        });

        return nextToLastStatsAlert;
    }

    public void markAsVerified() {
        /* Check if are any stats presents in db */
        List<AnswerStatEntity> lastAnswerStatEntities = answerStatRepository.getLastUnverifiedAnswerStats().orElseThrow(() -> {
            throw new BusinessException("Can not retrieve data from database");
        });

        /* Mark as is Verified */
        lastAnswerStatEntities
                .forEach(answerStatEntity -> {
                    answerStatEntity.setIsVerified(true);
                    answerStatRepository.save(answerStatEntity);
                });
    }

    private AnswerStatEntity saveWeekAndYearToStat(AnswerStatEntity answerStat) {

        LocalDateTime date = answerStat.getToDate();

        long week = date.get(WeekFields.ISO.weekOfWeekBasedYear());
        String formattedWeek = String.format("%02d", week);

        answerStat.setWeekAndYearOfStat("Year " + String.valueOf(date.get ( IsoFields.WEEK_BASED_YEAR )).concat(" Week ").concat(formattedWeek));
        return answerStat;
    }
}

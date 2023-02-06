package com.endava.endavibe.quiz;

import com.endava.endavibe.appUser.AppUserService;
import com.endava.endavibe.common.dto.*;
import com.endava.endavibe.common.dto.quiz.PostCompleteQuizDto;
import com.endava.endavibe.common.dto.quiz.QuizDto;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.util.ValidationUtils;
import com.endava.endavibe.log.answerLog.AnswerLogService;
import com.endava.endavibe.log.answerUserLog.AnswerUserService;
import com.endava.endavibe.log.questionLog.QuestionLogService;
import com.endava.endavibe.mail.MailService;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.project.ProjectService;
import com.endava.endavibe.project.projectQuiz.ProjectQuizEntity;
import com.endava.endavibe.project.projectQuiz.ProjectQuizRepository;
import com.endava.endavibe.question.QuestionEntity;
import com.endava.endavibe.question.QuestionService;
import com.endava.endavibe.quiz.questionQuiz.QuestionQuizEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class QuizService {

    Logger logger = LoggerFactory.getLogger(QuizService.class);

    @Value("${client.url}")
    private String clientUrl;

    private final ModelMapper modelMapper;
    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final ProjectQuizRepository projectQuizRepository;
    private final MailService mailService;
    private final AnswerLogService answerLogService;
    private final AnswerUserService answerUserService;
    private final AppUserService appUserService;
    private final QuestionLogService questionLogService;


    @Transactional
    public void generateQuiz() throws Exception {

        List<ProjectEntity> projectsToSendQuizTo = getProjectsToSendQuizTo();

        if (CollectionUtils.isEmpty(projectsToSendQuizTo)) {
            throw new BusinessException("No projects to send quiz to ");
        }
        for (ProjectEntity project : projectsToSendQuizTo) {
            QuizEntity savedQuiz = QuizEntity.builder()
                    .insDate(LocalDateTime.now())
                    .isActive(Boolean.FALSE)
                    .build();

            quizRepository.save(savedQuiz);
            logger.info("Quiz has been generated with id :" + savedQuiz.getId());

            sendQuizToProjectTeamMembers(project, savedQuiz);
        }

    }

    private void sendQuizToProjectTeamMembers(ProjectEntity project, QuizEntity quizEntity) throws Exception {

        QuizDto generatedQuiz = generateQuizDtoFromEntity(project, quizEntity);

        Random random = new Random();
        int daysUntilNextSendDate = random.nextInt(5 - 2) + 2;

        WrapTemplate templateQuiz = new WrapTemplate("quiz", generatedQuiz);
        WrapTemplate templateProject = new WrapTemplate("project", project);
        WrapTemplate templateClientUrl = new WrapTemplate("clientUrl", clientUrl);
        String template = mailService.generateTemplate("quiz_template.ftl", templateQuiz, templateProject, templateClientUrl);

        ProjectQuizEntity projectQuiz = saveRelationBetweenQuizAndProject(project, quizEntity);
        /*  Log the question info and how many times was sent to a specific project */
        for (QuestionDto questionQuizEntity : generatedQuiz.getQuestions()) {
            questionLogService.questionLogInsert(
                    project.getId(),                         /* project id */
                    questionQuizEntity.getId(),              /* question id */
                    questionQuizEntity.getCategory().getId() /* category id */);
        }

        List<String> teamMemberMailList = projectService.getProjectTeamMembers(project)
                .stream().map(AppUserDto::getEmail)
                .toList();

        if (CollectionUtils.isEmpty(teamMemberMailList)) {
            throw new BusinessException("No team members found to send email to for project: " + project.getUuid());
        }

        /* TODO: uncomment this after the generation of data */
        //mailService.sendHtmlMessage(teamMemberMailList, "Quiz for project " + project.getName(), template);

        project.setNextSendDate(LocalDateTime.now().plusDays(daysUntilNextSendDate));
        project.setSendDate(LocalDateTime.now());
        projectQuiz.setIsSent(Boolean.TRUE);
        projectQuizRepository.save(projectQuiz);

        quizEntity.setIsActive(Boolean.TRUE);
        quizRepository.save(quizEntity);

    }

    public List<ProjectEntity> getProjectsToSendQuizTo() {

        return projectRepository.findProjectsByIsActive(Boolean.TRUE).stream()
                .filter(project -> project.getIsEngineActive().equals(Boolean.TRUE))
                .filter(project -> project.getSendDate() == null || project.getNextSendDate().toLocalDate().equals(LocalDate.now()))
                .toList();
    }

    public QuizDto generateQuizDtoFromEntity(ProjectEntity project, QuizEntity quizEntity) throws BusinessException {

        QuizDto generatedQuizDto = modelMapper.map(quizEntity, QuizDto.class);

        List<QuestionDto> listOfQuestionsDto = questionService.getAListOfQuestions(project, quizEntity);

        generatedQuizDto.setQuestions(listOfQuestionsDto);

        logger.info("Quiz has been created completely.");
        return generatedQuizDto;
    }

    private ProjectQuizEntity saveRelationBetweenQuizAndProject(ProjectEntity project, QuizEntity quizEntity) {

        ProjectQuizEntity projectQuizEntity = ProjectQuizEntity.builder()
                .project(project)
                .quiz(quizEntity)
                .isSent(Boolean.FALSE)
                .insDate(LocalDateTime.now())
                .build();

        projectQuizRepository.save(projectQuizEntity);
        logger.info("Link between Quiz " + quizEntity.getId() + " and Project : '" + project.getName() + "' has been created with id :" + projectQuizEntity.getId());

        return projectQuizEntity;
    }

    @Transactional
    public void completeQuiz(PostCompleteQuizDto postCompleteQuizDto) throws BusinessException, BadRequestException {
        if (ObjectUtils.isEmpty(postCompleteQuizDto) && ObjectUtils.isEmpty(postCompleteQuizDto.getQuizUuid())) {
            throw new BadRequestException("The received object is not correct");
        }

        QuizEntity quiz = quizRepository.findByUuid(postCompleteQuizDto.getQuizUuid()).orElseThrow(() -> {
            throw new BusinessException("No quiz with Id " + postCompleteQuizDto.getQuizUuid());
        });


        answerLogService.saveAnswerLog(postCompleteQuizDto);
        answerUserService.saveUserLog(quiz.getUuid(), postCompleteQuizDto.getAppUserUuid(),postCompleteQuizDto.getProjectUuid());
    }

    public QuizDto getQuizById(UUID uuid, UUID projectUuid) throws BusinessException {
        QuizEntity quiz = quizRepository.findByUuid(uuid).orElseThrow(() -> new BusinessException("No quiz found for id : " + uuid));

        ProjectEntity project = projectRepository.findByUuid(projectUuid).orElseThrow(() -> new BusinessException("No project found for id : " + projectUuid));

        ValidationUtils.validateQuiz(quiz.getId(), appUserService.getLoggedAppUserEntity().getId(), project.getId());

        QuizDto quizDto = modelMapper.map(quiz, QuizDto.class);
        quizDto.setQuestions(questionService.setQuestionsForGivenQuiz(quiz));

        return quizDto;
    }
}
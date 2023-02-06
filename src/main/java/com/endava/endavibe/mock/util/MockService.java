package com.endava.endavibe.mock.util;

import com.endava.endavibe.answer.AnswerEntity;
import com.endava.endavibe.answer.AnswerRepository;
import com.endava.endavibe.appUser.AppUserService;
import com.endava.endavibe.appUser.role.RoleService;
import com.endava.endavibe.common.dto.*;
import com.endava.endavibe.common.dto.quiz.PostCompleteQuizDto;
import com.endava.endavibe.common.dto.stats.AnswerDto;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.log.answerLog.AnswerLogEntity;
import com.endava.endavibe.log.answerLog.AnswerLogRepository;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.project.ProjectService;
import com.endava.endavibe.project.projectQuiz.ProjectQuizEntity;
import com.endava.endavibe.project.projectQuiz.ProjectQuizRepository;
import com.endava.endavibe.question.QuestionEntity;
import com.endava.endavibe.question.QuestionRepository;
import com.endava.endavibe.quiz.QuizEntity;
import com.endava.endavibe.quiz.questionQuiz.QuestionQuizEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MockService {
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final AppUserService appUserService;
    private final ModelMapper modelMapper;
    private final AnswerRepository answerRepository;
    private final ProjectQuizRepository projectQuizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerLogRepository answerLogRepository;
    private final RoleService roleService;

    public void createProject() throws Exception {
        AppUserDto loggedAppUserDto = appUserService.getLoggedAppUserDto();
        List<AppUserDto> allAppUserDtos = new ArrayList<>(MockConstants.APP_USER_DTOS);
        List<String> projectNames =  new ArrayList<>(MockConstants.PROJECT_NAMES);

        Boolean contains = allAppUserDtos.contains(loggedAppUserDto);

        if (BooleanUtils.isTrue(contains))
            allAppUserDtos.remove(loggedAppUserDto);

        Collections.shuffle(allAppUserDtos);
        Collections.shuffle(projectNames);

        List<AppUserDto> teamMembers = new ArrayList<>(allAppUserDtos.subList(0, allAppUserDtos.size()/2));
        teamMembers.add(loggedAppUserDto);

        List<SubscriberDto> subscribers = new ArrayList<>(allAppUserDtos.subList(allAppUserDtos.size()/2+1, allAppUserDtos.size()).stream()
                .map(appUserDto -> modelMapper.map(appUserDto, SubscriberDto.class)).toList());
        subscribers.add(modelMapper.map(loggedAppUserDto, SubscriberDto.class));

        ProjectDto projectDto = ProjectDto.builder()
                .name(projectNames.get(0))
                .listOfTeamMembers(teamMembers)
                .isEngineActive(Boolean.TRUE)
                .build();

        projectService.createProject(projectDto);
    }

    public void generateAnswers(LocalDateTime from, LocalDateTime to) {
        /* Iterate quizzes */
        List<AnswerDto> answers = new ArrayList<>(answerRepository.findAll().stream().map(AnswerEntity::toDto).toList());
        List<ProjectQuizEntity> allByIsSent = projectQuizRepository.findAllByIsSent(true);

        allByIsSent.forEach(projectQuizEntity -> {
            ProjectEntity project = projectQuizEntity.getProject();

            QuizEntity quiz = projectQuizEntity.getQuiz();
            Collection<QuestionQuizEntity> questionQuizList = quiz.getQuestionQuizList();

            List<AppUserDto> projectTeamMembers = projectService.getProjectTeamMembers(project);

            projectTeamMembers.forEach(appUserDto -> {
                List<QuestionAnswerDto> questionAnswerDtos = new ArrayList<>();
                questionQuizList.forEach(questionQuizEntity -> {
                    /* Shuffle list in order to give random answers */
                    Collections.shuffle(answers);

                    QuestionEntity question = questionQuizEntity.getQuestion();
                    QuestionAnswerDto questionAnswerDto = QuestionAnswerDto.builder()
                            .questionUuid(question.getUuid())
                            .answerUuid(answers.get(0).getUuid())
                            .build();
                    questionAnswerDtos.add(questionAnswerDto);
                });

                PostCompleteQuizDto postCompleteQuizDto = PostCompleteQuizDto.builder()
                        .appUserUuid(appUserDto.getUuid())
                        .quizUuid(quiz.getUuid())
                        .projectUuid(project.getUuid())
                        .listOfAnswers(questionAnswerDtos)
                        .build();

                /* Random Date between given interval */
                int i = to.getDayOfYear() - from.getDayOfYear();
                int random = RandomUtils.nextInt(0, i+1);
                LocalDateTime randomDate = from.plusDays(random);

                /* SAVES LOGS */
                saveAnswerLog(postCompleteQuizDto, randomDate);
            });
        });
    }

    public void saveAnswerLog(PostCompleteQuizDto postCompleteQuizDto, LocalDateTime date) throws BusinessException {

        UUID projectUuid = postCompleteQuizDto.getProjectUuid();
        ProjectEntity project = projectRepository.findByUuid(projectUuid).orElseThrow(() -> {
            throw new BusinessException("There are no projects with UUID " + projectUuid);
        });

        postCompleteQuizDto.getListOfAnswers().forEach(questionAnswerDto -> {
            AnswerLogEntity answerLogEntity = new AnswerLogEntity();
            answerLogEntity.setProjectId(project.getId());

            /* Set Question data */
            UUID questionUuid = questionAnswerDto.getQuestionUuid();
            QuestionEntity question = questionRepository.findByUuid(questionUuid)
                    .orElseThrow(() -> {
                        throw new BusinessException("There is no question with UUID " + questionUuid);
                    });
            answerLogEntity.setQuestionId(question.getId());
            answerLogEntity.setQuestionCategory(question.getQuestionCategory().getTitle());
            answerLogEntity.setQuestionCategoryId(question.getQuestionCategory().getId());
            answerLogEntity.setQuestionContent(question.getContent());

            /* Set Answer value */
            UUID answerUuid = questionAnswerDto.getAnswerUuid();
            AnswerEntity answer = answerRepository.findByUuid(answerUuid).orElseThrow(() -> {
                throw new BusinessException("There is no answer with UUID " + answerUuid);
            });
            answerLogEntity.setAnswerValue(answer.getValue());

            /* Set insertion date */
            answerLogEntity.setInsDate(date);

            /* Save into database */
            answerLogRepository.save(answerLogEntity);
        });
    }
}

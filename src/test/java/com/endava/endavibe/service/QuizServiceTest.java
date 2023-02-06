package com.endava.endavibe.service;

import com.endava.endavibe.appUser.AppUserService;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.QuestionDto;
import com.endava.endavibe.common.dto.quiz.QuizDto;
import com.endava.endavibe.common.util.ValidationUtils;
import com.endava.endavibe.log.answerUserLog.AnswerUserRepository;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.quiz.QuizEntity;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.question.QuestionService;
import com.endava.endavibe.quiz.QuizRepository;
import com.endava.endavibe.quiz.QuizService;
import com.endava.endavibe.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;
    @Mock
    private QuestionService questionService;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private AppUserService appUserService;
    @Mock
    private AnswerUserRepository answerUserRepository;

    @InjectMocks
    private QuizService quizService;
    @InjectMocks
    private ValidationUtils validationUtils;
    private QuizEntity quizEntity;
    private QuizDto quizDto;
    private UUID quizUuid;
    private ProjectEntity projectEntity;
    private List<QuestionDto> questionDtoList;
    private UUID projectUuid;

    @Spy
    private ModelMapper modelMapper;

    @Before
    public void setup() {
        quizDto = Utils.getQuizDto();
        quizUuid = Utils.quizUuid;
        quizEntity = Utils.getQuizEntity();
        projectEntity = Utils.getProjectEntityWithAssignations().get();
        questionDtoList = Utils.getQuestionList();
        projectUuid = Utils.projectUuid;

    }

    @Test
    public void getQuizById_Test() throws BusinessException {

        given(quizRepository.findByUuid(any())).willReturn(Optional.of(quizEntity));
        given(questionService.setQuestionsForGivenQuiz(any())).willReturn(Utils.getQuestionList());
        given(projectRepository.findByUuid(any())).willReturn(Optional.of(projectEntity));

        given(appUserService.getLoggedAppUserEntity()).willReturn(Utils.getAppUserEntity());
        assertThatCode(() -> validationUtils.validateQuiz(any(),any(),any())).doesNotThrowAnyException();

        QuizDto result = quizService.getQuizById(quizUuid,projectUuid);

        assertEquals(result.getId(), quizEntity.getId());
    }

    @Test
    public void getQuizById_NoQuizFound_Test() throws BusinessException {

        BusinessException businessException = Assertions.assertThrows(BusinessException.class, () ->
                quizService.getQuizById(quizUuid, projectUuid)
        );
        assertEquals(businessException.getMessage(), "No quiz found for id : " + quizUuid);
    }

    @Test
    public void getProjectsToSendQuizTo_Test() {
        given(projectRepository.findProjectsByIsActive(anyBoolean())).willReturn(List.of(projectEntity));
        List<ProjectEntity> result = quizService.getProjectsToSendQuizTo();

        assertEquals(result.size(), List.of(projectEntity).size());
    }

    @Test
    public void generateQuizDtoFromEntity_Test() {
        given(questionService.getAListOfQuestions(any(), any())).willReturn(questionDtoList);

        QuizDto result = quizService.generateQuizDtoFromEntity(projectEntity, quizEntity);

        assertEquals(quizEntity.getUuid(), result.getUuid());
    }

}

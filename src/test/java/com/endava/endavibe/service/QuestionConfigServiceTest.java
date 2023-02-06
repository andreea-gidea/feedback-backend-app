package com.endava.endavibe.service;

import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.answer.AnswerRepository;
import com.endava.endavibe.question.questionConfig.QuestionConfigRepository;
import com.endava.endavibe.question.questionConfig.QuestionConfigService;
import com.endava.endavibe.util.Utils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class QuestionConfigServiceTest {

    @InjectMocks
    private QuestionConfigService questionConfigService;

    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private QuestionConfigRepository questionConfigRepository;

    @Test
    public void createDefaultQuestionConfig_BusinessEx_Test() {
        BusinessException businessException = Assertions.assertThrows(BusinessException.class, () ->
                questionConfigService.createDefaultQuestionConfig(null)
        );

        assertEquals(businessException.getMessage(), "Question entity is null");
    }

    @Test
    public void createDefaultQuestionConfig_NoAnswer_Test() {

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () ->
                questionConfigService.createDefaultQuestionConfig(Utils.getQuestionEntity())
        );

        assertEquals(runtimeException.getMessage(), "No answer for given id 1");
    }

    @Test
    public void deleteQuestionConfigByQuestion_BusinessEx_Test() {
        BusinessException businessException = Assertions.assertThrows(BusinessException.class, () ->
                questionConfigService.deleteQuestionConfigByQuestion(Utils.getQuestionEntity())
        );

        assertEquals(businessException.getMessage(), "No question config found for question " + Utils.getQuestionEntity().getUuid());
    }

}

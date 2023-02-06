package com.endava.endavibe.service;

import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.QuestionDto;
import com.endava.endavibe.question.questionCategory.QuestionCategoryEntity;
import com.endava.endavibe.question.QuestionEntity;
import com.endava.endavibe.question.QuestionService;
import com.endava.endavibe.question.questionCategory.QuestionCategoryRepository;
import com.endava.endavibe.question.QuestionRepository;
import com.endava.endavibe.question.questionConfig.QuestionConfigService;
import com.endava.endavibe.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {

    @Mock
    private QuestionCategoryRepository questionCategoryRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private QuestionConfigService questionConfigService;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private QuestionService questionService;


    private QuestionCategoryEntity questionCategoryEntity;
    private QuestionDto readQuestionDto;
    private QuestionDto writeQuestionDto;
    private QuestionEntity questionEntity;

    @Before
    public void setup() {
        questionCategoryEntity = Utils.getQuestionCategoryEntity();
        readQuestionDto = Utils.getReadQuestionDto();
        writeQuestionDto = Utils.getWriteQuestionDto();
        questionEntity = Utils.getQuestionEntity();
    }

    @Test
    public void addQuestion_Test() throws BusinessException {
        given(questionCategoryRepository.findByUuid(any())).willReturn(Optional.of(questionCategoryEntity));
        given(questionRepository.save(any())).willReturn(questionEntity);
        doNothing().when(questionConfigService).createDefaultQuestionConfig(any());

        QuestionDto result = questionService.addQuestion(writeQuestionDto);

        assertEquals(result.getUuid(), readQuestionDto.getUuid());
    }

    @Test
    public void addQuestion_NoCategory_Test() {

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () ->
                questionService.addQuestion(writeQuestionDto)
        );

        assertEquals(runtimeException.getMessage(), "No question category for given id " + writeQuestionDto.getCategoryUuid());
    }

    @Test
    public void updateQuestionByUuid_Test() {
        given(questionRepository.findByUuid(any())).willReturn(Optional.ofNullable(questionEntity));
        given(questionCategoryRepository.findByUuid(any())).willReturn(Optional.of(questionCategoryEntity));
        given(questionRepository.save(any())).willReturn(questionEntity);

        QuestionDto result = questionService.updateQuestionByUuid(Utils.questionUuid1, writeQuestionDto);

        assertEquals(result.getContent(), readQuestionDto.getContent());
        assertEquals(result.getUuid(), readQuestionDto.getUuid());
    }

    @Test
    public void updateQuestionByUuid_NoQuestion_Test() {

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () ->
                questionService.updateQuestionByUuid(Utils.questionUuid1, writeQuestionDto)
        );

        assertEquals(runtimeException.getMessage(), "No question found for given id " + Utils.questionUuid1);
    }

    @Test
    public void updateQuestionByUuid_NoCategory_Test() {

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () ->
                questionService.addQuestion(writeQuestionDto)
        );

        assertEquals(runtimeException.getMessage(), "No question category for given id " + writeQuestionDto.getCategoryUuid());
    }

}

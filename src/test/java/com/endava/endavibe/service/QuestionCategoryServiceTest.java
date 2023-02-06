package com.endava.endavibe.service;

import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.QuestionCategoryDto;
import com.endava.endavibe.question.questionCategory.QuestionCategoryEntity;
import com.endava.endavibe.question.questionCategory.QuestionCategoryRepository;
import com.endava.endavibe.question.questionCategory.QuestionCategoryService;
import com.endava.endavibe.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class QuestionCategoryServiceTest {

    @Mock
    private QuestionCategoryRepository questionCategoryRepo;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private QuestionCategoryService questionCategoryService;

    private List<QuestionCategoryDto> questionCategoryDtoList;
    private QuestionCategoryDto writeQuestionCategoryDto;
    private QuestionCategoryEntity questionCategoryEntity;
    private QuestionCategoryEntity updatedQuestionCategoryEntity;

    @Before
    public void setup() {
        questionCategoryDtoList = Utils.getQuestionCategoryDtoList();
        writeQuestionCategoryDto = Utils.getCreateQuestionCategoryDto();
        questionCategoryEntity = Utils.getQuestionCategoryEntity();
        updatedQuestionCategoryEntity = Utils.getQuestionCategoryEntity();
        updatedQuestionCategoryEntity.setTitle("Updated_Title");
    }

    @Test
    public void getAllQuestionCategory_Test() {
        given(questionCategoryRepo.findAll()).willReturn(Utils.getQuestionCategoryEntityList());

        List<QuestionCategoryDto> result = questionCategoryService.getAllQuestionCategory();

        assertEquals(result.size(), questionCategoryDtoList.size());
    }

    @Test
    public void getAllQuestionCategory_NoCategory_Test() {

        List<QuestionCategoryDto> result = questionCategoryService.getAllQuestionCategory();

        assertEquals(result.size(), 0);
    }

    @Test
    public void addQuestionCategory_Test() {
        given(questionCategoryRepo.save(any())).willReturn(questionCategoryEntity);

        QuestionCategoryDto result = questionCategoryService.addQuestionCategory(writeQuestionCategoryDto);

        assertEquals(result.getTitle(), (writeQuestionCategoryDto.getTitle()));
    }

    @Test
    public void addQuestionCategory_NoEntity_Test() {

        QuestionCategoryDto result = questionCategoryService.addQuestionCategory(writeQuestionCategoryDto);

        assertNotNull(result);
    }

    @Test
    public void updateQuestionCategoryByUuid_Test() throws BusinessException {
        given(questionCategoryRepo.findByUuid(any())).willReturn(Optional.ofNullable(questionCategoryEntity));
        given(questionCategoryRepo.save(any())).willReturn(updatedQuestionCategoryEntity);

        QuestionCategoryDto result = questionCategoryService.updateQuestionCategoryByUuid(Utils.questionCatUuid1, writeQuestionCategoryDto);
        QuestionCategoryDto expectedResult = Utils.getQuestionCatFromEntity(updatedQuestionCategoryEntity);

        assertEquals(result.getUuid(), expectedResult.getUuid());
        assertEquals(result.getTitle(), expectedResult.getTitle());
    }

    @Test(expected = BusinessException.class)
    public void updateQuestionCategoryByUuid_NoCategory_Test() throws BusinessException {

        questionCategoryService.updateQuestionCategoryByUuid(Utils.questionCatUuid1, writeQuestionCategoryDto);
    }

    @Test
    public void deleteQuestionCategoryByUuid_Test() throws BusinessException {
        given(questionCategoryRepo.findByUuid(any())).willReturn(Optional.ofNullable(questionCategoryEntity));
        doNothing().when(questionCategoryRepo).delete(any());

        questionCategoryService.deleteQuestionCategoryByUuid(any());
    }

    @Test(expected = BusinessException.class)
    public void deleteQuestionCategoryByUuid_NoCategory_Test() throws BusinessException {

        questionCategoryService.deleteQuestionCategoryByUuid(Utils.questionCatUuid1);
    }

    @Test(expected = BusinessException.class)
    public void deleteQuestionCategoryByUuid_QRelation_Test() throws BusinessException {
        given(questionCategoryRepo.findByUuid(any())).willReturn(Optional.ofNullable(Utils.getQuestionCategoryEntityQWithQuestion()));
        questionCategoryService.deleteQuestionCategoryByUuid(Utils.questionCatUuid1);
    }


}

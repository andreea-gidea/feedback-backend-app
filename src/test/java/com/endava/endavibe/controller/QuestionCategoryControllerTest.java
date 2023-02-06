package com.endava.endavibe.controller;

import com.endava.endavibe.EndavibeApplication;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.question.questionCategory.QuestionCategoryService;
import com.endava.endavibe.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EndavibeApplication.class)
@WebAppConfiguration
public class QuestionCategoryControllerTest {

    protected MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private QuestionCategoryService questionCategoryService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getAllQuestionCategory_Test() throws Exception {
        given(questionCategoryService.getAllQuestionCategory()).willReturn(Utils.getQuestionCategoryDtoList());

        MvcResult response = mvc.perform(get("/category").accept(APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getAllQuestionCategory_NoContent_Test() throws Exception {
        given(questionCategoryService.getAllQuestionCategory()).willReturn(new ArrayList<>());

        MvcResult response = mvc.perform(get("/category").accept(APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void addQuestionCategory_Test() throws Exception {
        given(questionCategoryService.addQuestionCategory(any())).willReturn(Utils.getReadQuestionCategoryDto());

        MvcResult response = mvc.perform(post("/category").contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(Utils.getCreateQuestionCategoryDto())))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void addQuestionCategory_NoContent_Test() throws Exception {
        given(questionCategoryService.addQuestionCategory(any())).willReturn(null);

        MvcResult response = mvc.perform(post("/category").contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(Utils.getCreateQuestionCategoryDto())))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void addQuestionCategory_BadRequest_Test() throws Exception {

        final String emptyBody = "{}";
        MvcResult response = mvc.perform(post("/category").contentType(APPLICATION_JSON).content(emptyBody))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateQuestionCategory_Test() throws Exception {
        given(questionCategoryService.updateQuestionCategoryByUuid(any(), any())).willReturn(Utils.getReadQuestionCategoryDto());

        MvcResult response = mvc.perform(patch("/category/{uuid}", Utils.questionCatUuid1).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(Utils.getCreateQuestionCategoryDto())))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void updateQuestionCategory_NoContent_Test() throws Exception {
        given(questionCategoryService.updateQuestionCategoryByUuid(any(), any())).willReturn(null);

        MvcResult response = mvc.perform(patch("/category/{uuid}", Utils.questionCatUuid1).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(Utils.getCreateQuestionCategoryDto())))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void updateQuestionCategory_BadRequest_Test() throws Exception {

        final String emptyBody = "{}";
        MvcResult response = mvc.perform(patch("/category/{uuid}", Utils.questionCatUuid1).contentType(APPLICATION_JSON).content(emptyBody))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateQuestionCategory_BusinessException_Test() throws Exception {
        doThrow(BusinessException.class)
                .when(questionCategoryService)
                .updateQuestionCategoryByUuid(any(), any());

        MvcResult response = mvc.perform(patch("/category/{uuid}", Utils.questionCatUuid1).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(Utils.getCreateQuestionCategoryDto())))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void deleteQuestionCategoryByUuid_Test() throws Exception {
        doNothing().when(questionCategoryService).deleteQuestionCategoryByUuid(any());

        MvcResult response = mvc.perform(delete("/category/{uuid}", Utils.questionCatUuid1).contentType(APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void deleteQuestionCategoryByUuid_BusinessException_Test() throws Exception {
        doThrow(BusinessException.class)
                .when(questionCategoryService)
                .deleteQuestionCategoryByUuid(any());

        MvcResult response = mvc.perform(delete("/category/{uuid}", Utils.questionCatUuid1).contentType(APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }


}

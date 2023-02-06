package com.endava.endavibe.controller;

import com.endava.endavibe.EndavibeApplication;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.QuestionDto;
import com.endava.endavibe.question.QuestionService;
import com.endava.endavibe.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
public class QuestionControllerTest {

    protected MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private QuestionService questionService;

    private QuestionDto writeQuestionDto;
    private QuestionDto readQuestionDto;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        writeQuestionDto = Utils.getWriteQuestionDto();
        readQuestionDto = Utils.getReadQuestionDto();
    }

    @Test
    public void getQuestionList_Test() throws Exception {
        given(questionService.getQuestionListByCategory()).willReturn(Utils.getQuestionList());

        MvcResult response = mvc.perform(get("/question").accept(APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getQuestionList_NoContent_Test() throws Exception {

        MvcResult response = mvc.perform(get("/question").accept(APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void getQuestionList_BusinessException_Test() throws Exception {
        doThrow(RuntimeException.class)
                .when(questionService)
                .getQuestionListByCategory();

        MvcResult response = mvc.perform(get("/question", Utils.questionCatUuid1).accept(APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    public void addQuestion_Test() throws Exception {
        given(questionService.addQuestion(any())).willReturn(readQuestionDto);

        MockHttpServletResponse response = mvc.perform(
                        post("/question").contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(writeQuestionDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void addQuestion_NoContent_Test() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                        post("/question").contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(writeQuestionDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void addQuestion_BadRequest_Test() throws Exception {

        final String emptyBody = "{}";
        MvcResult response = mvc.perform(post("/question").contentType(APPLICATION_JSON).content(emptyBody))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addQuestion_BusinessException_Test() throws Exception {
        doThrow(BusinessException.class)
                .when(questionService)
                .addQuestion(any());

        MvcResult response = mvc.perform(post("/question", Utils.questionCatUuid1).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(writeQuestionDto)))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void addQuestion_RuntimeException_Test() throws Exception {
        doThrow(RuntimeException.class)
                .when(questionService)
                .addQuestion(any());

        MvcResult response = mvc.perform(post("/question", Utils.questionCatUuid1).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(writeQuestionDto)))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    public void updateQuestionByUuid_Test() throws Exception {
        given(questionService.updateQuestionByUuid(any(), any())).willReturn(readQuestionDto);

        MockHttpServletResponse response = mvc.perform(
                        patch("/question/{uuid}", Utils.questionUuid1).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(writeQuestionDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void updateQuestionByUuid_NoContent_Test() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                        patch("/question/{uuid}", Utils.questionUuid1).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(writeQuestionDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void updateQuestionByUuid_BadRequest_Test() throws Exception {

        final String emptyBody = "{}";
        MvcResult response = mvc.perform(patch("/question/{uuid}", Utils.questionUuid1).contentType(APPLICATION_JSON).content(emptyBody))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateQuestionByUuid_RuntimeException_Test() throws Exception {
        doThrow(RuntimeException.class)
                .when(questionService)
                .updateQuestionByUuid(any(), any());

        MvcResult response = mvc.perform(patch("/question/{uuid}", Utils.questionUuid1).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(writeQuestionDto)))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    public void deleteQuestionCategoryByUuid_Test() throws Exception {
        doNothing().when(questionService).deleteQuestionByUuid(any());

        MvcResult response = mvc.perform(delete("/question/{uuid}", Utils.questionUuid1).contentType(APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void deleteQuestionCategoryByUuid_BusinessException_Test() throws Exception {
        doThrow(BusinessException.class)
                .when(questionService)
                .deleteQuestionByUuid(any());

        MvcResult response = mvc.perform(delete("/question/{uuid}", Utils.questionUuid1).contentType(APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

}

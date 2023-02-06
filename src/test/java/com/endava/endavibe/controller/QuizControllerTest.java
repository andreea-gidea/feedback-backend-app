package com.endava.endavibe.controller;

import com.endava.endavibe.EndavibeApplication;
import com.endava.endavibe.common.dto.quiz.QuizDto;
import com.endava.endavibe.quiz.QuizService;
import com.endava.endavibe.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EndavibeApplication.class)
@WebAppConfiguration
public class QuizControllerTest {

    protected MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private QuizService quizService;

    private QuizDto quizDto;
    private UUID quizUuid;
    private UUID projectUuid;


    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        quizUuid = Utils.quizUuid;
        quizDto = Utils.getQuizDto();
        projectUuid = Utils.quizUuid;
    }

    @Test
    public void getQuizById_Test() throws Exception {

        given(quizService.getQuizById(ArgumentMatchers.any(),ArgumentMatchers.any())).willReturn(quizDto);

        MockHttpServletResponse response = mvc.perform(
                        get("/quiz/{uuid}/{projectUuid}", quizUuid, projectUuid)
                                .accept(APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getQuizById_NoContent_Test() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                        get("/quiz/{uuid}/{projectUuid}", quizUuid, projectUuid)
                                .accept(APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}

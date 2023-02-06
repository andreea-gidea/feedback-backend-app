package com.endava.endavibe.controller;

import com.endava.endavibe.EndavibeApplication;
import com.endava.endavibe.common.dto.ProjectDto;
import com.endava.endavibe.project.ProjectService;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EndavibeApplication.class)
@WebAppConfiguration
public class ProjectControllerTest {

    protected MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private ProjectService projectService;

    private ProjectDto projectDto;
    private UUID projectUuid;
    private UUID appUserUuid;


    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        projectUuid = Utils.projectUuid;
        projectDto = Utils.getProjectDto();
        appUserUuid = Utils.appUserUuid_01;
    }



    @Test
    public void createAProjectGivenAName_Test() throws Exception {
        given(projectService.createProject(any())).willReturn(projectDto);

        MockHttpServletResponse response = mvc.perform(
                        post("/project").contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(projectDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void createAProjectGivenAName_RequestBodyEmpty_Test() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                        post("/project").contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(new ProjectDto())))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateProjectNameById_Test() throws Exception {

        given(projectService.updateProject(ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(projectDto);

        MockHttpServletResponse response = mvc.perform(
                        patch("/project/{uuid}", projectUuid).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(projectDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void updateProjectNameById_RequestBodyEmpty_Test() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                        patch("/project/{uuid}", projectUuid).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(new ProjectDto())))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void getAllProjectsByCurrentUser_Test() throws Exception {

        given(projectService.getAllProjectsByCurrentUser()).willReturn(Utils.getProjectDtoList());

        MockHttpServletResponse response = mvc.perform(
                        get("/project")
                                .accept(APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getAllProjectsByCurrentUser_EmptyList_Test() throws Exception {
        Set<ProjectDto> projectEmptyList = new TreeSet<>();

        given(projectService.getAllProjectsByCurrentUser()).willReturn(projectEmptyList);

        MockHttpServletResponse response = mvc.perform(
                        get("/project")
                                .accept(APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void setActiveEngine_Test() throws Exception {

        given(projectService.setActiveEngine(ArgumentMatchers.any(), ArgumentMatchers.anyBoolean())).willReturn(projectDto);

        MockHttpServletResponse response = mvc.perform(
                        patch("/project/{uuid}/activeEngine", projectUuid)
                                .param("isActive", String.valueOf(true)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}

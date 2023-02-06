package com.endava.endavibe.controller;

import com.endava.endavibe.EndavibeApplication;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.appUser.AppUserService;
import com.endava.endavibe.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EndavibeApplication.class)
@WebAppConfiguration
public class AppUserControllerTest {

    protected MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private ModelMapper modelMapper;

    private AppUserDto writeAppUserDto;
    private AppUserDto readAppUserDto;
    private UUID appUserUuid;
    private UUID projectUuid;


    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        AppUserEntity appUserEntity = Utils.getAppUserEntity();
        appUserUuid = Utils.appUserUuid_01;
        projectUuid = Utils.projectUuid;
        writeAppUserDto = Utils.getWriteAppUserDtoFromEntity(appUserEntity);
        readAppUserDto = Utils.getReadAppUserDtoFromEntity(appUserEntity);
    }

    @Test
    public void getAppUserByIdTest() throws Exception {

        given(appUserService.getAppUserById(ArgumentMatchers.any())).willReturn(readAppUserDto);

        MockHttpServletResponse response = mvc.perform(
                        get("/user/{uuid}", appUserUuid)
                                .accept(APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getAppUserById_NoContent_Test() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                        get("/user/{uuid}", appUserUuid)
                                .accept(APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void getAppUserById_BusinessException_Test() throws Exception {
        doThrow(BusinessException.class)
                .when(appUserService)
                .getAppUserById(any());

        MvcResult response = mvc.perform(get("/user/{uuid}", appUserUuid).accept(APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }



    @Test
    public void updateAppUserTest() throws Exception {

        given(appUserService.updateAppUser(ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(readAppUserDto);

        MockHttpServletResponse response = mvc.perform(
                        patch("/user/{uuid}", appUserUuid).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(writeAppUserDto)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void updateAppUser_RequestBodyEmpty_Test() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                        patch("/user/{uuid}", appUserUuid).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(new AppUserDto())))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateAppUser_BusinessException_Test() throws Exception {
        doThrow(BusinessException.class)
                .when(appUserService)
                .updateAppUser(any(), any());

        MvcResult response = mvc.perform(patch("/user/{uuid}", appUserUuid).contentType(APPLICATION_JSON).content(Utils.getRequestBodyParam(writeAppUserDto)))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }




}

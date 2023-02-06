package com.endava.endavibe.service;

import com.endava.endavibe.appUser.AppUserService;
import com.endava.endavibe.appUser.role.RoleEntity;
import com.endava.endavibe.appUser.role.RoleService;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.util.enums.RolesEnum;
import com.endava.endavibe.config.CustomAuthentication;
import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.ProjectDto;
import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserEntity;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.project.ProjectService;
import com.endava.endavibe.appUser.AppUserRepository;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserRepository;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.subscriber.SubscriberService;
import com.endava.endavibe.util.Utils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private ProjectAppUserRepository projectAppUserRepository;
    @Mock
    private AppUserService appUserService;
    @Mock
    private SubscriberService subscriberService;
    @Mock
    private CustomAuthentication auth;
    @Mock
    private RoleService roleService;
//    @Mock
//    private ProjectService projectServiceMock;

    @InjectMocks
    private ProjectService projectService;

    @Spy
    private ModelMapper modelMapper;


    private ProjectEntity projectEntity;
    private ProjectDto fullProjectDto;
    private ProjectDto projectDtoWithTeamMembers;
    private ProjectDto projectDtoWithSubscribers;
    private UUID projectUuid;
    private AppUserEntity appUserEntity;

    @Before
    public void setup() {
        projectEntity = Utils.getProjectEntity().get();
        fullProjectDto = Utils.getFullProjectDto();
        projectDtoWithTeamMembers = Utils.getProjectDtoWithTeamMembers();
        projectDtoWithSubscribers = Utils.getProjectDtoWithSubscribers();
        projectUuid = Utils.projectUuid;
        appUserEntity = Utils.getAppUserEntity();
        auth = new CustomAuthentication(appUserEntity, appUserService);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    @Test
    public void createProject_Test() throws Exception {

        given(projectRepository.save(any())).willReturn(projectEntity);
        given(appUserService.addAndAssignAppUser(any(), any(), anyBoolean())).willReturn(Utils.getAppUserDto());
        doNothing().when(subscriberService).addAndAssignSubscriber(any(), any());
        given(roleService.getManager()).willReturn(Utils.getRoleEntity(RolesEnum.MANAGER));


        projectService.createProject(fullProjectDto);

    }

    @Test
    public void createProject_WithTeamMembers_Test() throws Exception {

        given(projectRepository.save(any())).willReturn(projectEntity);
        given(appUserService.addAndAssignAppUser(any(), any(), anyBoolean())).willReturn(Utils.getAppUserDto());
        given(roleService.getManager()).willReturn(Utils.getRoleEntity(RolesEnum.MANAGER));

        projectService.createProject(projectDtoWithTeamMembers);

    }

    @Test
    public void createProject_WithSubscriber_Test() throws Exception {

        given(projectRepository.save(any())).willReturn(projectEntity);
        doNothing().when(subscriberService).addAndAssignSubscriber(any(), any());
        given(roleService.getManager()).willReturn(Utils.getRoleEntity(RolesEnum.MANAGER));

        projectService.createProject(projectDtoWithSubscribers);

    }

    @Test
    public void updateProjectName_Test() throws BusinessException {

        given(projectRepository.findByUuid(any())).willReturn(Optional.ofNullable(projectEntity));
        given(roleService.getManager()).willReturn(Utils.getRoleEntity(RolesEnum.MANAGER));

        ProjectDto result = projectService.updateProject(projectUuid, fullProjectDto);

        assertEquals(result.getUuid(), fullProjectDto.getUuid());
    }

    @Test
    public void updateProjectName_ProjectNotFound_Test() {

        BusinessException businessException = Assertions.assertThrows(BusinessException.class, () ->
                projectService.updateProject(projectUuid, fullProjectDto)
        );

        assertEquals(businessException.getMessage(), "No project found for id : " + projectUuid);
    }

//    TODO: Solve this test
//    @Test
//    public void getAllProjectsByCurrentUser_Test() throws BusinessException {
//
//        given(projectRepository.findAllByAppUserId(any())).willReturn(Utils.getProjectEntityList());
//        given(appUserRepository.getAppUserByProject(any())).willReturn(Utils.getAppUserEntityList());
//        given(projectService.getProjectManager(any())).willReturn(Utils.getAppUserDto());
//
//        Set<ProjectDto> result = projectService.getAllProjectsByCurrentUser();
//
//        assertEquals(List.of(Utils.getProjectEntity().get()).size(), result.size());
//    }

    @Test
    public void setActiveEngine_Test() throws BadRequestException {

        given(projectRepository.findByUuid(any())).willReturn(Optional.ofNullable(projectEntity));
        given(projectRepository.save(any())).willReturn(projectEntity);
        given(roleService.getManager()).willReturn(Utils.getRoleEntity(RolesEnum.MANAGER));

        ProjectDto result = projectService.setActiveEngine(projectUuid, Boolean.TRUE);

        assertEquals(result.getIsEngineActive(), fullProjectDto.getIsEngineActive());
    }

    @Test
    public void setActiveEngine_ProjectNotFound_Test() {

        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () ->
                projectService.setActiveEngine(projectUuid, Boolean.TRUE)
        );

        assertEquals(badRequestException.getMessage(), "No project found for id : " + projectUuid);
    }

    @Test
    public void deleteProject_NoProject_Test() {
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () ->
                projectService.deleteProject(projectUuid)
        );

        assertEquals(badRequestException.getMessage(), "No existing project with id : " + projectUuid);
    }



}

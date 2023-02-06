package com.endava.endavibe.service;

import com.endava.endavibe.appUser.AppUserService;
import com.endava.endavibe.appUser.role.RoleEntity;
import com.endava.endavibe.appUser.role.RoleService;
import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.common.util.ValidationUtils;
import com.endava.endavibe.common.util.enums.RolesEnum;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserEntity;
import com.endava.endavibe.appUser.AppUserRepository;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserRepository;
import com.endava.endavibe.project.ProjectRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectAppUserRepository projectAppUserRepository;
    @Mock
    private RoleService roleService;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private AppUserService appUserService;
    @InjectMocks
    private ValidationUtils validationUtils;

    private AppUserEntity appUserEntity;
    private AppUserDto appUserDto;
    private AppUserDto writeAppUserDto;
    private UUID appUserUuid;
    private UUID projectUuid;

    @Before
    public void setup() {
        appUserEntity = Utils.getAppUserEntity();
        appUserDto = Utils.getAppUserDto();
        appUserUuid = Utils.appUserUuid_01;
        projectUuid = Utils.projectUuid;
        writeAppUserDto = Utils.getWriteAppUserDtoFromEntity(appUserEntity);
        appUserDto = Utils.getReadAppUserDtoFromEntity(appUserEntity);
    }

    @Test
    public void getAppUserById_Test() throws BusinessException, IllegalAccessException, BadRequestException {

        given(appUserRepository.findByUuid(any())).willReturn(Optional.of(appUserEntity));
        AppUserDto result = appUserService.getAppUserById(appUserUuid);

        assertEquals(result.getId(), appUserEntity.getId());
    }

    @Test
    public void getAppUserById_NotFound_Test() {

        BusinessException businessException = Assertions.assertThrows(BusinessException.class, () ->
                appUserService.getAppUserById(appUserUuid)
        );

        assertEquals(businessException.getMessage(), "No user was found for id : " + appUserUuid);
    }


    @Test
    public void addAndAssignAppUser_Test() throws BusinessException {

        given(projectRepository.findByUuid(any())).willReturn(Utils.getProjectEntity());
        given(projectAppUserRepository.save(any())).willReturn(new ProjectAppUserEntity());
        given(appUserRepository.findByUuid(any())).willReturn(Optional.ofNullable(appUserEntity));

        AppUserDto result = appUserService.addAndAssignAppUser(writeAppUserDto, projectUuid, Boolean.FALSE);

        assertEquals(result.getUuid(), writeAppUserDto.getUuid());
    }

    @Test
    public void addAndAssignAppUser_NoProject_Test() {

        given(appUserRepository.save(any())).willReturn(appUserEntity);


        BusinessException businessException = Assertions.assertThrows(BusinessException.class, () ->
                appUserService.addAndAssignAppUser(writeAppUserDto, projectUuid, Boolean.FALSE)
        );

        assertEquals(businessException.getMessage(), "No project found for id : " + projectUuid);
    }

    @Test
    public void updateAppUser_Test() throws BusinessException, BadRequestException {

        given(appUserRepository.findByUuid(any())).willReturn(Optional.of(appUserEntity));
        given(appUserRepository.save(any())).willReturn(appUserEntity);
        assertThatCode(() -> validationUtils.validateUser(Utils.email)).doesNotThrowAnyException();


        AppUserDto result = appUserService.updateAppUser(appUserUuid, writeAppUserDto);

        assertEquals(result, writeAppUserDto);
    }

    @Test
    public void updateAppUser_NoUser_Test() {

        BusinessException businessException = Assertions.assertThrows(BusinessException.class, () ->
                appUserService.updateAppUser(appUserUuid, writeAppUserDto)
        );

        assertEquals(businessException.getMessage(), "No team member was found for id : " + appUserUuid);
    }


}

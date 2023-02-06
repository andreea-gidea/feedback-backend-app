package com.endava.endavibe.common.util;

import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.appUser.AppUserRepository;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ValidationUtilsTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private ValidationUtils validationUtils;

    private AppUserEntity appUserEntity;
    private AppUserDto writeAppUserDto;

    @Before
    public void setup() {
        appUserEntity = Utils.getAppUserEntity();
        writeAppUserDto = Utils.getWriteAppUserDtoFromEntity(appUserEntity);
    }

    @Test
    public void addUserValidation_Success_Test() {
        given(appUserRepository.findByEmail(any())).willReturn(null);

        assertThatCode(() -> validationUtils.validateUser(Utils.email)).doesNotThrowAnyException();
    }

    @Test
    public void addUserValidation_Failed_Test() {

        given(appUserRepository.findByEmail(any())).willReturn(appUserEntity);

        BusinessException businessException = Assertions.assertThrows(BusinessException.class, () ->
                validationUtils.validateUser(Utils.email)
        );

        assertEquals(businessException.getMessage(), "A team member with the email " + Utils.getWriteAppUserDtoFromEntity(appUserEntity).getEmail() + " already exists");
    }

    @Test
    public void validateSubscriber_Failed_Test() {
        given(appUserRepository.findByEmail(any())).willReturn(appUserEntity);

        BusinessException businessException = Assertions.assertThrows(BusinessException.class, () ->
                validationUtils.validateSubscriber(Utils.email)
        );

        assertEquals(businessException.getMessage(), "A subscriber with the email " + appUserEntity.getEmail() + " already exists");
    }

    @Test
    public void validateSubscriber_Success_Test() {

        assertThatCode(() -> validationUtils.validateSubscriber(Utils.email)).doesNotThrowAnyException();
    }
}

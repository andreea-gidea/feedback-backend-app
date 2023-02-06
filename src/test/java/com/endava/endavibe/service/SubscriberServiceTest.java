package com.endava.endavibe.service;

import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.subscriber.SubscriberService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.Silent.class)//TODO: make separate test classes for methods that use sendMail
public class SubscriberServiceTest {



    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private SubscriberService subscriberService;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private SubscriberService instanceSubscriberService;

    private AppUserDto subscriberDto;
    private ProjectEntity projectEntity;
    private UUID projectUuid;

    @Before
    public void setup() {
        projectEntity = Utils.getProjectEntity().get();
        projectUuid = Utils.projectUuid;
        subscriberDto = Utils.getAppUserDto();
    }

    @Test
    public void addAndAssignSubscriber_Test() throws Exception {
//        given(subscriberRepository.save(any())).willReturn(Utils.getSubscriberEntity());
        doNothing().when(subscriberService).sendSubscriptionMail(any(),any());

        subscriberService.addAndAssignSubscriber(subscriberDto, projectEntity);
    }


    @Test
    public void updateProjectSubscribers_NoProject_Test() {
        BusinessException businessException = Assertions.assertThrows(BusinessException.class, () ->
                instanceSubscriberService.updateProjectSubscribers(projectUuid, Utils.getAppUserDtoList())
        );

        assertEquals(businessException.getMessage(), "No project found for id : " + projectUuid);
    }
}

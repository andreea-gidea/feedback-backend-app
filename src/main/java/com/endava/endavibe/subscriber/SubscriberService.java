package com.endava.endavibe.subscriber;

import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.appUser.AppUserRepository;
import com.endava.endavibe.appUser.role.RoleService;
import com.endava.endavibe.common.dto.WrapTemplate;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.util.ValidationUtils;
import com.endava.endavibe.mail.MailService;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserEntity;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class SubscriberService {
    @Value("${client.url}")
    private String clientUrl;
    Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    private final ProjectRepository projectRepository;
    private final ProjectAppUserRepository projectAppUserRepository;
    private final AppUserRepository appUserRepository;

    private final RoleService roleService;
    private final MailService mailService;

    public void addAndAssignSubscriber(AppUserDto subscriberDto, ProjectEntity project) throws Exception {
        if (StringUtils.isEmpty(subscriberDto.getFirstName()) || StringUtils.isEmpty(subscriberDto.getLastName()) || StringUtils.isEmpty(subscriberDto.getEmail())) {
            throw new BusinessException("Name, email or both are empty.");
        }

        appUserRepository.findByUuid(subscriberDto.getUuid()).ifPresentOrElse(
                user -> assignSubscriberToProject(project, user),
                () -> assignSubscriberToProject(project, addSubscriber(subscriberDto)));

        sendSubscriptionMail(subscriberDto, project);
    }

    public void sendSubscriptionMail(AppUserDto subscriberDto, ProjectEntity project) throws Exception {

        WrapTemplate projectTemplate = new WrapTemplate("project", project);
        WrapTemplate templateClientUrl = new WrapTemplate("clientUrl", clientUrl);
        WrapTemplate subscriberTemplate = new WrapTemplate("subscriber", subscriberDto);

        String template = mailService.generateTemplate("subscriber_template.ftl", projectTemplate, subscriberTemplate, templateClientUrl);

        //mailService.sendHtmlMessage(List.of(subscriberDto.getEmail()), "New subscription for project " + project.getName(), template);
    }

    public AppUserEntity addSubscriber(AppUserDto subscriberDto) {
        ValidationUtils.validateSubscriber(subscriberDto.getEmail());
        AppUserEntity appUserEntity = appUserRepository.save(AppUserEntity.builder().uuid(subscriberDto.getUuid()).firstName(subscriberDto.getFirstName())
                .lastName(subscriberDto.getLastName()).email(subscriberDto.getEmail()).insDate(LocalDateTime.now()).build());

        logger.info("Subscriber with id " + appUserEntity.getUuid() + " have been saved");

        return appUserEntity;
    }

    public void assignSubscriberToProject(ProjectEntity project, AppUserEntity subscriberEntity) {

            if (CollectionUtils.isNotEmpty(projectAppUserRepository.findAllByProjectAndRole(project, roleService.getSubscriber()))) {

                projectAppUserRepository.findAllByProjectAndRole(project, roleService.getSubscriber()).stream()
                        .filter(pa -> pa.getAppUser().equals(subscriberEntity)).findFirst().ifPresent(pa -> {
                throw new BusinessException("Subscriber " + subscriberEntity.getUuid() + " is already assigned to project " + project.getUuid());
            });
        }

        projectAppUserRepository.save(ProjectAppUserEntity.builder().project(project).appUser(subscriberEntity).role(roleService.getSubscriber()).insDate(LocalDateTime.now()).build());

        logger.info("Subscriber with id " + subscriberEntity.getUuid() + " has been assigned to project " + project.getUuid());

    }

    public List<AppUserDto> updateProjectSubscribers(UUID projectUuid, List<AppUserDto> subscriberDtoList) throws Exception {

        ProjectEntity projectEntity = projectRepository.findByUuid(projectUuid).orElseThrow(() -> {
            throw new BusinessException("No project found for id : " + projectUuid);
        });

        List<UUID> existingSubscriberUuidList = projectAppUserRepository
                .findAllByProjectAndRole(projectEntity , roleService.getSubscriber())
                .stream()
                .map(ProjectAppUserEntity::getAppUser)
                .map(AppUserEntity::getUuid)
                .toList();

        if (CollectionUtils.isNotEmpty(subscriberDtoList)) {
            List<UUID> newSubscriberUuidList = subscriberDtoList
                    .stream()
                    .map(AppUserDto::getUuid)
                    .toList();

            for (int i = 0; i < newSubscriberUuidList.size(); i++) {
                if (!existingSubscriberUuidList.contains(newSubscriberUuidList.get(i))) {
                    addAndAssignSubscriber(subscriberDtoList.get(i), projectEntity);
                }
            }

            existingSubscriberUuidList.forEach(subscriberUuid -> {
                if (!newSubscriberUuidList.contains(subscriberUuid)) {
                    projectAppUserRepository.deleteByProjectUuidAndAppUserUuidAndRole(projectUuid, subscriberUuid, roleService.getSubscriber());
                }
            });
        } else {
            projectAppUserRepository.deleteByProjectUuidAndAppUserUuidInAndRole(projectUuid, existingSubscriberUuidList, roleService.getSubscriber());
        }

        return projectAppUserRepository.findAllByProjectAndRole(projectEntity, roleService.getSubscriber())
                .stream()
                .map(ProjectAppUserEntity::getAppUser)
                .map(AppUserEntity::toDto)
                .toList();
    }

    public void unsubscribe(UUID projectUuid, UUID subscriberUuid) {

        AppUserEntity projectSubscriber = appUserRepository.findById(projectAppUserRepository
                        .findByProjectUuidAndAppUserUuidAndRole(projectUuid, subscriberUuid, roleService.getSubscriber()).getId())
                .orElseThrow(() -> {
                    throw new BusinessException("No app user found for id : " + subscriberUuid);
                });

        if (ObjectUtils.isEmpty(projectSubscriber)) {
            throw new BusinessException("You already unsubscribed form this project");
        }

        projectAppUserRepository.deleteByProjectUuidAndAppUserUuidAndRole(projectUuid, subscriberUuid, roleService.getSubscriber());

    }
}

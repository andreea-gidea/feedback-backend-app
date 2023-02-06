package com.endava.endavibe.project;


import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.appUser.AppUserRepository;
import com.endava.endavibe.appUser.AppUserService;
import com.endava.endavibe.appUser.role.RoleEntity;
import com.endava.endavibe.appUser.role.RoleService;
import com.endava.endavibe.common.dto.ProjectDto;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserEntity;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserRepository;
import com.endava.endavibe.project.projectQuiz.ProjectQuizRepository;
import com.endava.endavibe.subscriber.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Transactional
@Service
@RequiredArgsConstructor
public class ProjectService {

    Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private final RoleService roleService;
    private final ProjectRepository projectRepository;
    private final AppUserRepository appUserRepository;
    private final ProjectAppUserRepository projectAppUserRepository;
    private final ProjectQuizRepository projectQuizRepository;
    private final AppUserService appUserService;
    private final SubscriberService subscriberService;


    public ProjectDto createProject(ProjectDto createProjectDto) throws Exception {

        ProjectEntity newProject = projectRepository.save(ProjectEntity.builder()
                .name(createProjectDto.getName())
                .insDate(LocalDateTime.now())
                .isEngineActive(createProjectDto.getIsEngineActive())
                .build());

        logger.info("Project with id " + newProject.getUuid() + "have been created");

        AppUserDto projectOwner = appUserService.getLoggedAppUserDto();
        appUserService.addAndAssignAppUser(projectOwner, newProject.getUuid(), roleService.getManager());

        List<AppUserDto> teamMemberList = createProjectDto.getListOfTeamMembers();
        if (CollectionUtils.isNotEmpty(teamMemberList)) {
            for (AppUserDto user : teamMemberList) {
                appUserService.addAndAssignAppUser(user, newProject.getUuid(), roleService.getTeamMember());
            }
        }

        List<AppUserDto> subscriberDtoList = createProjectDto.getListOfSubscribers();
        if (CollectionUtils.isNotEmpty(subscriberDtoList)) {
            for (AppUserDto subscriber : subscriberDtoList) {
                subscriberService.addAndAssignSubscriber(subscriber, newProject);
            }
        }

        return createProjectDtoFromEntity(newProject);
    }


    public ProjectDto updateProject(UUID projectUuid, ProjectDto projectDto) throws BusinessException {

        ProjectEntity projectEntity = projectRepository.findByUuid(projectUuid).orElseThrow(() -> {
            throw new BusinessException("No project found for id : " + projectUuid);
        });

        if (!projectDto.getName().equals(projectEntity.getName())) {
            projectRepository.save(projectEntity);
            projectEntity.setName(projectDto.getName());
        }

        List<AppUserDto> newTeamMembersDtoList = projectDto.getListOfTeamMembers();
        if (CollectionUtils.isNotEmpty(newTeamMembersDtoList)) {
            List<UUID> newTeamMemberList = newTeamMembersDtoList.stream().map(AppUserDto::getUuid).toList();
            List<UUID> existingTeamMemberUuidList = appUserRepository.getAppUserByProject(projectUuid).stream().map(AppUserEntity::getUuid).toList();

            for (int i = 0; i < newTeamMemberList.size(); i++) {
                if (!existingTeamMemberUuidList.contains(newTeamMemberList.get(i))) {
                    appUserService.addAndAssignAppUser(newTeamMembersDtoList.get(i), projectUuid, Boolean.FALSE);
                }
            }

            existingTeamMemberUuidList.forEach(userUuid -> {
                if (!newTeamMemberList.contains(userUuid) && !appUserRepository.getOwnerUuidByProjectId(projectEntity.getId(), roleService.getManager()).equals(userUuid)) {
                    projectAppUserRepository.deleteByProjectUuidAndAppUserUuid(projectUuid, userUuid);
                }
            });
        }

        return createProjectDtoFromEntity(projectEntity);
    }


    public void deleteProject(UUID uuid) throws BadRequestException {
        ProjectEntity projectToDelete = projectRepository.findByUuid(uuid)
                .orElseThrow(() -> new BadRequestException("No existing project with id : " + uuid));

        projectQuizRepository.deleteAll(projectToDelete.getProjectQuizList());
        logger.info("Quizzes related to project " + uuid + " have been unassigned");

        projectAppUserRepository.deleteAll(projectToDelete.getProjectAppUserList());
        logger.info("Team members and subscribers from project " + uuid + " have been unassigned");

        projectRepository.delete(projectToDelete);
        logger.info("Project " + uuid + " have been deleted");
    }

    public ProjectDto setActiveEngine(UUID projectUuid, boolean isActive) throws BadRequestException {
        ProjectEntity project = projectRepository.findByUuid(projectUuid)
                .orElseThrow(() -> new BadRequestException("No project found for id : " + projectUuid));
        project.setIsEngineActive(isActive);
        logger.info("Active engine have been set " + isActive + " for project " + projectUuid);

        return createProjectDtoFromEntity(projectRepository.save(project));
    }


    public ProjectDto createProjectDtoFromEntity(ProjectEntity projectEntity) {
        ProjectDto detailsProjectDto = new ProjectDto();
        detailsProjectDto.setId(projectEntity.getId());
        detailsProjectDto.setUuid(projectEntity.getUuid());
        detailsProjectDto.setName(projectEntity.getName());
        detailsProjectDto.setIsEngineActive(projectEntity.getIsEngineActive());
        detailsProjectDto.setOwnerUuid(getOwnerUuid(projectEntity));
        detailsProjectDto.setListOfTeamMembers(this.getProjectTeamMembers(projectEntity));
        detailsProjectDto.setListOfSubscribers(this.getProjectSubscribers(projectEntity));
        return detailsProjectDto;
    }

    public UUID getOwnerUuid(ProjectEntity projectEntity) {
        Collection<ProjectAppUserEntity> assignations = projectEntity.getProjectAppUserList();

        if (CollectionUtils.isNotEmpty(assignations)) {
            Optional<ProjectAppUserEntity> ownerAssignation = assignations.stream()
                    .filter(as -> roleService.getManager().equals(as.getRole()))
                    .findAny();
            if (ownerAssignation.isPresent()) {
                return ownerAssignation.get().getAppUser().getUuid();
            }
        }
        return null;
    }

    public Set<ProjectDto> getAllProjectsByCurrentUser() {
        AppUserEntity appUserEntity = (AppUserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ProjectEntity> projects = projectRepository.findAllByAppUserId(appUserEntity.getId());
        Set<ProjectDto> projectDtoList = new TreeSet<>();

        if (CollectionUtils.isNotEmpty(projects)) {
            projectDtoList = projects.stream().map(ProjectEntity::toDto).collect(Collectors.toCollection(TreeSet::new));
            projectDtoList.forEach(project -> {
                boolean isEngineActive = BooleanUtils.isTrue(project.getIsEngineActive());

                project.setListOfTeamMembers(this.getProjectTeamMembers(project));
                project.setListOfSubscribers(this.getProjectSubscribers(project));
                project.setOwnerUuid(this.getProjectManager(project).getUuid());

                try {
                    project.add(linkTo(methodOn(ProjectController.class).updateProject(String.valueOf(project.getUuid()), null)).withRel("update"))
                            .add(linkTo(methodOn(ProjectController.class).deleteProjectById(String.valueOf(project.getUuid()))).withRel("delete"))
                            .add(linkTo(methodOn(ProjectController.class).activeEngine(String.valueOf(project.getUuid()), isEngineActive ? Boolean.FALSE : Boolean.TRUE))
                                    .withRel(isEngineActive ? "deactivate" : "activate"));
                } catch (IllegalAccessException | BadRequestException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return projectDtoList;
    }

    public List<AppUserDto> getProjectAppUsersByRole (ProjectEntity projectEntity, RoleEntity role) {
        List<ProjectAppUserEntity> allByProjectAndRole = projectAppUserRepository.findAllByProjectAndRole(projectEntity, role);
        return allByProjectAndRole.stream()
                .map(ProjectAppUserEntity::getAppUser)
                .map(AppUserEntity::toDto)
                .toList();
    }

    public List<AppUserDto> getProjectTeamMembers(ProjectEntity projectEntity) {
        return this.getProjectAppUsersByRole(projectEntity, roleService.getTeamMember());
    }

    public List<AppUserDto> getProjectTeamMembers(ProjectDto projectDto) {
        return this.getProjectTeamMembers(projectDto.toEntity());
    }

    public  List<AppUserDto> getProjectSubscribers(ProjectEntity projectEntity) {
        return this.getProjectAppUsersByRole(projectEntity, roleService.getSubscriber());
    }

    public  List<AppUserDto> getProjectSubscribers(ProjectDto projectDto) {
        return this.getProjectSubscribers(projectDto.toEntity());
    }

    public AppUserDto getProjectManager(ProjectDto projectDto){
        ProjectEntity projectEntity = projectDto.toEntity();
        /* On each project is a single Manager */
        return this.getProjectAppUsersByRole(projectEntity, roleService.getManager())
                .stream()
                /* We reuse getProjectAppUsersByRole() method and take the only return AppUser if this exist */
                .findFirst()
                .orElseThrow(() -> new BusinessException("No Manager found for the project " + projectEntity));
    }

}

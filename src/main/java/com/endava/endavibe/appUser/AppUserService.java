package com.endava.endavibe.appUser;

import com.endava.endavibe.appUser.role.PrivilegeEntity;
import com.endava.endavibe.appUser.role.RoleEntity;
import com.endava.endavibe.appUser.role.RoleRepository;
import com.endava.endavibe.appUser.role.RoleService;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.dto.user.AppUserWithAccessDto;
import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.util.ValidationUtils;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserEntity;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class AppUserService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(AppUserService.class);
    private final RoleService roleService;
    private final AppUserRepository appUserRepository;
    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;

    private final ProjectAppUserRepository projectAppUserRepository;
    private final ModelMapper modelMapper;

    public AppUserDto getAppUserById(UUID id) throws BusinessException, IllegalAccessException, BadRequestException {
        AppUserEntity user = appUserRepository.findByUuid(id).orElseThrow(() -> {
            throw new BusinessException("No user was found for id : " + id);
        });
        AppUserDto appUserDto = modelMapper.map(user, AppUserDto.class);
        appUserDto.add(linkTo(methodOn(AppUserController.class).updateAppUser(String.valueOf(user.getUuid()), new AppUserDto())).withRel("update"))
                .add(linkTo(methodOn(AppUserController.class).deleteAppUser(String.valueOf(user.getUuid()))).withRel("delete"));

        return appUserDto;
    }


    public AppUserDto addAndAssignAppUser(AppUserDto appUserDto, UUID projectUuid, boolean isProjectOwner) throws BusinessException {
        if (StringUtils.isEmpty(appUserDto.getFirstName()) || StringUtils.isEmpty(appUserDto.getLastName()) || StringUtils.isEmpty(appUserDto.getEmail())) {
            throw new BusinessException("Name, email or both are empty.");
        }

        RoleEntity role = (isProjectOwner) ? (roleService.getManager()) : (roleService.getTeamMember());

        appUserRepository.findByUuid(appUserDto.getUuid()).ifPresentOrElse(
                user -> assignAppUserToProject(user, projectUuid, role),
                () -> assignAppUserToProject(addAppUser(appUserDto), projectUuid, role));

        return modelMapper.map(appUserRepository.findByUuid(appUserDto.getUuid()).orElseThrow(() -> {
            throw new BusinessException("No existing team member with id : " + appUserDto.getUuid());
        }), AppUserDto.class);
    }

    public void addAndAssignAppUser(AppUserDto appUserDto, UUID projectUuid, RoleEntity roleEntity) throws BusinessException {
        if (StringUtils.isEmpty(appUserDto.getFirstName()) || StringUtils.isEmpty(appUserDto.getLastName()) || StringUtils.isEmpty(appUserDto.getEmail())) {
            throw new BusinessException("Name, email or both are empty.");
        }

        appUserRepository.findByUuid(appUserDto.getUuid()).ifPresentOrElse(
                user -> assignAppUserToProject(user, projectUuid, roleEntity),
                () -> assignAppUserToProject(addAppUser(appUserDto), projectUuid, roleEntity));

    }

    public AppUserEntity addAppUser(AppUserDto appUserDto) throws BusinessException {
        ValidationUtils.validateUser(appUserDto.getEmail());

        AppUserEntity userEntity = AppUserEntity.builder()
                .uuid(appUserDto.getUuid())
                .firstName(appUserDto.getFirstName())
                .lastName(appUserDto.getLastName())
                .email(appUserDto.getEmail())
                .insDate(LocalDateTime.now())
                .build();

        AppUserEntity savedAppUser = appUserRepository.save(userEntity);
        logger.info("App user has been saved with id : " + savedAppUser.getUuid());

        return savedAppUser;
    }


    public void assignAppUserToProject(AppUserEntity appUserEntity, UUID projectUuid, RoleEntity roleEntity) throws BusinessException {
        ProjectAppUserEntity existingProjectAppUserEntity = projectAppUserRepository.findByAppUserUuidAndProjectUuid(appUserEntity.getUuid(), projectUuid);

        if (ObjectUtils.isEmpty(existingProjectAppUserEntity)) {

            ProjectEntity project = projectRepository.findByUuid(projectUuid).orElseThrow(() -> {
                throw new BusinessException("No project found for id : " + projectUuid);
            });

            if (CollectionUtils.isNotEmpty(project.getProjectAppUserList())) {
                project.getProjectAppUserList().stream().filter(pa -> pa.getAppUser().equals(appUserEntity)).findFirst().ifPresent(pa -> {
                    throw new BusinessException("Team member " + appUserEntity.getUuid() + " is already assigned to project " + projectUuid);
                });
            }

            ProjectAppUserEntity projectAppUserEntity = new ProjectAppUserEntity();

            projectAppUserEntity.setAppUser(appUserEntity);
            projectAppUserEntity.setProject(project);
            projectAppUserEntity.setRole(roleEntity);
            projectAppUserEntity.setInsDate(LocalDateTime.now());

            projectAppUserRepository.save(projectAppUserEntity);

            logger.info("Team member with id : " + appUserEntity.getUuid() + " have been assigned to project " + projectUuid);
        }
    }
    public AppUserDto updateAppUser(UUID uuid, AppUserDto appUserDto) throws BusinessException{
        logger.info("Modify a user has stated");

        AppUserEntity user;
        user = appUserRepository.findByUuid(uuid).orElseThrow(() -> {
            throw new BusinessException("No user found for uuid : " + uuid);
        });

        user.setFirstName(appUserDto.getFirstName());
        user.setLastName(appUserDto.getLastName());
        user.setEmail(appUserDto.getEmail());


        TreeSet<RoleEntity> newRoles = appUserDto.getRoles().stream()
                .map(roleDto -> roleRepository.findByName(roleDto.getName()))
                .sorted(Comparator.comparing(RoleEntity::getName))
                .collect(Collectors.toCollection(TreeSet::new));
        TreeSet<RoleEntity> existingRoles = user
                .getRoles().stream()
                .sorted(Comparator.comparing(RoleEntity::getName))
                .collect(Collectors.toCollection(TreeSet::new));

        for (RoleEntity newRole : newRoles) {
            if (!existingRoles.contains(newRole)) {
                saveAppUserRoles(user, newRole);
            }
        }

        for (RoleEntity existingRole : existingRoles) {
            if (!newRoles.contains(existingRole)) {
                if (roleService.getAdmin().equals(existingRole)) {
                    if(projectAppUserRepository.countAllByRole(existingRole)==1){
                        throw new BusinessException("Cannot delete user " + user.getUuid() + " as Admin before creating a different admin");
                    }
                    projectAppUserRepository.deleteByAppUserUuidAndRole(user.getUuid(), existingRole);
                } else if (roleService.getManager().equals(existingRole)) {
                    if (projectAppUserRepository.countByAppUserUuidAndRole(user.getId(), existingRole.getId()) > 0) {
                        throw new BusinessException("Manager with uuid " + user.getUuid() + " is manager to at least one project. It cannot be deleted");

                    } else {
                        projectAppUserRepository.deleteByAppUserUuidAndRole(user.getUuid(), existingRole);
                    }
                }
            }
        }

        return modelMapper.map(user, AppUserDto.class);
    }



    public AppUserDto getLoggedAppUserDto(){
        return  modelMapper.map(SecurityContextHolder.getContext().getAuthentication().getPrincipal(), AppUserDto.class);
    }

    public AppUserEntity getLoggedAppUserEntity() {

        AppUserEntity appUserEntity = (AppUserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return appUserRepository.findByUuid(appUserEntity.getUuid()).orElseThrow(() -> {
            throw new BusinessException("No existing team member with id : " + appUserEntity.getUuid());
        });
    }

    public Optional<AppUserEntity> findByUuid(AppUserEntity appUserEntity) {
        return appUserRepository.findByUuid(appUserEntity.getUuid());
    }

    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {

        Optional<AppUserEntity> optUser = appUserRepository.findByUuid(UUID.fromString(uuid));
        if (optUser.isEmpty()) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Collections.singletonList(
                            roleService.getDefault())));
        }

        AppUserEntity user = optUser.get();
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), "mock_password", /* user.isEnabled() */ true, true, true,
                true, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<RoleEntity> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<RoleEntity> roles) {
        List<String> privileges = new ArrayList<>();
        List<PrivilegeEntity> collection = new ArrayList<>();
        for (RoleEntity role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (PrivilegeEntity item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    public AppUserDto saveAdminOrPM(AppUserDto appUser) throws BusinessException, BadRequestException {

        RoleEntity roleToBeAssigned;

        if (appUser.getRoles().size() == 1) {
            roleToBeAssigned = roleRepository.findByName(appUser.getRoles().iterator().next().getName());
        } else
            throw new BadRequestException("Roles not correct");

        ValidationUtils.validateAdminOrPm(appUser.getEmail(), roleToBeAssigned);

        AppUserEntity user = appUserRepository.findByEmail(appUser.getEmail());

        if (ObjectUtils.isEmpty(user)) {
            user = addAppUser(appUser);
        }

        saveAppUserRoles(user, roleToBeAssigned);
        logger.info("App user with uuid " +user.getUuid() + " has been saved as a " + roleToBeAssigned.getName());

        return modelMapper.map(appUser, AppUserDto.class);
    }

    private void saveAppUserRoles(AppUserEntity userEntity, RoleEntity role) {
        ProjectAppUserEntity projectAppUser = ProjectAppUserEntity.builder()
                .appUser(userEntity)
                .role(roleRepository.findByName(role.getName()))
                .insDate(LocalDateTime.now())
                .build();
        projectAppUserRepository.save(projectAppUser);
    }

    public List<AppUserWithAccessDto> getAppUsersWithAccess() {

        Set<AppUserWithAccessDto> appUserWithAccessDtos = new TreeSet<>();

        List<AppUserWithAccessDto> projectOwners = getUsersWithAccessByRole(roleService.getManager());

        List<AppUserWithAccessDto> projectSubscribers = getUsersWithAccessByRole(roleService.getSubscriber());

        List<AppUserWithAccessDto> admins = getUsersWithAccessByRole(roleService.getAdmin());

        appUserWithAccessDtos.addAll(projectOwners);
        appUserWithAccessDtos.addAll(projectSubscribers);
        appUserWithAccessDtos.addAll(admins);


        return appUserWithAccessDtos.stream()
                .peek(appUserWithAccessDto -> appUserWithAccessDto
                        .setNrOfProjects(projectAppUserRepository.countByAppUserUuidAndRole(appUserWithAccessDto.getId(), roleService.getManager().getId())))
                .peek(appUserWithAccessDto -> appUserWithAccessDto
                        .setNrOfSubscriptions(projectAppUserRepository.countByAppUserUuidAndRole(appUserWithAccessDto.getId(), roleService.getSubscriber().getId())))
                .sorted()
                .toList();

    }

    private List<AppUserWithAccessDto> getUsersWithAccessByRole(RoleEntity roleService) {
        return projectAppUserRepository.findByRole(roleService).stream()
                .map(projectAppUserEntity -> getAppUserEntity(projectAppUserEntity.getAppUser().getId()))
                .map(appUserEntity -> modelMapper.map(appUserEntity, AppUserWithAccessDto.class))
                .toList();
    }

    private AppUserEntity getAppUserEntity(Long AppUserId) {
        return appUserRepository.findById(AppUserId)
                .orElseThrow(() -> new BusinessException("No app user for given Id "));
    }
    @Transactional
    public void deleteAppUserWithAccess(UUID uuid) throws BusinessException {

        AppUserEntity appUserEntity = appUserRepository.findByUuid(uuid).orElseThrow(() -> {
            throw new BusinessException("No existing team member with id : " + uuid);
        });
        if (appUserEntity.getRoles().contains(roleService.getAdmin())){
            projectAppUserRepository.deleteByAppUserUuidAndRole(appUserEntity.getUuid(),roleService.getAdmin());
        }

        if (projectAppUserRepository.countByAppUserUuidAndRole(appUserEntity.getId() ,roleService.getManager().getId()) == 0){
            projectAppUserRepository.deleteByAppUserUuidAndRole(appUserEntity.getUuid(),roleService.getManager());
        }
        else
            throw new BusinessException("User cannot be deleted because of assigned projects");

    }

    public void save(AppUserEntity updated) {
        appUserRepository.save(updated);
    }

    public int getNumberOfAppUsers() {

        return appUserRepository.countAppUserEntities();

    }

    public AppUserDto addFirstUser(AppUserDto appUser) {

        RoleEntity roleToBeAssigned = roleService.getSubscriber();

        ValidationUtils.validateAdminOrPm(appUser.getEmail(), roleToBeAssigned);

        AppUserEntity user = addAppUser(appUser);

        saveAppUserRoles(user, roleToBeAssigned);
        logger.info("App user with uuid " +user.getUuid() + " has been saved as a " + roleToBeAssigned.getName());

        return modelMapper.map(appUser, AppUserDto.class);    }
}

package com.endava.endavibe.project.projectAppUser;

import com.endava.endavibe.appUser.role.RoleEntity;
import com.endava.endavibe.project.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectAppUserRepository extends JpaRepository<ProjectAppUserEntity, Long> {

    ProjectAppUserEntity findByAppUserUuidAndProjectUuid(UUID userUuid, UUID projectUuid);
    List<ProjectAppUserEntity> findByRole(RoleEntity role);

    @Query(nativeQuery = true, value = "SELECT  COUNT(*) FROM project_app_user " +
            "WHERE app_user_id = :userId AND role_id = :roleId AND project_id IS NOT NULL")
    Integer countByAppUserUuidAndRole(Long userId, Long roleId);

    void deleteByProjectUuidAndAppUserUuid(UUID projectUuid, UUID appUserUuid);

    List<ProjectAppUserEntity> findAllByProjectAndRole(ProjectEntity projectEntity, RoleEntity roleEntity);
    ProjectAppUserEntity findByProjectUuidAndAppUserUuidAndRole(UUID projectUuid, UUID appUserUuid, RoleEntity roleEntity);
    void deleteByProjectUuidAndAppUserUuidAndRole(UUID projectUuid, UUID appUserUuid, RoleEntity roleEntity);
    void deleteByProjectUuidAndAppUserUuidInAndRole(UUID projectUuid, List<UUID> appUserUuids, RoleEntity roleEntity);
    List<ProjectAppUserEntity> findByAppUserUuidAndRole(UUID appUserUuid, RoleEntity roleEntity);
    void deleteByAppUserUuidAndRole(UUID appUserUuid,RoleEntity roleEntity);
    Integer countAllByRole(RoleEntity roleEntity);

}

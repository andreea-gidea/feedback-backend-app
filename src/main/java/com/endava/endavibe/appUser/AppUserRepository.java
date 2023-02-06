package com.endava.endavibe.appUser;

import com.endava.endavibe.appUser.role.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {

    AppUserEntity findByEmail(String email);

    Optional<AppUserEntity> findByUuid(UUID uuid);

    @Query(value = " select au from AppUserEntity as au, ProjectAppUserEntity as pau where au.id = pau.appUser.id and pau.project.uuid = ?1")
    List<AppUserEntity> getAppUserByProject(UUID projectUuid);

    @Query(value = " select au.uuid from AppUserEntity as au, ProjectAppUserEntity as pau where pau.appUser.id = au.id and pau.project.id = :projectId and pau.role = :role")
    UUID getOwnerUuidByProjectId(Long projectId, RoleEntity role);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM app_user")
    int countAppUserEntities();

}

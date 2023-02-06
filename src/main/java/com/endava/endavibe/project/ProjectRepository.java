package com.endava.endavibe.project;

import com.endava.endavibe.project.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByUuid(UUID uuid);

    @Query(value = " select p from ProjectEntity p where p.isEngineActive = ?1")
    List<ProjectEntity> findProjectsByIsActive(Boolean isEngineActive);

    @Query(value = "SELECT prj FROM ProjectEntity AS prj " +
            "INNER JOIN ProjectAppUserEntity as prjau " +
            "ON  prjau.project.id = prj.id " +
            "WHERE prjau.appUser.id = :id")
    List<ProjectEntity> findAllByAppUserId(Long id);


}

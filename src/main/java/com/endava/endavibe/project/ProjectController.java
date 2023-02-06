package com.endava.endavibe.project;

import com.endava.endavibe.common.dto.ProjectDto;
import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.util.ValidationUtils;
import com.endava.endavibe.log.questionLog.QuestionLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/project")
public class ProjectController {


    private final ProjectService projectService;
    private final QuestionLogService questionLogService;

    @Operation(summary = "Create a project given a name")
    @PostMapping()
    public ResponseEntity<?> createProject(@RequestBody ProjectDto createProjectDto) throws Exception {
        if (!ValidationUtils.isValidRequestBody(createProjectDto)) {
            throw new BadRequestException("The received object is not correct");
        }

        ProjectDto projectDto = projectService.createProject(createProjectDto);
        return new ResponseEntity<>(projectDto, HttpStatus.CREATED);
    }


    @Operation(summary = "Update project name by id")
    @PatchMapping("/{uuid}")
    public ResponseEntity<?> updateProject(@PathVariable("uuid") String uuid, @RequestBody ProjectDto projectDetailToUpdate) throws IllegalAccessException, BadRequestException {
        if (!ValidationUtils.isValidRequestBody(projectDetailToUpdate)) {
            throw new BadRequestException("The received object is not correct");
        }

        ProjectDto project = projectService.updateProject(UUID.fromString(uuid), projectDetailToUpdate);
        HttpStatus status = ObjectUtils.isNotEmpty(project) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(project, status);
    }

    @Operation(summary = "Get a list of all projects")
    @GetMapping()
    public ResponseEntity<?> getAllProjects() {
        Set<ProjectDto> projects = projectService.getAllProjectsByCurrentUser();
        HttpStatus status = ObjectUtils.isNotEmpty(projects) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(projects, status);
    }

    @Operation(summary = "Delete a project by id")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteProjectById(@PathVariable("uuid") String uuid) throws BadRequestException {

        if (StringUtils.isEmpty(uuid)) {
            throw new BadRequestException("The received uuid is null");
        }
        projectService.deleteProject(UUID.fromString(uuid));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Activate/Deactivate engine by project id")
    @PatchMapping("/{uuid}/activeEngine")
    public ResponseEntity<?> activeEngine(@PathVariable(value = "uuid") String projectUuid, @RequestParam boolean isActive) throws BadRequestException {
        ProjectDto projectDto = projectService.setActiveEngine(UUID.fromString(projectUuid), isActive);
        HttpStatus status = ObjectUtils.isNotEmpty(projectDto) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        /*  The setup for the questions' recommendation algorithm */
        questionLogService.initProjectQuestionLogs(projectDto.getId());

        return new ResponseEntity<>(projectDto, status);
    }


}

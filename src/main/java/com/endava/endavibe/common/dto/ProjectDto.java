package com.endava.endavibe.common.dto;

import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.project.ProjectEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDto extends RepresentationModel<ProjectDto> implements Comparable <ProjectDto>{
    @JsonIgnore
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID ownerUuid;

    private String name;
    private Boolean isEngineActive;
    private List<AppUserDto> listOfTeamMembers;
    private List<AppUserDto> listOfSubscribers;

    public ProjectEntity toEntity() {
        return new ModelMapper().map(this, ProjectEntity.class);
    }


    @Override
    public int compareTo(ProjectDto o) {
        return this.uuid.compareTo(o.uuid);
    }

}

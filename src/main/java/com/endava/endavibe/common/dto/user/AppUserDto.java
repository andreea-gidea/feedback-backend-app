package com.endava.endavibe.common.dto.user;

import com.endava.endavibe.appUser.AppUserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUserDto extends RepresentationModel<AppUserDto> {

    @JsonIgnore
    private Long id;

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
    private Set<RoleDto> roles;

    public AppUserEntity toEntity() {
        return new ModelMapper().map(this, AppUserEntity.class);
    }
}

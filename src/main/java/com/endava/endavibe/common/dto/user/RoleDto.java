package com.endava.endavibe.common.dto.user;

import com.endava.endavibe.appUser.role.RoleEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {
    @JsonIgnore
    private Long id;
    private String name;

    public RoleEntity toEntity() {
        return new ModelMapper().map(this, RoleEntity.class);
    }
}

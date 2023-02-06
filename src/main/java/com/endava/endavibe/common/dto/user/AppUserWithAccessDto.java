package com.endava.endavibe.common.dto.user;

import com.endava.endavibe.appUser.role.RoleEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
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
public class AppUserWithAccessDto implements Comparable <AppUserWithAccessDto>{

    @JsonIgnore
    private Long id;

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
    private Integer nrOfProjects;
    private Integer nrOfSubscriptions;
    private Set<RoleEntity> roles;



    @Override
    public int compareTo(AppUserWithAccessDto o) {
        return Integer.compare(this.email.compareTo(o.email), 0);
    }
}

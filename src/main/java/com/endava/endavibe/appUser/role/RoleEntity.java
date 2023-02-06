package com.endava.endavibe.appUser.role;

import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.common.dto.ProjectDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "role", schema = "public", catalog = "endavibe")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoleEntity implements Serializable, Comparable<RoleEntity> {

    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<AppUserEntity> users;

    @ManyToMany()
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<PrivilegeEntity> privileges;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int compareTo(RoleEntity o) {
        return this.getName().compareTo(o.getName());
    }


}

package com.endava.endavibe.appUser;

import com.endava.endavibe.appUser.role.RoleEntity;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "app_user", schema = "public", catalog = "endavibe")
public class AppUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    public AppUserEntity(UUID uuid, String firstName, String lastName, String email) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid")
    private UUID uuid;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;


    @Column(name = "ins_date", nullable = false)
    private LocalDateTime insDate = LocalDateTime.now();

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<ProjectAppUserEntity> projectAppUserList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "project_app_user",
            joinColumns = @JoinColumn(
                    name = "app_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;

    public AppUserDto toDto() {
        return new ModelMapper().map(this, AppUserDto.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppUserEntity that = (AppUserEntity) o;

        if (!uuid.equals(that.uuid)) return false;
        if (!firstName.equals(that.firstName)) return false;
        if (!lastName.equals(that.lastName)) return false;
        return email.equals(that.email);
    }
}

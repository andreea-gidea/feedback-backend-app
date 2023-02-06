package com.endava.endavibe.project.projectAppUser;

import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.appUser.role.RoleEntity;
import com.endava.endavibe.project.ProjectEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "project_app_user", schema = "public", catalog = "endavibe")
public class ProjectAppUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUserEntity appUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", referencedColumnName = "id")
    private RoleEntity role;


    @Column(name = "ins_date", nullable = false)
    private LocalDateTime insDate;


}

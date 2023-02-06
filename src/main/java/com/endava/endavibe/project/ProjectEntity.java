package com.endava.endavibe.project;

import com.endava.endavibe.common.dto.ProjectDto;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserEntity;
import com.endava.endavibe.project.projectQuiz.ProjectQuizEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
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
@Table(name = "project", schema = "public", catalog = "endavibe")
public class ProjectEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid")
    private UUID uuid;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_engine_active", nullable = false)
    private Boolean isEngineActive;

    @Column(name = "send_date")
    private LocalDateTime sendDate;

    @Column(name = "next_send_date")
    private LocalDateTime nextSendDate;

    @Column(name = "ins_date", nullable = false)
    private LocalDateTime insDate;


    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<ProjectAppUserEntity> projectAppUserList;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<ProjectQuizEntity> projectQuizList;

    @PrePersist
    protected void onCreate() {
        setUuid(java.util.UUID.randomUUID());
    }

    public ProjectDto toDto() {
        return new ModelMapper().map(this, ProjectDto.class);
    }
}

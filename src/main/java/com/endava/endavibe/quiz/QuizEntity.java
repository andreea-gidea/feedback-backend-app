package com.endava.endavibe.quiz;

import com.endava.endavibe.project.projectQuiz.ProjectQuizEntity;
import com.endava.endavibe.quiz.questionQuiz.QuestionQuizEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "quiz", schema = "public", catalog = "endavibe")
public class QuizEntity implements Serializable {

    private static final long serialVersionUID = 1945670924947820279L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "ins_date", nullable = false)
    private LocalDateTime insDate;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<ProjectQuizEntity> projectQuizList;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<QuestionQuizEntity> questionQuizList;

    @PrePersist
    protected void onCreate() {
        setUuid(java.util.UUID.randomUUID());
    }
}

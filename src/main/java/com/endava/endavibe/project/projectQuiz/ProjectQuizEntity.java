package com.endava.endavibe.project.projectQuiz;

import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.quiz.QuizEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "project_quiz", schema = "public", catalog = "endavibe")
public class ProjectQuizEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id", nullable = false)
    private QuizEntity quiz;

    @Column(name = "ins_date", nullable = false)
    private LocalDateTime insDate;

    @Column(name = "is_sent")
    private Boolean isSent;

    @PrePersist
    protected void onCreate() {
        setUuid(java.util.UUID.randomUUID());
    }

}

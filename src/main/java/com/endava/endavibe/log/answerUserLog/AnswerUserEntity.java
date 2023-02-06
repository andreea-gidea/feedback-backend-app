package com.endava.endavibe.log.answerUserLog;

import com.endava.endavibe.project.ProjectEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "answer_user")
public class AnswerUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quiz_id")
    private Long quizId;

    @Column(name = "app_user_id")
    private Long appUserId;

    @Column(name = "ins_date")
    private LocalDateTime insDate;

    @Column(name = "project_id")
    private Long projectId;

}

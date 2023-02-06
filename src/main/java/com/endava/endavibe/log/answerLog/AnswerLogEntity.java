package com.endava.endavibe.log.answerLog;

import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answer_log", schema = "public", catalog = "endavibe")
public class AnswerLogEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "question_category_id")
    private Long questionCategoryId;

    @Column(name = "question_category")
    private String questionCategory;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "question_quiz_id")
    private Long questionQuizId;

    @Column(name = "answer_value")
    private Long answerValue;

    @Column(name = "question_content")
    private String questionContent;

    @Column(name = "ins_date", nullable = false)
    private LocalDateTime insDate;
}

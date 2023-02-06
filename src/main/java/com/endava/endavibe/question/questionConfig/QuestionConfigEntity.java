package com.endava.endavibe.question.questionConfig;

import com.endava.endavibe.answer.AnswerEntity;
import com.endava.endavibe.question.QuestionEntity;
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
@Table(name = "question_config", schema = "public", catalog = "endavibe")
public class QuestionConfigEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = false)
    private QuestionEntity question;

    @ManyToOne
    @JoinColumn(name = "answer_id", referencedColumnName = "id", nullable = false)
    private AnswerEntity answer;

    @Column(name = "ins_date", nullable = false)
    private LocalDateTime insDate;

}

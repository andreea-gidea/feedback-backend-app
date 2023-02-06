package com.endava.endavibe.question;

import com.endava.endavibe.common.dto.QuestionDto;
import com.endava.endavibe.question.questionCategory.QuestionCategoryEntity;
import com.endava.endavibe.question.questionConfig.QuestionConfigEntity;
import com.endava.endavibe.quiz.questionQuiz.QuestionQuizEntity;
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
@Table(name = "question", schema = "public", catalog = "endavibe")
public class QuestionEntity implements Serializable {

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
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private QuestionCategoryEntity questionCategory;

    @Column(name = "ins_date", nullable = false)
    private LocalDateTime insDate;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<QuestionConfigEntity> questionConfigs;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<QuestionQuizEntity> questionQuizs;

    @PrePersist
    protected void onCreate() {
        setUuid(java.util.UUID.randomUUID());
    }

    public QuestionDto toDto() {
        return new ModelMapper().map(this, QuestionDto.class);
    }
}

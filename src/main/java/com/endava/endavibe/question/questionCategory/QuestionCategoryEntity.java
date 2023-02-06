package com.endava.endavibe.question.questionCategory;

import com.endava.endavibe.common.dto.QuestionDto;
import com.endava.endavibe.question.QuestionEntity;
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
@Table(name = "question_category", schema = "public", catalog = "endavibe")
public class QuestionCategoryEntity implements Serializable {

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
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "ins_date", nullable = false)
    private LocalDateTime insDate;

    @OneToMany(mappedBy = "questionCategory", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<QuestionEntity> questions;

    @PrePersist
    protected void onCreate() {
        setUuid(java.util.UUID.randomUUID());
    }

    public QuestionDto toDto() {
        return new ModelMapper().map(this, QuestionDto.class);
    }
}

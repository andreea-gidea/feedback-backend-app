package com.endava.endavibe.log.questionLog;

import com.endava.endavibe.common.dto.QuestionLogDto;
import com.endava.endavibe.question.QuestionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serial;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@Table(name = "question_log", schema = "public", catalog = "endavibe")
public class QuestionLogEntity {
    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "project_id")
    private Long projectId;

    @Basic
    @Column(name = "question_id")
    private Long questionId;

    @Basic
    @Column(name = "question_category_id")
    private Long questionCategoryId;

    @Basic
    @Column(name = "sending_counter")
    private Long sendingCounter;

    public QuestionLogDto toDto() {
        return new ModelMapper().map(this, QuestionLogDto.class);
    }
}

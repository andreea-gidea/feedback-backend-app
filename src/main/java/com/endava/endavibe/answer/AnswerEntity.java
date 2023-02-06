package com.endava.endavibe.answer;

import com.endava.endavibe.common.dto.stats.AnswerDto;
import com.endava.endavibe.question.questionConfig.QuestionConfigEntity;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "answer", schema = "public", catalog = "endavibe")
public class AnswerEntity implements Serializable {

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

    @Column(name = "value", nullable = false)
    private Long value;

    @Column(name = "ins_date", nullable = false)
    private LocalDateTime insDate;

    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<QuestionConfigEntity> questionConfigList;

    @PrePersist
    protected void onCreate() {
        setUuid(java.util.UUID.randomUUID());
    }

    public AnswerDto toDto() {
        return new ModelMapper().map(this, AnswerDto.class);
    }
}

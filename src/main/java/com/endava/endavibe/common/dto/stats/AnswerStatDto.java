package com.endava.endavibe.common.dto.stats;

import com.endava.endavibe.statistics.AnswerStatEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AnswerStatDto {
    private Long id;
    private Long projectId;
    private Long questionCategoryId;
    private Double avgValue;
    private Long nrOfRespondents;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    public AnswerStatDto(Long projectId, Long questionCategoryId, Double avgValue, Long nrOfRespondents, LocalDateTime fromDate, LocalDateTime toDate) {
        this.projectId = projectId;
        this.questionCategoryId = questionCategoryId;
        this.avgValue = avgValue;
        this.nrOfRespondents = nrOfRespondents;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public AnswerStatEntity toEntity() {
        return new ModelMapper().map(this, AnswerStatEntity.class);
    }
}

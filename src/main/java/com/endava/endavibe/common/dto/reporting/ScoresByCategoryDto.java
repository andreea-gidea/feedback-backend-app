package com.endava.endavibe.common.dto.reporting;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ScoresByCategoryDto {

    private UUID categoryUuid;
    private String categoryTitle;
    private List<ReportingAnswerStatForGraphDto> stats;

}

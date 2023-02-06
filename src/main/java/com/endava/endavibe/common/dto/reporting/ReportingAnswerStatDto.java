package com.endava.endavibe.common.dto.reporting;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ReportingAnswerStatDto {

    private String questionCategoryTitle;
    private Double avgValue;
    private Long nrOfRespondents;


}

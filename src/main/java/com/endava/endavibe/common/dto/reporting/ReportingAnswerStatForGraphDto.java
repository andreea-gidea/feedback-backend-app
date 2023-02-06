package com.endava.endavibe.common.dto.reporting;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ReportingAnswerStatForGraphDto extends ReportingAnswerStatDto{

    private Long nrOfProjectUsers;
    private String weekAndYearOfStat;

}

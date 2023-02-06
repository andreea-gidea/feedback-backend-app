package com.endava.endavibe.common.dto.reporting;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectWeeklyReportDto {

    private UUID projectUuid;
    private String projectName;
    private String weekAndYearOfStat;
    private Long nrOfProjectUsers;
    private List<ReportingAnswerStatDto> reportsRelated;

}

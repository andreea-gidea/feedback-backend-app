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
public class ProjectInfoOverallDto {

    private UUID projectUuid;
    private String projectName;
    private List<WeeklyReportDto> lastReports;

}

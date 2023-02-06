package com.endava.endavibe.common.dto.reporting;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class WeeklyReportDto {

    private UUID uuid;

    private String weekAndYearOfStat;

    private Double overallScore;

    private Long nrOfTeamMembers;

    private Long nrOfRespondents;


}

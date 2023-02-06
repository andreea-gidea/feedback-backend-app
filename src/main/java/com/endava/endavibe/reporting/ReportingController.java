package com.endava.endavibe.reporting;


import com.endava.endavibe.common.dto.reporting.OverallReportDto;
import com.endava.endavibe.common.dto.reporting.ProjectInfoGraphDto;
import com.endava.endavibe.common.dto.reporting.ProjectWeeklyReportDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reporting")
public class ReportingController {

    private final WeeklyReportService weeklyReportService;

    @Operation(summary = "Get weekly reports for all projects")
    @GetMapping()
    public ResponseEntity<?> getAllWeeklyReports() {

        OverallReportDto weeklyReports = weeklyReportService.getAllWeeklyReportsByCurrentUser();
        HttpStatus status = ObjectUtils.isNotEmpty(weeklyReports) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(weeklyReports, status);

    }

    @Operation(summary = "Get a detailed weekly report based on a overall weekly report")
    @GetMapping("/{overallReportUuid}")
    public ResponseEntity<?> getDetailedWeeklyReports(@PathVariable String overallReportUuid) {

        ProjectWeeklyReportDto answerStatDtos = weeklyReportService.getDetailedWeeklyReports(UUID.fromString(overallReportUuid));
        HttpStatus status = ObjectUtils.isNotEmpty(answerStatDtos) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(answerStatDtos, status);

    }

    @Operation(summary = "Get a list of reports to generate a graph")
    @GetMapping("/{projectUuid}/{period}")
    public ResponseEntity<?> getReportsForGraph(@PathVariable("projectUuid") String projectUuid, @PathVariable("period") String period) {

        ProjectInfoGraphDto projectInfoGraphDto = weeklyReportService.getReportsForGraph(projectUuid, period);
        HttpStatus status = ObjectUtils.isNotEmpty(projectInfoGraphDto) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(projectInfoGraphDto, status);

    }

}

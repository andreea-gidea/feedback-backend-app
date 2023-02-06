package com.endava.endavibe.statistics;

import com.endava.endavibe.reporting.WeeklyReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatsController {

    private final AnswerStatService answerStatService;
    private final WeeklyReportService weeklyReportService;

    @Operation(summary = "Analyze the last logs and create stats by project and category")
    @GetMapping("/save")
    public void createStats() {
        answerStatService.saveAnswersStat();
        weeklyReportService.saveWeeklyReports();
    }

    @Operation(summary = "Get all stats")
    @GetMapping()
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(answerStatService.getAll());
    }
}

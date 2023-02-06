package com.endava.endavibe.mock;

import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.mock.util.MockService;
import com.endava.endavibe.quiz.QuizService;
import com.endava.endavibe.reporting.WeeklyReportService;
import com.endava.endavibe.statistics.AnswerStatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mock")
public class MockController {
    private static final int DEFAULT_NR_PROJECTS = 1;
    private final MockService mockService;
    private final QuizService quizService;
    private final AnswerStatService answerStatService;
    private final WeeklyReportService weeklyReportService;

    @Operation(summary = "Generates a given number of projects, default is one if no number is provided")
    @PostMapping ("/generate_projects")
    public void generateProjects(@RequestParam(name="number", defaultValue = "1") Integer number) throws Exception {
        for(int i=0; i<number; i++)
            mockService.createProject();
    }

    @PostMapping ("/generate_quiz")
    public void generateQuiz(@RequestParam(name="number", defaultValue = "1") Integer number) throws Exception {
        for(int i=0; i<number; i++)
            quizService.generateQuiz();
    }

    @PostMapping ("/generate_answers")
    public void generateAnswers(LocalDateTime date) throws Exception {
        //mockService.generateAnswers(date);
    }

    public void generateAnswers(LocalDateTime from, LocalDateTime to) throws Exception {
        mockService.generateAnswers(from, to);
    }

    @Operation(summary = "Generates mock answers User Logs,Answer Stats " +
            "and adjacently Weekly for every project in the database")
    @PostMapping ("/generate_stats")
    public void generateStats() {
        answerStatService.saveAnswersStat();
        weeklyReportService.saveWeeklyReports();
    }


    @Operation(summary = "Generates mock logs, stats and reports by a given date interval, " +
            "the interval must be in the same year")
    @PostMapping ("/generate_weekly_scenario")
    public void scenario(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                         @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) throws Exception {

        if(startDate.getYear() != endDate.getYear())
            throw new BusinessException("Select dates in the same year period");

        for (LocalDateTime i = startDate; i.isBefore(endDate); i = i.plusWeeks(1)) {
            this.generateAnswers(i, i.plusWeeks(1));
            generateStats();
        }
    }

}

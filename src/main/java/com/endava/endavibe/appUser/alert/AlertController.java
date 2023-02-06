package com.endava.endavibe.appUser.alert;

import com.endava.endavibe.statistics.AnswerStatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alert")
public class AlertController {

    private final AnswerStatService answerStatService;

    @Operation(summary = "Check answer stats values and send email to subscribers if something is wrong")
    @GetMapping()
    public void analyzeStats() throws Exception {
        answerStatService.alert();
    }
}

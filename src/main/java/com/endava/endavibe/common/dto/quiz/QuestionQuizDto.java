package com.endava.endavibe.common.dto.quiz;

import com.endava.endavibe.common.dto.stats.AnswerDto;
import com.endava.endavibe.common.dto.QuestionDto;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class QuestionQuizDto {

    private Long id;
    private QuizDto quiz;
    private QuestionDto question;
    private AnswerDto answer;
    private LocalDateTime insDate;
}

package com.endava.endavibe.common.dto.quiz;

import com.endava.endavibe.common.dto.QuestionAnswerDto;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Data
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCompleteQuizDto {
    private UUID appUserUuid;
    private UUID quizUuid;
    private UUID projectUuid;
    private List<QuestionAnswerDto> listOfAnswers;
}

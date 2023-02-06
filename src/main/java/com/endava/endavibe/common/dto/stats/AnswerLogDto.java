package com.endava.endavibe.common.dto.stats;

import com.endava.endavibe.common.dto.ProjectDto;
import com.endava.endavibe.common.dto.QuestionCategoryDto;
import com.endava.endavibe.common.dto.QuestionDto;
import com.endava.endavibe.common.dto.quiz.QuestionQuizDto;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AnswerLogDto {
    private Long id;
    private ProjectDto project;
    private QuestionCategoryDto questionCategory;
    private QuestionDto question;
    private QuestionQuizDto questionQuiz;
    private Long answerValue;
    private String QuestionContent;
}

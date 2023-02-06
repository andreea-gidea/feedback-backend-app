package com.endava.endavibe.common.dto.stats;

import com.endava.endavibe.common.dto.quiz.QuizDto;
import com.endava.endavibe.common.dto.user.AppUserDto;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class AnswerUserDto {
    private Long id;
    private QuizDto quiz;
    private AppUserDto appUser;
    private LocalDateTime insDate;
}

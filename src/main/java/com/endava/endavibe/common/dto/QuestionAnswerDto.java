package com.endava.endavibe.common.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerDto {
    private UUID questionUuid;
    private UUID answerUuid;
}

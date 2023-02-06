
package com.endava.endavibe.common.dto.quiz;

import com.endava.endavibe.common.dto.QuestionDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizDto extends RepresentationModel<QuizDto> {

    @JsonIgnore
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    private String title;
    private Boolean sent;
    private Boolean completed;
    private List<QuestionDto> questions;

}

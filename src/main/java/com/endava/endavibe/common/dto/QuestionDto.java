
package com.endava.endavibe.common.dto;


import com.endava.endavibe.common.dto.stats.AnswerDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
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
public class QuestionDto {

    @JsonIgnore
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<AnswerDto> answers;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private QuestionCategoryDto category;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID categoryUuid;

    private String content;


}

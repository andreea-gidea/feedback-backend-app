package com.endava.endavibe.common.dto.reporting;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeSet;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReportsForGraphDto {

    private TreeSet<String> labels;
    private List<ReportingAnswerStatDto> projects;

}

package com.endava.endavibe.common.dto.reporting;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProjectInfoGraphDto {

    private UUID projectUuid;
    private String projectName;
    private TreeSet<String> labels;
    private List<ScoresByCategoryDto> scoresByCategories;

}

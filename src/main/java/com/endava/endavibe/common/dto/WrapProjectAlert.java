package com.endava.endavibe.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WrapProjectAlert {
    ProjectDto projectDto;
    Double averageValue;
    List<AlertProjectCategoryDto> alertProjectCategories = new ArrayList<>();
}

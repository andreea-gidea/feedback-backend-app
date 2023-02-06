package com.endava.endavibe.common.dto;

import com.endava.endavibe.common.util.enums.AlertType;
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
public class AlertProjectCategoryDto {
    private ProjectDto projectDto;
    private QuestionDto questionCategoryDto;
    private String value;
    private List<AlertType> alertsType = new ArrayList<>();

    @Override
    public String toString() {
        return "AlertProjectCategoryDto{" +
                "projectUuid=" + projectDto +
                ", questionCategoryUuid=" + questionCategoryDto +
                ", value=" + value +
                ", alertType=" + alertsType +
                '}';
    }
}

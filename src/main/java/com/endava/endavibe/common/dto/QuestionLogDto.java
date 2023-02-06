package com.endava.endavibe.common.dto;


import com.endava.endavibe.log.questionLog.QuestionLogEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;


public class QuestionLogDto extends RepresentationModel<QuestionLogDto> {

    @JsonIgnore
    private Long id;

    private Long projectId;
    private Long questionId;
    private Long sendingCounter;
    private Long questionCategoryId;

    public QuestionLogEntity toEntity() {
        return new ModelMapper().map(this, QuestionLogEntity.class);
    }

}

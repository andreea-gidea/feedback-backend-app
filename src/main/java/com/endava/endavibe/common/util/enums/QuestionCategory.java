package com.endava.endavibe.common.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionCategory {

    RELATED_TO_PROJECT(1L, "related to the project"),
    RELATED_TO_PERSONAL_SATISFACTION(2L, "related to personal satisfaction"),
    RELATED_TO_PEERS(3L, "related to the peers"),
    RELATED_TO_MANAGER(4L, "related to the manager");

    private final Long id;
    private final String title;
}

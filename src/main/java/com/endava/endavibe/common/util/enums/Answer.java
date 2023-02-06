package com.endava.endavibe.common.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Answer {

    STRONGLY_DISAGREE(1L, "Strongly disagree", 1L),
    DISAGREE(2L, "Disagree", 2L),
    AGREE(3L, "Agree", 4L),
    STRONGLY_AGREE(4L, "Strongly agree", 5L);


    private final Long id;
    private final String title;
    private final Long value;

}

package com.endava.endavibe.common.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SatisfactionType {
    MINIMUM(1D),
    AVERAGE(3D),
    MAXIMUM(5D);
    private final Double value;
}

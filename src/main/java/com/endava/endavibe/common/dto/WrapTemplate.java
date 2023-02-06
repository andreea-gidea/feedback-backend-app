package com.endava.endavibe.common.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WrapTemplate {

    String objectName;
    Object object;
}

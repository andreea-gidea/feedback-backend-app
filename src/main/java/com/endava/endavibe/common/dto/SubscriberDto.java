package com.endava.endavibe.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriberDto {

    @JsonIgnore
    private Long id;

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
}

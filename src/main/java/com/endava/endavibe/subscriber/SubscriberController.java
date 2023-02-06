package com.endava.endavibe.subscriber;

import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/subscriber")
@RequiredArgsConstructor
public class SubscriberController {

    private final SubscriberService subscriberService;


    @Operation(summary = "Update subscribers")
    @PutMapping("/{projectUuid}")
    public ResponseEntity<?> updateProjectSubscribers(@PathVariable String projectUuid, @RequestBody List<AppUserDto> subscriberDtoList) throws Exception {
        List<AppUserDto> result = subscriberService.updateProjectSubscribers(UUID.fromString(projectUuid), subscriberDtoList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Unsubscribe")
    @PutMapping("/unsubscribe/{projectUuid}/{subscriberUuid}")
    public ResponseEntity<?> unsubscribeFromProject(@PathVariable String projectUuid, @PathVariable String subscriberUuid) throws BadRequestException {

        if (StringUtils.isEmpty(projectUuid) || StringUtils.isEmpty(subscriberUuid)) {
            throw new BadRequestException("At least one of the received uuid is null");
        }
        subscriberService.unsubscribe(UUID.fromString(projectUuid), UUID.fromString(subscriberUuid));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

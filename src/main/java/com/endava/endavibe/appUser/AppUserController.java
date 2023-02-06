package com.endava.endavibe.appUser;

import com.endava.endavibe.common.dto.user.AppUserWithAccessDto;
import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.util.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AppUserController {


    private final AppUserService appUserService;

    @Operation(summary = "Get a team member by id")
    @GetMapping("/{uuid}")
    public ResponseEntity<?> getAppUserById(@PathVariable("uuid") String uuid) throws BadRequestException, IllegalAccessException {

        AppUserDto userDto = appUserService.getAppUserById(UUID.fromString(uuid));
        HttpStatus status = ObjectUtils.isNotEmpty(userDto) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(userDto, status);

    }


    @Operation(summary = "Update app user member by uuid")
    @PatchMapping("/{uuid}")
    public ResponseEntity<?> updateAppUser(@PathVariable("uuid") String id, @RequestBody AppUserDto appUserDto) throws IllegalAccessException, BadRequestException {
        if (!ValidationUtils.isValidRequestBody(appUserDto)) {
            throw new BadRequestException("The received object is not correct");
        }
        AppUserDto user = appUserService.updateAppUser(UUID.fromString(id), appUserDto);
        HttpStatus status = ObjectUtils.isNotEmpty(user) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(user, status);
    }


    @Operation(summary = "Add a admin or PM")
    @PostMapping("/add")
    public ResponseEntity<?> addAdminOrPM(@RequestBody AppUserDto appUserDto) throws IllegalAccessException, BadRequestException {
        if (!ValidationUtils.isValidRequestBody(appUserDto)) {
            throw new BadRequestException("The received object is not correct");
        }
        AppUserDto user = appUserService.saveAdminOrPM(appUserDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }
    @Operation(summary = "Get a list of admins, subscribers and PM")
    @GetMapping("/appUsers")
    public ResponseEntity<?> getAdminsSubscribersPMs(){

        List<AppUserWithAccessDto> appUsersWithAccess = appUserService.getAppUsersWithAccess();
        HttpStatus status = CollectionUtils.isNotEmpty(appUsersWithAccess) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(appUsersWithAccess, status);

    }

    @Operation(summary = "Delete a app user by uuid")
    @DeleteMapping("/appUsers/{uuid}")
    public ResponseEntity<?> deleteAppUser(@PathVariable("uuid") String uuid) {

        appUserService.deleteAppUserWithAccess(UUID.fromString(uuid));
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @Operation(summary = "Get the number of AppUsers")
    @GetMapping("/appUsers/totalNumber")
    public ResponseEntity<?> getTheNumberOfAppUsers(){

        int nrOfAppUsers = appUserService.getNumberOfAppUsers();
        return new ResponseEntity<>(nrOfAppUsers, HttpStatus.OK);

    }

    @Operation(summary = "Add first user")
    @PostMapping("/firstUser")
    public ResponseEntity<?> addFirstUser(@RequestBody AppUserDto appUserDto) throws IllegalAccessException, BadRequestException {
        if (!ValidationUtils.isValidRequestBody(appUserDto)) {
            throw new BadRequestException("The received object is not correct");
        }
        AppUserDto user = appUserService.addFirstUser(appUserDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

}

package com.endava.endavibe.question.questionCategory;

import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.dto.QuestionCategoryDto;
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


@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class QuestionCategoryController {


    private final QuestionCategoryService questionCategoryService;

    @Operation(summary = "Get all question categories")
    @GetMapping
    public ResponseEntity<?> getAllQuestionCategory() {
        List<QuestionCategoryDto> questionCategoryDtoList = questionCategoryService.getAllQuestionCategory();
        HttpStatus status = CollectionUtils.isNotEmpty(questionCategoryDtoList) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(questionCategoryDtoList, status);
    }

    @Operation(summary = "Add question category")
    @PostMapping
    public ResponseEntity<?> addQuestionCategory(@RequestBody QuestionCategoryDto questionCategoryDto) throws IllegalAccessException, BadRequestException {
        if (!ValidationUtils.isValidRequestBody(questionCategoryDto)) {
            throw new BadRequestException("The received object is not correct");
        }

        QuestionCategoryDto questionCategory = questionCategoryService.addQuestionCategory(questionCategoryDto);
        HttpStatus status = ObjectUtils.isNotEmpty(questionCategory) ? HttpStatus.CREATED : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(questionCategory, status);
    }

    @Operation(summary = "Update question category by uuid")
    @PatchMapping("/{uuid}")
    public ResponseEntity<?> updateQuestionCategory(@PathVariable String uuid, @RequestBody QuestionCategoryDto questionCategoryDto) throws IllegalAccessException, BadRequestException {
        if (!ValidationUtils.isValidRequestBody(questionCategoryDto)) {
            throw new BadRequestException("The received object is not correct");
        }
        QuestionCategoryDto result = questionCategoryService.updateQuestionCategoryByUuid(UUID.fromString(uuid), questionCategoryDto);
        HttpStatus status = ObjectUtils.isNotEmpty(result) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(result, status);
    }

    @Operation(summary = "Delete question category by uuid")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteQuestionCategoryByUuid(@PathVariable UUID uuid) {

        questionCategoryService.deleteQuestionCategoryByUuid(uuid);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}

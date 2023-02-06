package com.endava.endavibe.question;

import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.dto.QuestionDto;
import com.endava.endavibe.common.util.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "Gets questionList by category")
    @GetMapping
    public ResponseEntity<?> getQuestionList() {

        List<QuestionDto> questionDtoList = questionService.getQuestionListByCategory();
        HttpStatus status = CollectionUtils.isNotEmpty(questionDtoList) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(questionDtoList, status);

    }

    @Operation(summary = "Create question")
    @PostMapping
    public ResponseEntity<?> addQuestion(@RequestBody QuestionDto questionDto) throws IllegalAccessException, BadRequestException {
        if (!ValidationUtils.isValidRequestBody(questionDto)) {
            throw new BadRequestException("The received object is not correct");
        }
        QuestionDto result = questionService.addQuestion(questionDto);
        HttpStatus status = ObjectUtils.isNotEmpty(result) ? HttpStatus.CREATED : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(result, status);

    }

    @Operation(summary = "Modify question by uuid")
    @PatchMapping("/{uuid}")
    public ResponseEntity<?> updateQuestionByUuid(@PathVariable String uuid, @RequestBody QuestionDto questionDto) throws IllegalAccessException, BadRequestException {
        if (!ValidationUtils.isValidRequestBody(questionDto)) {
            throw new BadRequestException("The received uuid or object is null");
        }
        QuestionDto result = questionService.updateQuestionByUuid(UUID.fromString(uuid), questionDto);
        HttpStatus status = ObjectUtils.isNotEmpty(result) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(result, status);

    }

    @Operation(summary = "Delete question by uuid")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteQuestion(@PathVariable String uuid) throws BadRequestException {
        if (StringUtils.isEmpty(uuid)) {
            throw new BadRequestException("The received uuid is null");
        }
        questionService.deleteQuestionByUuid(UUID.fromString(uuid));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.endava.endavibe.quiz;


import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.dto.quiz.PostCompleteQuizDto;
import com.endava.endavibe.common.dto.quiz.QuizDto;
import com.endava.endavibe.common.util.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "Generate a quiz")
    @GetMapping()
    public ResponseEntity<?> generateQuiz() throws Exception {

        quizService.generateQuiz();
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Operation(summary = "Complete quiz")
    @PostMapping()
    public ResponseEntity<?> completeQuiz(@RequestBody PostCompleteQuizDto postCompleteQuizDto) throws BadRequestException, IllegalAccessException {
        if (!ValidationUtils.isValidRequestBody(postCompleteQuizDto)) {
            throw new BadRequestException("The received object is not correct");
        }

        quizService.completeQuiz(postCompleteQuizDto);
        return ResponseEntity.ok("Responses saved");
    }

    @Operation(summary = "Get a quiz by id")
    @GetMapping("/{uuid}/{projectUuid}")
    public ResponseEntity<?> getQuizById(@PathVariable("uuid") String uuid,@PathVariable("projectUuid") String projectUuid) {

        QuizDto quiz = quizService.getQuizById(UUID.fromString(uuid),UUID.fromString(projectUuid));
        HttpStatus status = ObjectUtils.isNotEmpty(quiz) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(quiz, status);
    }
}

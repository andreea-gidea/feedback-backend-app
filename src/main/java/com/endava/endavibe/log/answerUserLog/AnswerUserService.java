package com.endava.endavibe.log.answerUserLog;

import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.appUser.AppUserRepository;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.quiz.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerUserService {

    private final AnswerUserRepository answerUserRepository;
    private final QuizRepository quizRepository;
    private final AppUserRepository appUserRepository;
    private final ProjectRepository projectRepository;

    public void saveUserLog(UUID quizId, UUID appUserUuid, UUID projectUuid) {
        AnswerUserEntity answerUserEntity = new AnswerUserEntity();
        answerUserEntity.setQuizId(quizRepository.findByUuid(quizId).orElseThrow(() -> {
            throw new BusinessException("There are no quiz with UUID " + quizId);
        }).getId());
        answerUserEntity.setAppUserId(appUserRepository.findByUuid(appUserUuid).orElseThrow(() -> {
            throw new BusinessException("There are no user with UUID " + appUserUuid);
        }).getId());
        answerUserEntity.setProjectId(projectRepository.findByUuid(projectUuid).orElseThrow(() -> {
            throw new BusinessException("There are no projects with UUID " + projectUuid);
        }).getId());
        answerUserEntity.setInsDate(LocalDateTime.now());

        answerUserRepository.save(answerUserEntity);
    }
}

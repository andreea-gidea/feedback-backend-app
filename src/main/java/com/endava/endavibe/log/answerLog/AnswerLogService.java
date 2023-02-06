package com.endava.endavibe.log.answerLog;

import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.quiz.PostCompleteQuizDto;
import com.endava.endavibe.answer.AnswerEntity;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.question.QuestionEntity;
import com.endava.endavibe.answer.AnswerRepository;
import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.question.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerLogService {

    private final AnswerLogRepository answerLogRepository;
    private final ProjectRepository projectRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public void saveAnswerLog(PostCompleteQuizDto postCompleteQuizDto) throws BusinessException {

        UUID projectUuid = postCompleteQuizDto.getProjectUuid();
        ProjectEntity project = projectRepository.findByUuid(projectUuid).orElseThrow(() -> {
            throw new BusinessException("There are no projects with UUID " + projectUuid);
        });

        postCompleteQuizDto.getListOfAnswers().forEach(questionAnswerDto -> {
            AnswerLogEntity answerLogEntity = new AnswerLogEntity();
            answerLogEntity.setProjectId(project.getId());

            /* Set Question data */
            UUID questionUuid = questionAnswerDto.getQuestionUuid();
            QuestionEntity question = questionRepository.findByUuid(questionUuid)
                    .orElseThrow(() -> {
                        throw new BusinessException("There is no question with UUID " + questionUuid);
                    });
            answerLogEntity.setQuestionId(question.getId());
            answerLogEntity.setQuestionCategory(question.getQuestionCategory().getTitle());
            answerLogEntity.setQuestionCategoryId(question.getQuestionCategory().getId());
            answerLogEntity.setQuestionContent(question.getContent());

            /* Set Answer value */
            UUID answerUuid = questionAnswerDto.getAnswerUuid();
            AnswerEntity answer = answerRepository.findByUuid(answerUuid).orElseThrow(() -> {
                throw new BusinessException("There is no answer with UUID " + answerUuid);
            });
            answerLogEntity.setAnswerValue(answer.getValue());

            /* Set insertion date */
            answerLogEntity.setInsDate(LocalDateTime.now());

            /* Save into database */
            answerLogRepository.save(answerLogEntity);
        });
    }
}

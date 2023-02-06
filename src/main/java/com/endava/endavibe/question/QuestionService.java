package com.endava.endavibe.question;

import com.endava.endavibe.answer.AnswerEntity;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.stats.AnswerDto;
import com.endava.endavibe.common.dto.QuestionDto;
import com.endava.endavibe.log.questionLog.QuestionLogEntity;
import com.endava.endavibe.log.questionLog.QuestionLogService;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.quiz.questionQuiz.QuestionQuizEntity;
import com.endava.endavibe.quiz.QuizEntity;
import com.endava.endavibe.answer.AnswerRepository;
import com.endava.endavibe.question.questionCategory.QuestionCategoryRepository;
import com.endava.endavibe.quiz.questionQuiz.QuestionQuizRepository;
import com.endava.endavibe.answer.AnswerService;
import com.endava.endavibe.question.questionConfig.QuestionConfigService;
import com.endava.endavibe.common.util.enums.QuestionCategory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class QuestionService {

    Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;
    private final QuestionQuizRepository questionQuizRepository;
    private final QuestionCategoryRepository questionCategoryRepository;

    private final AnswerService answerService;
    private final QuestionConfigService questionConfigService;
    private final AnswerRepository answerRepository;
    private final QuestionLogService questionLogService;

    /***
     * @param project - project entity
     * @param savedQuiz - one of the project quizzes
     * @return a list of random question from a priority list for this specific project.
     * The rarely sent questions have priority.
     * Abstract example :
     * Proj Question Category  Sent    |   hasPriority                  | Priority List
     * 1    q1       c1        0       |   Yes  bc. min. for c1 is 0    | c1 => [q2,q3]
     * 1    q2       c1        1       |   No   bc. min. for c1 is 0    |
     * 1    q3       c1        1       |   No   bc. min. for c1 is 0    |
     * 1    q4       c1        0       |   Yes  bc. min. for c1 is 0    |
     *
     * 1    q6       c2        2       |   No   bc. min. for c2 is 1    | c2 => [q7,q8]
     * 1    q7       c2        1       |   Yes  bc. min. for c2 is 1    |
     * 1    q8       c2        1       |   Yes  bc. min. for c2 is 1    |
     *
     * 1    q9       c3        0       |   Yes  bc. min. for c3 is 0    | c3 => [q9]
     *
     * From those priority lists for each category only one aleatory question will be selected.
     * @throws BusinessException
     */
    public List<QuestionDto> getAListOfQuestions(ProjectEntity project, QuizEntity savedQuiz) throws BusinessException {
        logger.info("Question selection started.");

        List<QuestionEntity> listOfQuestions = new ArrayList<>();
            for (QuestionCategory value : QuestionCategory.values()) {
                QuestionLogEntity rndQuestionLog = questionLogService.getRandomQuestionByProjectAndCategory(
                        project.getId(), value.getId());

                listOfQuestions.add(questionRepository.findById(rndQuestionLog.getQuestionId())
                                .orElseThrow(() -> {throw new BusinessException("Question not found"); })
                );
            }

        saveLinkQuestionToQuiz(savedQuiz, listOfQuestions);
        return createQuestionDto(listOfQuestions);
    }

    List<QuestionDto> createQuestionDto(List<QuestionEntity> listOfQuestions) throws BusinessException {
        List<QuestionDto> listOfQuestionsDto = new ArrayList<>();
        for (QuestionEntity q : listOfQuestions) {
            QuestionDto questionDto = q.toDto();
            List<AnswerDto> possibleAnswers = answerService.getAnswerDtoList(q);
            questionDto.setAnswers(possibleAnswers);
            listOfQuestionsDto.add(questionDto);
        }
        return listOfQuestionsDto;
    }

    private void saveLinkQuestionToQuiz(QuizEntity savedQuiz, List<QuestionEntity> listOfQuestions) {
        for (QuestionEntity q : listOfQuestions) {
            QuestionQuizEntity questionQuizEntity = QuestionQuizEntity.builder()
                    .quiz(savedQuiz)
                    .question(q)
                    .insDate(LocalDateTime.now())
                    .build();
            questionQuizRepository.save(questionQuizEntity);
        }
        logger.info("Questions have been assigned to related quiz");
    }


    public List<QuestionDto> getQuestionListByCategory() {
        List<QuestionEntity> questionList = questionRepository.findAll();
        return questionList.stream().map(QuestionEntity::toDto).toList();
    }

    public QuestionDto addQuestion(QuestionDto questionDto) throws BusinessException {
        logger.info("Add question started");

        QuestionEntity questionEntity = questionRepository.save(QuestionEntity.builder()
                .content(questionDto.getContent())
                .questionCategory(questionCategoryRepository.findByUuid(questionDto.getCategoryUuid()).orElseThrow(() -> {
                    throw new BusinessException("No question category for given id " + questionDto.getCategoryUuid());
                }))
                .insDate(LocalDateTime.now())
                .build());

        questionConfigService.createDefaultQuestionConfig(questionEntity);
        logger.info("Question have been created with id " + questionEntity.getUuid());

        return questionEntity.toDto();
    }

    public QuestionDto updateQuestionByUuid(UUID uuid, QuestionDto questionDto) {
        QuestionEntity questionEntity = questionRepository.findByUuid(uuid).orElseThrow(() -> new BusinessException("No question found for given id " + uuid));

        logger.info("Update question " + uuid + " started");

        if (StringUtils.isNotEmpty(questionDto.getContent())) {
            questionEntity.setContent(questionDto.getContent());
        }

        if (ObjectUtils.isNotEmpty(questionDto.getCategoryUuid())) {
            questionEntity.setQuestionCategory(questionCategoryRepository.findByUuid(questionDto.getCategoryUuid())
                    .orElseThrow(() -> new BusinessException("No question category for given id " + questionDto.getCategoryUuid())));
        }

        QuestionDto QuestionDto = questionRepository.save(questionEntity).toDto();
        logger.info("Question " + uuid + " have been updated");

        return QuestionDto;
    }

    public void deleteQuestionByUuid(UUID uuid) throws BusinessException {
        QuestionEntity questionEntity = questionRepository.findByUuid(uuid)
                .orElseThrow(() -> new BusinessException("No question found for given id " + uuid));

        if (CollectionUtils.isNotEmpty(questionEntity.getQuestionQuizs())) {
            throw new BusinessException("This question cannot be deleted because it's assigned to quizzes");
        }

        questionConfigService.deleteQuestionConfigByQuestion(questionEntity);
        questionRepository.delete(questionEntity);

        logger.info("Question " + uuid + " have been deleted");
    }

    public List<QuestionDto> setQuestionsForGivenQuiz(QuizEntity quiz) {
        List<QuestionDto> questionDtoList = questionRepository.findQuestionsByQuizId(quiz.getUuid())
                .stream()
                .map(QuestionEntity::toDto)
                .toList();

        for (QuestionDto question : questionDtoList) {
            question.setAnswers(answerRepository.findAnswersByQuestionId(question.getUuid())
                    .stream()
                    .map(AnswerEntity::toDto)
                    .toList());
        }
        return questionDtoList;
    }
}


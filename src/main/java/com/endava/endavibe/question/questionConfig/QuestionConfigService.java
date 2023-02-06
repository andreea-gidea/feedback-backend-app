package com.endava.endavibe.question.questionConfig;

import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.question.QuestionEntity;
import com.endava.endavibe.answer.AnswerRepository;
import com.endava.endavibe.common.util.enums.Answer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class QuestionConfigService {

    private final Logger logger = LoggerFactory.getLogger(QuestionConfigService.class);

    private final QuestionConfigRepository questionConfigRepository;
    private final AnswerRepository answerRepository;

    public void createDefaultQuestionConfig(QuestionEntity questionEntity) throws BusinessException {
        if (ObjectUtils.isEmpty(questionEntity)) {
            throw new BusinessException("Question entity is null");
        }

        logger.info("Create config for question " + questionEntity.getUuid());

        for (Answer answer : Answer.values()) {
            QuestionConfigEntity questionConfig = QuestionConfigEntity.builder()
                    .question(questionEntity)
                    .answer(answerRepository.findById(answer.getId())
                            .orElseThrow(() -> new BusinessException("No answer for given id " + answer.getId())))
                    .insDate(LocalDateTime.now()).build();
            questionConfigRepository.save(questionConfig);
        }
        logger.info("Config have been added for question " + questionEntity.getUuid());
    }

    public void deleteQuestionConfigByQuestion(QuestionEntity questionEntity) throws BusinessException {
        List<QuestionConfigEntity> questionConfigEntityList = questionConfigRepository.findByQuestion(questionEntity);
        if (CollectionUtils.isEmpty(questionConfigEntityList)) {
            throw new BusinessException("No question config found for question " + questionEntity.getUuid());
        }

        questionConfigRepository.deleteAll(questionConfigEntityList);
        logger.info("Configuration for question " + questionEntity.getUuid() + " have been deleted");
    }


}

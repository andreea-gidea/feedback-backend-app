package com.endava.endavibe.answer;

import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.stats.AnswerDto;
import com.endava.endavibe.question.questionConfig.QuestionConfigEntity;
import com.endava.endavibe.question.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {

    Logger logger = LoggerFactory.getLogger(AnswerService.class);

    private final ModelMapper modelMapper;
    private final AnswerRepository answerRepository;

    public List<AnswerDto> getAnswerDtoList(QuestionEntity q) throws BusinessException {
        logger.info("Get question configuration started for question with id : " + q.getId());

        List<AnswerEntity> answerEntities = q.getQuestionConfigs().stream()
                .map(QuestionConfigEntity::getAnswer)
                .toList();
        if (CollectionUtils.isEmpty(answerEntities)) {
            throw new BusinessException("No Configuration found for question with id : " + q.getId());
        }
        return modelMapper.map(answerEntities, new TypeToken<List<AnswerDto>>() {
        }.getType());
    }

    AnswerEntity getAnswerEntityById(Long id) throws RuntimeException {
        return answerRepository.findById(id).orElseThrow(() -> new BusinessException("No answer for given id " + id));
    }

    public Optional<AnswerEntity> getAnswerEntityByUuid(UUID answerUuid) {
        return answerRepository.findByUuid(answerUuid);
    }
}

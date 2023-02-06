package com.endava.endavibe.question.questionCategory;

import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.common.dto.QuestionCategoryDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
public class QuestionCategoryService {

    Logger logger = LoggerFactory.getLogger(QuestionCategoryService.class);

    private final QuestionCategoryRepository questionCategoryRepo;
    private final ModelMapper modelMapper;

    public List<QuestionCategoryDto> getAllQuestionCategory() {
        List<QuestionCategoryEntity> questionCategoryEntities = questionCategoryRepo.findAll();
        List<QuestionCategoryDto> questionCategoryDtoList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(questionCategoryEntities)) {
            questionCategoryDtoList = modelMapper.map(questionCategoryEntities, new TypeToken<List<QuestionCategoryDto>>() {
            }.getType());
        }

        return questionCategoryDtoList;
    }

    public QuestionCategoryDto addQuestionCategory(QuestionCategoryDto questionCategoryDto) {
        QuestionCategoryEntity questionCategoryEntity = questionCategoryRepo.save(QuestionCategoryEntity.builder().title(questionCategoryDto.getTitle()).insDate(LocalDateTime.now()).build());

        QuestionCategoryDto questionCategory = new QuestionCategoryDto();

        if (ObjectUtils.isNotEmpty(questionCategoryEntity)) {
            logger.info("Question category have been saved with uuid " + questionCategoryEntity.getUuid());
            questionCategory = modelMapper.map(questionCategoryEntity, QuestionCategoryDto.class);
        }

        return questionCategory;
    }

    public QuestionCategoryDto updateQuestionCategoryByUuid(UUID uuid, QuestionCategoryDto questionCategoryDto) throws BusinessException {
        QuestionCategoryEntity questionCategoryEntity = questionCategoryRepo.findByUuid(uuid).orElseThrow(() -> {
            throw new BusinessException("No question category found for given id " + uuid);
        });

        questionCategoryEntity.setTitle(questionCategoryDto.getTitle());
        QuestionCategoryEntity updatedEntity = questionCategoryRepo.save(questionCategoryEntity);
        logger.info("Question category with uuid " + questionCategoryEntity.getUuid() + " have been updated");

        return modelMapper.map(updatedEntity, QuestionCategoryDto.class);
    }

    public void deleteQuestionCategoryByUuid(UUID uuid) throws BusinessException {
        QuestionCategoryEntity questionCategoryEntity = questionCategoryRepo.findByUuid(uuid).orElseThrow(() -> {
            throw new BusinessException("No question category found for given id " + uuid);
        });

        if (CollectionUtils.isNotEmpty(questionCategoryEntity.getQuestions())) {
            throw new BusinessException("This category cannot be deleted because some questions are assigned to it");
        }

        questionCategoryRepo.delete(questionCategoryEntity);
        logger.info("Question category with id " + uuid + " have been deleted");
    }

}

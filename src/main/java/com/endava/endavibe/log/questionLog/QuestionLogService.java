package com.endava.endavibe.log.questionLog;

import com.endava.endavibe.project.ProjectRepository;
import com.endava.endavibe.question.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionLogService {
    Logger logger = LoggerFactory.getLogger(QuestionLogService.class);
    private final QuestionLogRepository questionLogRepository;
    private final ProjectRepository projectRepository;

    public QuestionLogEntity getRandomQuestionByProjectAndCategory(Long projectId, Long categoryId) {
        return questionLogRepository.getRandomQuestionByProjectAndCategory(projectId, categoryId);
    }

    public void initProjectQuestionLogs(Long projectId) {
        questionLogRepository.questionLogByProjectInit(projectId);
    }

    public void questionLogInsert(Long projectId, Long questionId, Long questionCategoryId) {
        questionLogRepository.questionLogInsert(projectId, questionId, questionCategoryId);
    }
}

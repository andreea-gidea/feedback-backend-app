package com.endava.endavibe.common.util;

import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.appUser.AppUserRepository;
import com.endava.endavibe.appUser.role.RoleEntity;
import com.endava.endavibe.common.exception.BusinessException;
import com.endava.endavibe.log.answerUserLog.AnswerUserEntity;
import com.endava.endavibe.log.answerUserLog.AnswerUserRepository;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserEntity;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
public class ValidationUtils {

    static Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    private static AppUserRepository appUserRepository;
    private static AnswerUserRepository answerUserRepository;
    private static ProjectAppUserRepository projectAppUserRepository;


    @Autowired
    private ValidationUtils(AppUserRepository appUserRepo, AnswerUserRepository answerUserRepo, ProjectAppUserRepository projectAppUserRepo) {
        ValidationUtils.appUserRepository = appUserRepo;
        ValidationUtils.answerUserRepository = answerUserRepo;
        ValidationUtils.projectAppUserRepository = projectAppUserRepo;

    }

    public static boolean isValidRequestBody(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        boolean isValid = Boolean.FALSE;
        for (Field field : fields) {
            field.setAccessible(Boolean.TRUE);
            if (field.get(obj) != null) {
                isValid = Boolean.TRUE;
            }
        }
        return isValid;
    }

    public static void validateUser(String email) throws BusinessException {
        if ("".equals(email)) {
            throw new BusinessException("App user has no valid email");
        }
        AppUserEntity existingUser = appUserRepository.findByEmail(email);
        if (ObjectUtils.isNotEmpty(existingUser)) {
            throw new BusinessException("App user with email " + email + " already exists");
        }
        logger.info("App user with email " + email + " is valid");

    }

    public static void validateSubscriber(String email) throws BusinessException {
        AppUserEntity existingUser = appUserRepository.findByEmail(email);
        if (ObjectUtils.isNotEmpty(existingUser)) {
            throw new BusinessException("A subscriber with the email " + email + " already exists");
        }
        logger.info("Subscriber with email " + email + " is valid");
    }

    public static void validateAdminOrPm(String email, RoleEntity role) throws BusinessException {
        AppUserEntity existingUser = appUserRepository.findByEmail(email);
        if (ObjectUtils.isNotEmpty(existingUser)) {
            List<ProjectAppUserEntity> projectAppUser = projectAppUserRepository.findByAppUserUuidAndRole(existingUser.getUuid(), role);
            if (ObjectUtils.isNotEmpty(projectAppUser)) {
                throw new BusinessException("App user  with the email " + email + " already exists as a " + role.getName());
            }
        }
        logger.info("App user with email " + email + " is valid");
    }

    public static void validateQuiz(Long quizId, Long userId , Long projectId){
        List<AnswerUserEntity> userAnswers = answerUserRepository.findByQuizIdAndAppUserIdAndProjectId(quizId, userId, projectId);
        if(CollectionUtils.isNotEmpty(userAnswers)){
            throw new BusinessException("This quiz have been already completed");
        }
    }

}

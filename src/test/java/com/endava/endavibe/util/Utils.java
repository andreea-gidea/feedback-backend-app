package com.endava.endavibe.util;

import com.endava.endavibe.answer.AnswerEntity;
import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.appUser.role.RoleEntity;
import com.endava.endavibe.common.dto.*;
import com.endava.endavibe.common.dto.quiz.QuizDto;
import com.endava.endavibe.common.dto.stats.AnswerDto;
import com.endava.endavibe.common.dto.user.AppUserDto;
import com.endava.endavibe.common.util.enums.RolesEnum;
import com.endava.endavibe.project.ProjectEntity;
import com.endava.endavibe.project.projectAppUser.ProjectAppUserEntity;
import com.endava.endavibe.question.QuestionEntity;
import com.endava.endavibe.question.questionCategory.QuestionCategoryEntity;
import com.endava.endavibe.question.questionConfig.QuestionConfigEntity;
import com.endava.endavibe.quiz.QuizEntity;
import com.endava.endavibe.common.util.enums.Answer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.time.LocalDateTime;
import java.util.*;

public class Utils {

    public static UUID appUserUuid_01 = UUID.fromString("ffd0aefe-a19a-45f9-8aa4-b10eb446f463");
    public static UUID appUserUuid_02 = UUID.fromString("ffd0aefe-a19a-45f9-8aa4-b10eb446f852");
    public static UUID projectUuid = UUID.fromString("ffd0aefe-a19a-45f9-8aa4-b10eb446f464");
    public static UUID questionUuid1 = UUID.fromString("ffd0aefe-a19a-54f9-8aa4-b10eb446f463");
    public static UUID questionUuid2 = UUID.fromString("ffd0aefe-a19a-54f9-8ab4-b10eb446f468");
    public static UUID questionUuid3 = UUID.fromString("ffd0aefe-a19a-54f9-8ac4-b10eb446f443");
    public static UUID questionUuid4 = UUID.fromString("ffd0aefe-a19a-54f9-8ad4-b10eb446f493");
    public static UUID questionCatUuid1 = UUID.fromString("ffd1aefe-b19a-54f9-8ad4-b10eb446f493");
    public static UUID questionCatUuid2 = UUID.fromString("ffd1aefe-b85a-54f9-8ad4-b12eb446f497");
    public static UUID quizUuid = UUID.fromString("f78442c0-3dcf-43ad-91a9-8362c151e3af");
    public static UUID answerUuid1 = UUID.fromString("b731ffec-c56c-4c33-b831-cf1bbcf67ffa");
    public static UUID answerUuid2 = UUID.fromString("668f8956-0e0f-4588-a019-658b685efb2f");
    public static UUID answerUuid3 = UUID.fromString("e6d1b04e-f181-4dae-a58a-097a624cbe03");
    public static UUID answerUuid4 = UUID.fromString("fa6e561b-3780-42f6-9cf3-01eddfe0b4dc");
    public static String email = "Test@email_01";

    public static AppUserDto getReadAppUserDtoFromEntity(AppUserEntity appUserEntity) {
        AppUserDto readAppUserDto = new AppUserDto();
        readAppUserDto.setId(appUserEntity.getId());
        readAppUserDto.setUuid(appUserEntity.getUuid());
        readAppUserDto.setFirstName(appUserEntity.getFirstName());
        readAppUserDto.setLastName(appUserEntity.getLastName());
        readAppUserDto.setEmail(appUserEntity.getEmail());

        return readAppUserDto;
    }

    public static AppUserDto getWriteAppUserDtoFromEntity(AppUserEntity appUserEntity) {
        AppUserDto writeAppUserDto = new AppUserDto();
        writeAppUserDto.setId(appUserEntity.getId());
        writeAppUserDto.setUuid(appUserEntity.getUuid());
        writeAppUserDto.setFirstName(appUserEntity.getFirstName());
        writeAppUserDto.setLastName(appUserEntity.getLastName());
        writeAppUserDto.setEmail(appUserEntity.getEmail());

        return writeAppUserDto;
    }

    public static List<AppUserDto> getAppUserDtoList() {
        List<AppUserDto> appUserDtoList = new ArrayList<>();
        appUserDtoList.add(AppUserDto.builder().id(1L).uuid(appUserUuid_01).firstName("First_name_01").lastName("Last_name_01").email("Test@email_01").build());
        appUserDtoList.add(AppUserDto.builder().id(2L).uuid(appUserUuid_02).firstName("First_name_02").lastName("Last_name_02").email("Test@email_02").build());

        return appUserDtoList;
    }



    public static List<AppUserEntity> getAppUserEntityList() {
        List<AppUserEntity> userList = new ArrayList<>();
        userList.add(AppUserEntity.builder().id(1L).firstName("First_name_01").lastName("Last_name_01")
                .email("email_test01@test.com").build());
        userList.add(AppUserEntity.builder().id(2L).firstName("First_name_02").lastName("Last_name_02")
                .email("email_test02@test.com").build());
        return userList;
    }


    public static Optional<ProjectEntity> getProjectEntityWithAssignations() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
        projectEntity.setUuid(projectUuid);
        projectEntity.setName("Project_Test");
        projectEntity.setIsEngineActive(Boolean.TRUE);

        ProjectAppUserEntity projectAppUserEntity = new ProjectAppUserEntity();
        projectAppUserEntity.setProject(projectEntity);
        projectAppUserEntity.setAppUser(getAppUserEntity());

        projectEntity.setProjectAppUserList(Arrays.asList(projectAppUserEntity));

        return Optional.of(projectEntity);
    }

    public static Optional<ProjectEntity> getProjectEntity() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
        projectEntity.setUuid(projectUuid);
        projectEntity.setName("Project_Test");
        projectEntity.setIsEngineActive(Boolean.TRUE);

        ProjectAppUserEntity projectAppUserEntity = new ProjectAppUserEntity();
        projectAppUserEntity.setProject(projectEntity);
        projectAppUserEntity.setAppUser(getAppUserEntity());
        projectAppUserEntity.setRole(getRoleEntity(RolesEnum.MANAGER));

        projectEntity.setProjectAppUserList(Arrays.asList(projectAppUserEntity));

        return Optional.of(projectEntity);
    }

    public static Optional<ProjectEntity> getProjectEntityWithNoUsers() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
        projectEntity.setUuid(projectUuid);
        projectEntity.setName("Project_Test");

        return Optional.of(projectEntity);
    }

    public static ProjectDto getProjectDto() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setUuid(projectUuid);
        projectDto.setName("Project_Test");

        return projectDto;
    }

    public static ProjectDto getProjectDtoWithSubscribers() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setUuid(projectUuid);
        projectDto.setName("Project_Test");
        projectDto.setListOfSubscribers(getAppUserDtoList());

        return projectDto;
    }

    public static ProjectDto getProjectDtoWithTeamMembers() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setUuid(projectUuid);
        projectDto.setName("Project_Test");
        projectDto.setListOfTeamMembers(getAppUserDtoList());

        return projectDto;
    }

    public static ProjectDto getFullProjectDto() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setUuid(projectUuid);
        projectDto.setName("Project_Test");
        projectDto.setListOfTeamMembers(getAppUserDtoList());
        projectDto.setListOfSubscribers(getAppUserDtoList());
        projectDto.setIsEngineActive(Boolean.TRUE);

        return projectDto;
    }


    public static AppUserEntity getAppUserEntity() {
        AppUserEntity appUserEntity = new AppUserEntity();
        appUserEntity.setId(1L);
        appUserEntity.setUuid(appUserUuid_01);
        appUserEntity.setFirstName("First_Name_1_Test");
        appUserEntity.setLastName("Last_Name_1_Test");
        appUserEntity.setEmail(email);

        return appUserEntity;
    }



    public static AppUserDto getAppUserDto() {
        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setId(1L);
        appUserDto.setUuid(appUserUuid_01);
        appUserDto.setFirstName("First_Name_1_Test");
        appUserDto.setLastName("Last_Name_1_Test");

        return appUserDto;
    }

    public static Set<ProjectDto> getProjectDtoList() {
        Set<ProjectDto> projectDtoList = new TreeSet<>();

        projectDtoList.add(ProjectDto.builder().id(1L).name("Project_Name_1_Test")
                .build());
        projectDtoList.add(ProjectDto.builder().id(2L).name("Project_Name_2_Test")
                .build());

        return projectDtoList;
    }

    public static List<ProjectEntity> getProjectEntityList() {
        List<ProjectEntity> projectEntities = new ArrayList<>();

        projectEntities.add(ProjectEntity.builder().id(1L).name("Project_Name_1_Test")
                .build());
        projectEntities.add(ProjectEntity.builder().id(2L).name("Project_Name_2_Test")
                .build());

        return projectEntities;
    }

    public static String getRequestBodyParam(Object object) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

    public static List<QuestionDto> getQuestionList() {
        List<QuestionDto> questionList = new ArrayList<>();

        questionList.add(QuestionDto.builder().id(1L).content("Do you feel your work is recognized on the project?").answers(getAnswerDtoList()).uuid(questionUuid1).build());
        questionList.add(QuestionDto.builder().id(2L).content("How do I feel today?").answers(getAnswerDtoList()).uuid(questionUuid2).build());
        questionList.add(QuestionDto.builder().id(3L).content("How do you get along with your teammates?").answers(getAnswerDtoList()).uuid(questionUuid3).build());
        questionList.add(QuestionDto.builder().id(4L).content("Do you feel like your direct manager cares about your well-being?").answers(getAnswerDtoList()).uuid(questionUuid4).build());

        return questionList;
    }

    public static QuestionEntity getQuestionEntity() {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(1L);
        questionEntity.setUuid(questionUuid1);
        questionEntity.setContent("Test_Content");
        questionEntity.setQuestionCategory(getQuestionCategoryEntity());

        return questionEntity;
    }

    public static QuestionCategoryEntity getQuestionCategoryEntity() {
        return QuestionCategoryEntity.builder()
                .id(1L)
                .uuid(questionCatUuid1)
                .title("Category_1")
                .build();
    }

    public static QuestionCategoryEntity getQuestionCategoryEntityQWithQuestion() {
        return QuestionCategoryEntity.builder()
                .id(1L)
                .uuid(questionCatUuid1)
                .title("Category_1")
                .questions(List.of(getQuestionEntity()))
                .build();
    }

    public static List<QuestionConfigEntity> getQuestionConfigEntity() {
        List<QuestionConfigEntity> list = new ArrayList<>();
        for (Answer answer : Answer.values()) {
            list.add(QuestionConfigEntity.builder()
                    .question(getQuestionEntity())
                    .answer(AnswerEntity.builder()
                            .id(answer.getId())
                            .uuid(UUID.randomUUID())
                            .title(answer.getTitle())
                            .value(answer.getValue())
                            .build())
                    .insDate(LocalDateTime.now()).build());
        }

        return list;
    }

    public static QuestionDto getReadQuestionDto() {
        return QuestionDto.builder().id(1L).content("Test_content").uuid(Utils.questionUuid1).build();
    }

    public static QuestionDto getWriteQuestionDto() {
        return QuestionDto.builder().id(1L).content("Test_content").categoryUuid(Utils.questionCatUuid1).build();
    }

    public static List<QuestionCategoryDto> getQuestionCategoryDtoList() {
        List<QuestionCategoryDto> list = new ArrayList<>();
        list.add(QuestionCategoryDto.builder().id(1L).uuid(questionCatUuid1).title("Category_1").build());
        list.add(QuestionCategoryDto.builder().id(2L).uuid(questionCatUuid2).title("Category_2").build());

        return list;
    }

    public static List<QuestionCategoryEntity> getQuestionCategoryEntityList() {
        List<QuestionCategoryEntity> list = new ArrayList<>();
        list.add(QuestionCategoryEntity.builder().id(1L).uuid(questionCatUuid1).title("Category_1").build());
        list.add(QuestionCategoryEntity.builder().id(2L).uuid(questionCatUuid2).title("Category_2").build());

        return list;
    }

    public static QuestionCategoryDto getCreateQuestionCategoryDto() {
        return QuestionCategoryDto.builder().title("Category_1").build();
    }

    public static QuestionCategoryDto getReadQuestionCategoryDto() {
        return QuestionCategoryDto.builder().id(1L).uuid(questionCatUuid1).title("Category_1").build();
    }

    public static QuestionCategoryDto getQuestionCatFromEntity(QuestionCategoryEntity entity) {
        return QuestionCategoryDto.builder().id(entity.getId()).uuid(entity.getUuid())
                .title(entity.getTitle()).build();
    }



    public static List<AnswerDto> getAnswerDtoList() {
        List<AnswerDto> list = new ArrayList<>();
        list.add(AnswerDto.builder().id(1L).title("Strongly disagree").value(1L).uuid(answerUuid1).build());
        list.add(AnswerDto.builder().id(2L).title("Disagree").value(2L).uuid(answerUuid2).build());
        list.add(AnswerDto.builder().id(3L).title("Agree").value(3L).uuid(answerUuid3).build());
        list.add(AnswerDto.builder().id(4L).title("Strongly agree").value(4L).uuid(answerUuid4).build());

        return list;
    }

    public static QuizDto getQuizDto() {
        QuizDto quizDto = new QuizDto();
        quizDto.setId(1L);
        quizDto.setUuid(quizUuid);
        quizDto.setTitle("Quiz_test");
        quizDto.setSent(Boolean.TRUE);
        quizDto.setCompleted(Boolean.FALSE);
        quizDto.setQuestions(getQuestionList());

        return quizDto;
    }

    public static QuizEntity getQuizEntity() {
        QuizEntity quizEntity = new QuizEntity();
        quizEntity.setId(1L);
        quizEntity.setUuid(quizUuid);


        return quizEntity;
    }
    public static RoleEntity getRoleEntity(RolesEnum role) {

        RoleEntity roleEntity = RoleEntity.builder().id(1L).name(String.valueOf(role)).build();

        return roleEntity;
    }

}

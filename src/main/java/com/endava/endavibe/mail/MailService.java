package com.endava.endavibe.mail;


import com.endava.endavibe.common.dto.WrapTemplate;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final Configuration freemarkerConfig;

    public void sendHtmlMessage(List<String> bcc, String subject, String htmlBody) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setSubject(subject);
        for (String email : bcc) {

            helper.addBcc(email);
        }

        if (ObjectUtils.isEmpty(htmlBody)) {
            throw new MessagingException("The mail could not be sent");
        } else {
            helper.setText(htmlBody, true);
            javaMailSender.send(message);
        }
    }

    public String generateTemplate(String templateName, WrapTemplate... objects) throws Exception {
        Map<String, Object> model = new HashMap<>();
        for (WrapTemplate object : objects) {
            model.put(object.getObjectName(), object.getObject());
        }
        return getFreeMarkerTemplateContent(model, templateName);
    }

    public String getFreeMarkerTemplateContent(Map<String, Object> model, String templateName) throws Exception {
        StringBuilder content = new StringBuilder();
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate(templateName), model));
            return content.toString();
        } catch (Exception e) {
            throw new Exception("Template for mail could not be processed ");
        }
    }

}

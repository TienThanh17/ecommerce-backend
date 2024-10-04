package com.bamito.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ApplicationConfig {
    @Value("${cloudinary.cloud-name}")
    private String CLOUDINARY_CLOUD_NAME;
    @Value("${cloudinary.api-key}")
    private String CLOUDINARY_API_KEY;
    @Value("${cloudinary.api-secret}")
    private String CLOUDINARY_API_SECRET;

    @Value("${spring.mail.host}")
    private String MAIL_HOST;
    @Value("${spring.mail.port}")
    private String MAIL_PORT;
    @Value("${spring.mail.username}")
    private String MAIL_USERNAME;
    @Value("${spring.mail.password}")
    private String MAIL_PASSWORD;

    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditAware();
    }

//    @Bean
//    public Cloudinary cloudinary() {
//        final Map<String, String> config = new HashMap<>();
//        config.put("cloud_name", CLOUD_NAME);
//        config.put("api_key", API_KEY);
//        config.put("api_secret", API_SECRET);
//
//        return new Cloudinary(config);
//    }
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUDINARY_CLOUD_NAME,
                "api_key", CLOUDINARY_API_KEY,
                "api_secret", CLOUDINARY_API_SECRET
        ));
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(MAIL_HOST);
        javaMailSender.setPort(Integer.parseInt(MAIL_PORT));
        javaMailSender.setUsername(MAIL_USERNAME);
        javaMailSender.setPassword(MAIL_PASSWORD);

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");
        return javaMailSender;
    }
}

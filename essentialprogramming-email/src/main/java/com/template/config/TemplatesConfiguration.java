package com.template.config;

import com.template.service.TemplateService;
import com.template.service.ThymeleafTemplateService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TemplatesConfiguration {

    @Bean
    public TemplateService loadTemplateService() {
        return new ThymeleafTemplateService("template");
    }


}

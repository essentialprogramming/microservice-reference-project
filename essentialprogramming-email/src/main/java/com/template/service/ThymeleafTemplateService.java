package com.template.service;

import com.template.model.Template;
import com.template.error.codes.TemplateErrorCode;
import com.lowagie.text.DocumentException;
import com.util.exceptions.ServiceException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class ThymeleafTemplateService implements TemplateService {

    private final TemplateEngine templateEngine;


    public ThymeleafTemplateService(final String templatesLocation) {
        this.templateEngine = init(templatesLocation);
    }

    @Override
    public String generateHTML(Template template, Map<String, Object> contentVariables) {
        return generateHTML(template, contentVariables, Locale.getDefault());
    }

    @Override
    public String generateHTML(Template template, Map<String, Object> contentVariables, Locale locale) {
        final String htmlContent;
        final Context context = new Context(locale);
        template.getFragment().ifPresent((fragment) -> context.setVariable("fragment", fragment));
        template.getPage().ifPresent((page) -> context.setVariable("page", page));
        contentVariables.forEach(context::setVariable); //contentVariables.forEach((key, value) -> context.setVariable(key, value));

        try {
            // If template has no master-template, it will be rendered as it is
            String templateToRender = template.getMaster().orElse(template).getPage().orElseThrow(() -> new ServiceException(TemplateErrorCode.UNABLE_TO_GENERATE_HTML));
            htmlContent = this.templateEngine.process(templateToRender, context);
        } catch (RuntimeException e) {
            throw new ServiceException(TemplateErrorCode.UNABLE_TO_GENERATE_HTML, e);
        }
        if (htmlContent == null) {
            throw new ServiceException(TemplateErrorCode.UNABLE_TO_GENERATE_HTML);
        }
        return htmlContent;
    }

    @Override
    public byte[] generatePDF(Template template, Map<String, Object> content, Locale locale) {
        final String htmlContent = generateHTML(template, content, locale);
        byte[] pdfByteArray;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            pdfByteArray = outputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new ServiceException(TemplateErrorCode.UNABLE_TO_GENERATE_PDF);
        }
        return pdfByteArray;
    }


    private static TemplateEngine init(final String location) {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        final ITemplateResolver templateResolver = createHTMLTemplateResolver(location);
        final ResourceBundleMessageSource messageSource = getMessageSource(location);
        final IDialect dialect = new Java8TimeDialect();

        templateEngine.addTemplateResolver(templateResolver);
        templateEngine.setTemplateEngineMessageSource(messageSource);
        templateEngine.addDialect(dialect);

        return templateEngine;
    }

    //Place the html templates in `resources/'template-location'/html`
    private static ITemplateResolver createHTMLTemplateResolver(final String templateLocation) {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/" + templateLocation + "/");
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        templateResolver.setOrder(1);
        return templateResolver;
    }


    private static ResourceBundleMessageSource getMessageSource(final String location) {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(location + "/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}

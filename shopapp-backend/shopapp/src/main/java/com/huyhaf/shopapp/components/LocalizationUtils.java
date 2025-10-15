package com.huyhaf.shopapp.components;

import java.util.Locale;
import org.springframework.web.servlet.LocaleResolver;

import com.huyhaf.shopapp.utils.WebUtils;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    public String getLocalizedMessage(String messageKey, Object... args){// spread operator
        HttpServletRequest request = WebUtils.getCurrentRequest();
        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey, args, locale);
    }
}

package com.sample.springtraining.handlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sample.springtraining.exception.UrlNotAccessibleException;

// @Component
public class UrlAccessibilityHandler {

    @Value("${app.url:https://dog.ceo/}")
    private String url;

    @EventListener(classes = ContextRefreshedEvent.class)
    public void listen() {
        throw new UrlNotAccessibleException(url);
    }
}

package com.github.snovelli;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;
import java.util.HashSet;

public class WebConfig implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        HashSet<SessionTrackingMode> set = new HashSet<>();
        set.add(SessionTrackingMode.COOKIE);
        servletContext.setSessionTrackingModes(set);

    }

}
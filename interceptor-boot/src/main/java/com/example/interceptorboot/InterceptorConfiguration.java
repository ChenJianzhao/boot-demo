package com.example.interceptorboot;

import org.apache.catalina.connector.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Configuration
public class InterceptorConfiguration extends WebMvcConfigurationSupport{

    Logger logger = LoggerFactory.getLogger(InterceptorConfiguration.class);

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);

        HandlerInterceptor interceptor = new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String authType = request.getAuthType();
                String contextPath = request.getContextPath();
                Cookie[] cookies = request.getCookies();
//                request.getHeader("Accept");
//                request.getHeader("Accept-Encoding");
//                request.getHeader("Cookie");
//                request.getHeader("Host");
//                request.getHeader("User-Agent");
                Enumeration<String> headerNames = request.getHeaderNames();
                String method = request.getMethod();
                String pathInfo = request.getPathInfo();
                String queryString = request.getQueryString();
                String requestSessionId = request.getRequestedSessionId();
                String requestURI = request.getRequestURI();
                logger.info("authType: {}", authType);
                logger.info("contextPath: {}", contextPath);
                logger.info("Cookies: {}", Arrays.asList(cookies).stream().map(Cookie::toString).collect(Collectors.joining(";")));
                logger.info("method: {}", method);
                logger.info("pathInfo: {}", pathInfo);
                logger.info("queryString: {}", queryString);
                logger.info("requestSessionId: {}", requestSessionId);
                logger.info("requestURI: {}", requestURI);
                return true;

            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

            }
        };

        registry.addInterceptor(interceptor);
    }
}

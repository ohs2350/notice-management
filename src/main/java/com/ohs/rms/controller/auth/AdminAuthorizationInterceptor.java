package com.ohs.rms.controller.auth;

import com.ohs.rms.exception.InvalidAuthorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isAdminAnnotation(handler)) {
            checkAdminAuthorization();
            return true;
        }
        return true;
    }

    private boolean isAdminAnnotation(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.getMethodAnnotation(Admin.class) != null;
    }

    private void checkAdminAuthorization() {
        if (false) { // (어드민 권환이 있는지 확인하는 로직)
            throw new InvalidAuthorException();
        }
    }
}

package com.seoultech.synergybe.system.config.login;

import com.seoultech.synergybe.domain.auth.exception.AuthAuthenticationException;
import com.seoultech.synergybe.domain.auth.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isStringClass = String.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isStringClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        checkAuthenticated(authentication);

        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication.getPrincipal();
//
        return jwtAuthentication.userId();
    }

    private void checkAuthenticated(Authentication authentication) {
        if (authentication == null) {
            throw new AuthAuthenticationException("인증되지 않은 사용자입니다.");
        }
    }
}

package com.seoultech.synergybe.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.user.application.UserFacade;
import com.seoultech.synergybe.domain.user.controller.UserController;
import com.seoultech.synergybe.domain.user.repository.UserRefreshTokenFactory;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.service.UserRefreshTokenReader;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.security.JwtUtil;
import com.seoultech.synergybe.system.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        UserController.class
})
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserFacade userFacade;

    @MockBean
    protected UserService userService;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected UserRefreshTokenFactory userRefreshTokenFactory;

    @MockBean
    protected UserRefreshTokenReader userRefreshTokenReader;

    @MockBean
    protected JwtUtil jwtUtil;

    @MockBean
    protected CookieUtil cookieUtil;
}

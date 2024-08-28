package com.seoultech.synergybe.domain.user;

import com.seoultech.synergybe.base.ControllerTestSupport;
import com.seoultech.synergybe.domain.user.dto.request.CreateUserRequest;
import com.seoultech.synergybe.domain.user.dto.request.LoginRequest;
import com.seoultech.synergybe.system.security.JwtAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {

    @DisplayName("email, password로 로그인 한다")
    @WithMockUser(username = "as@gmail.com", password = "whdgns0000", roles = "USER")
    @Test
    void login() throws Exception {
        // given
        String email = "as@gmail.com";
        String password = "whdgns0000";
        LoginRequest loginRequest = new LoginRequest(email, password);

        // login시
//        Mockito.when(userService.)

        mockMvc.perform(
                post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @WithMockUser(username = "as@gmail.com", password = "whdgns0000", roles = "USER")
    @Test
    void testJwtAuthenticationFilter() throws Exception {
        // Given
        String email = "as@gmail.com";
        String password = "whdgns0000";
        LoginRequest loginRequest = new LoginRequest(email, password);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userRefreshTokenReader, userRefreshTokenFactory, cookieUtil);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/v1/users/login");
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setContent("{\"email\":\"as@gmail.com\",\"password\":\"whdgns0000\"}".getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        // When
//        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);

        // Then
//        mockMvc.perform(
//                        post("/api/v1/users/login")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(loginRequest))
//                                .with(csrf())
//                )
//                .andDo(print())
//                .andExpect(status().isOk());
        // 추가적으로, JWT 토큰이 생성되었는지 등 다른 검증 로직도 포함할 수 있습니다.
    }

    @DisplayName("RefreshToken으로 accessToken을 재발행한다")
    @Test
    void generateAccessTokenByRefreshToken() throws Exception {
        // given
        String email = "jonghuncu@gmail.com";
        String password = "password";
        String name = "name";
        String major = "major";
        String userToken = userService.createUser(new CreateUserRequest(email, password, name, major, "123456"));
        User user = userRepository.findByUserToken(userToken).orElseThrow();

        UserRefreshToken refreshToken = new UserRefreshToken(1L);
        userRefreshTokenFactory.save(refreshToken);

        // when
        mockMvc.perform(
                        post("/api/v1/users/refresh-token")
                                .cookie(new Cookie("refreshToken", refreshToken.getRefreshToken().getRefreshToken()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(cookie().value("refreshToken", not(equalTo(refreshToken))))
        ;
    }
}

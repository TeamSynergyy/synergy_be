package com.seoultech.synergybe.domain.user;

import com.seoultech.synergybe.base.ControllerTestSupport;
import com.seoultech.synergybe.system.security.JwtAuthenticationFilter;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.not;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class JwtAuthenticationFilterTest extends ControllerTestSupport {

//    @DisplayName("email, password로 로그인 한다")
//    @WithMockUser(username = "as@gmail.com", password = "whdgns0000", roles = "USER")
//    @Test
//    void login() throws Exception {
//        // given
//        String email = "as@gmail.com";
//        String password = "whdgns0000";
//        LoginRequest loginRequest = new LoginRequest(email, password);
//
//        // login시
////        Mockito.when(userService.)
//
//        mockMvc.perform(
//                post("/api/v1/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest))
//                        .with(csrf())
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//        ;
//    }

    @DisplayName("email, password로 로그인시 인증필터를 거친다.")
    @WithMockUser(username = "as@gmail.com", password = "whdgns0000", roles = "USER")
    @Test
    void testJwtAuthenticationFilter() throws Exception {
        // Given

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userRefreshTokenReader, userRefreshTokenFactory, cookieUtil);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/v1/users/login");
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setContent("{\"email\":\"as@gmail.com\",\"password\":\"whdgns0000\"}".getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // When
        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

//    @DisplayName("RefreshToken으로 accessToken을 재발행한다")
//    void generateAccessTokenByRefreshToken() throws Exception {
//    @Test
//        // given
//        String email = "jonghuncu@gmail.com";
//        String password = "password";
//        String name = "name";
//        String major = "major";
//        String userToken = userService.createUser(new CreateUserRequest(email, password, name, major, "123456"));
//        User user = userRepository.findByUserToken(userToken).orElseThrow();
//
//        UserRefreshToken refreshToken = new UserRefreshToken(1L);
//        userRefreshTokenFactory.save(refreshToken);
//
//        // when
//        mockMvc.perform(
//                        post("/api/v1/users/refresh-token")
//                                .cookie(new Cookie("refreshToken", refreshToken.getRefreshToken().getRefreshToken()))
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isNoContent())
//                .andExpect(cookie().exists("refreshToken"))
//                .andExpect(cookie().value("refreshToken", not(equalTo(refreshToken))))
//        ;
//    }
}

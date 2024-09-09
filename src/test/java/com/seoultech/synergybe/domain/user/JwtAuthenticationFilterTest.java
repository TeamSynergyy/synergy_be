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

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest extends ControllerTestSupport {

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
}

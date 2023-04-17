package com.lettrip.lettripbackend.security.jwt;

import com.lettrip.lettripbackend.security.CustomUserDetailsService;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 1. Cookie에서 JWT token을 꺼낸다.
 * 2. JWT token을 파싱해서 username을 구한다.
 * 3. username으로 User을 구하고 Authentication을 생성한다.
 * 4. 생성된 Authentication을 SecurityContext에 넣고 완료한다.
 * 5. Exception이 발생하면 응답 쿠키를 null로 변경한다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if(StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
                Authentication authentication = getUsernamePasswordAuthenticationToken(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch(Exception e) {
            // 실패하는 경우 쿠키 초기화하기
        }
        filterChain.doFilter(request,response);
    }

    private Authentication getUsernamePasswordAuthenticationToken(String jwt) {
        Long userId = jwtProvider.getUserIdFromToken(jwt);
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}


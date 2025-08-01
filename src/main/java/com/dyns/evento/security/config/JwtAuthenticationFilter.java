package com.dyns.evento.security.config;

import com.dyns.evento.error.responses.ErrorResponse;
import com.dyns.evento.security.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token;
        String userIdentifier;

        if (doesHeaderStartsWithBearer(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            token = authHeader.substring(7);
            userIdentifier = jwtService.extractIdentifier(token);
            authenticateUser(request, userIdentifier, token);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            var error = ErrorResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("Authentication token is invalid or expired.")
                    .build();
            response.getWriter()
                    .write(mapper.writeValueAsString(error));
        }
    }

    private static boolean doesHeaderStartsWithBearer(String header) {
        return header == null || !header.startsWith("Bearer ");
    }

    private void authenticateUser(
            HttpServletRequest request,
            String userIdentifier,
            String token
    ) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userIdentifier);
        if (!jwtService.isTokenValid(token, userDetails)) return;

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

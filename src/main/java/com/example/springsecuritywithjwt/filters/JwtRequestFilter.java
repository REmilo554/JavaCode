package com.example.springsecuritywithjwt.filters;

import com.example.springsecuritywithjwt.service.UserDetailsServiceImpl;
import com.example.springsecuritywithjwt.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsServiceImpl userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    public JwtRequestFilter(UserDetailsServiceImpl userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.debug("No JWT token found in request headers");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authorizationHeader.substring(7);

        String username = null;
        try {
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        } catch (ExpiredJwtException e) {
            logger.error("Could not get username from token", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT token is expired");
            return;
        }catch (Exception e) {
            logger.error("Error while processing JWT token", e);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Invalid JWT token");
            return;
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = userDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                logger.error("Could not load user by username", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            try {
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    logger.info("SecurityContext was created");
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(securityContext);
                    logger.info("SecurityContext was set");
                } else {
                    logger.warn("JWT Token is not valid");
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                logger.error("Something wrong with token", e);
                filterChain.doFilter(request, response);
                return;
            }
        }
        logger.info("JWTRequestFilter is finished work");
        filterChain.doFilter(request, response);
    }
}

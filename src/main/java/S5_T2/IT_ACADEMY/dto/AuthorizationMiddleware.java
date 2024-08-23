package S5_T2.IT_ACADEMY.dto;

import java.io.IOException; // Use java.io.IOException

import S5_T2.IT_ACADEMY.security.JWTTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthorizationMiddleware extends OncePerRequestFilter {

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request); // Ensure resolveToken is defined in JWTTokenProvider

        if (token != null && jwtTokenProvider.validateToken(token)) { // Ensure validateToken is defined
            Authentication auth = jwtTokenProvider.getAuthentication(token); // Ensure getAuthentication is defined
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}

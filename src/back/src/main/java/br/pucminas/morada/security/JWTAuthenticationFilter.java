package br.pucminas.morada.security;

import java.io.IOException;
import java.util.ArrayList;

import br.pucminas.morada.Constants;
import br.pucminas.morada.exceptions.GlobalExceptionHandler;
import br.pucminas.morada.models.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.setAuthenticationFailureHandler(new GlobalExceptionHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {

            User user = Constants.OBJECT_MAPPER.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user.getEmail(), user.getPassword(), new ArrayList<>()
            );

            return this.authenticationManager.authenticate(authToken);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws JsonProcessingException {

        UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal();

        String username = userSpringSecurity.getUsername();
        JWTUtil.Token token = this.jwtUtil.generateToken(username);

        response.addHeader("Authorization", Constants.OBJECT_MAPPER.writeValueAsString(token));
        response.addHeader("access-control-expose-headers", "Authorization");

    }

}

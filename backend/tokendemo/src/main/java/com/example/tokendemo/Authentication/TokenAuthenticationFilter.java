package com.example.tokendemo.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String token_header="Authentication";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String token=request.getHeader(token_header);
        if(token!=null && token.startsWith("Bearer")){
            token=token.replaceFirst("Bearer","");
            String userId=JWT.decode(token).getAudience().get(0);
            JWTVerifier jv=JWT.require(Algorithm.HMAC256("Vini-Passwd")).build();
            jv.verify(token);
            filterChain.doFilter(request,response);
        }else{
            response.setStatus(401);
            response.getWriter().write("{\"errMsg\":\"Not authorized!\"}");
            response.getWriter().close();
        }
    }
}
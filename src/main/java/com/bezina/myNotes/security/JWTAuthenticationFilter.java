package com.bezina.myNotes.security;

import com.bezina.myNotes.services.CustomUserDetailServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


public class JWTAuthenticationFilter extends OncePerRequestFilter {
    public static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private  JWTTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailServices customUserDetailServices;
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {

        try{
            LOG.info("doFilterInternal");
        String jwt = getJwtFromRequest(request);
            LOG.info("getJwtFromRequest = "+jwt);
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            LOG.info("if block");
            Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
            LOG.info("iserID = "+userId.toString());
            UserDetails userDetails = customUserDetailServices.findUserById(userId);
            LOG.info("userDetails "+userDetails.getUsername()+ " pass: "+userDetails.getPassword());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(), userDetails.getPassword(),Collections.emptyList());
                 // userDetails  , null, Collections.emptyList());
            LOG.info("authentication "+ authentication.toString());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        }
        catch (Exception ex){
            LOG.error("Couldn't set user authentication "+ex.getMessage());
        }
        LOG.info("filterChain.doFilter(request, response)");
        try {
            filterChain.doFilter(request, response);
        }
     catch (Exception e)
     {LOG.error(e.getMessage());}
        
    }
    private String getJwtFromRequest(HttpServletRequest request){
        LOG.info("getJwtFromRequest");
        String bearToken = request.getHeader(SecurityConstants.HEADER_STRING);
        LOG.info("bearToken = "+bearToken);
        if(StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstants.TOKEN_PREFIX)){
            LOG.info("token "+bearToken.split(" ")[1]);
            return bearToken.split(" ")[1];}
        else return null;
    }
}

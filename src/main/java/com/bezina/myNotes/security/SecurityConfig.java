package com.bezina.myNotes.security;

import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.services.CustomUserDetailServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity
/*@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        proxyTargetClass = true
     //   jsr250Enabled = true
)*/
public class SecurityConfig {
    Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
   // @Qualifier("customAuthenticationEntryPoint")
    JWTAuthenticationEntryPoint authEntryPoint;
    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private CustomUserDetailServices customUserDetailServices;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            http.authorizeHttpRequests(auth -> auth
                            .requestMatchers(SecurityConstants.SIGN_UP_URLS).permitAll()
                            .anyRequest().authenticated())
                    //  .csrf(httpSecurityCsrfConfigurer::disable)
                    //.cors(AbstractHttpConfigurer::disable)
                    .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable())
                    //for unauthenticated work of methods like post/delete
                    .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                    .sessionManagement(httpSecuritySessionManagementConfigurer ->
                            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                            httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authEntryPoint))
                    .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .httpBasic(Customizer.withDefaults());
            return http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            if (userRepo.findUserByEmail(username).isPresent()) {
                return userRepo.findUserByEmail(username).get();
            }
            throw new UsernameNotFoundException("User " + username + " not found");
        };
    }
  /*  @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    } */


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       CustomUserDetailServices userDetailService)
            throws Exception {
        LOG.info("authenticationManager");
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }
}

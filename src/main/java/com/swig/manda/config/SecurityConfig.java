package com.swig.manda.config;

import com.swig.manda.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private AuthenticationFailureHandler customFailurHandler;
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequest ->
                        authorizationManagerRequest.requestMatchers(HttpMethod.GET, "/member/login/**", "/member/join/**", "/images/**", "css/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/member/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers("/member/loginSuccess").authenticated().anyRequest().permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/member/login")
                        .loginProcessingUrl("/login")
                        // .usernameParameter("userId")
                        .defaultSuccessUrl("/member/loginSuccess").failureHandler(customFailurHandler))
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage("/member/login")
                                .defaultSuccessUrl("http://localhost:9090/member/loginSuccess")
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(principalOauth2UserService)));


        return http.build();
    }


}

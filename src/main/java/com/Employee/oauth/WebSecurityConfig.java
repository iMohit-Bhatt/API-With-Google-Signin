package com.Employee.oauth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    public WebSecurityConfig(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/employee", "/img/**", "/oauth/**")
                .permitAll()

                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                    .defaultSuccessUrl("/employees")
                .and()
                .oauth2Login()
                    .loginPage("/employees")
                    .userInfoEndpoint().userService(oauthUserService)
                    .and()
                    .successHandler(oAuth2LoginSuccessHandler)
//                .defaultSuccessUrl("/employee")
        ;

        http
                .csrf()
                .disable();

        return http.build();
    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception{
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
    @Autowired
    private CustomOAuth2UserService oauthUserService;

    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

}

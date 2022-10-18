package com.Employee.oauth;

import com.Employee.Entity.AuthenticationProvider;
import com.Employee.Entity.Register;
import com.Employee.Service.RegisteredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    @Autowired
    private RegisteredService registeredService;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String name = oAuth2User.getName();
        String pass = oAuth2User.getPass();


        System.out.println(name + " " + email + " " + pass);
        System.out.println("Sucess Handler");
        Register register = registeredService.getRegisterByEmail(email);

        if(register == null){
            registeredService.createNewUserAfterOAuthLoginSuccess(email, name,  AuthenticationProvider.GOOGLE);
                    response.sendRedirect("/employees");



        } else {
            registeredService.updateUserAfterOAuthLoginSuccess(register, name, AuthenticationProvider.GOOGLE);
                    response.sendRedirect("/employees");

        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}

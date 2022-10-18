package com.Employee.Service;

import com.Employee.Dao.RegisterDao;
import com.Employee.Entity.AuthenticationProvider;
import com.Employee.Entity.Register;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Provider;

@Service
public class RegisteredService {

    private RegisterDao registerDao;

    public RegisteredService(RegisterDao registerDao) {
        this.registerDao = registerDao;
    }

    @Transactional
    public Register RegisterUser(Register register){
        return this.registerDao.save(register);
    }


    @Transactional
    public void createNewUserAfterOAuthLoginSuccess(String email, String name, AuthenticationProvider provider) {
        Register register = new Register();
        register.setEmail(email );
        register.setName(name);
        register.setCollege("not Provided");
        register.setPhone("12345");
        register.setPass("admin");
        register.setAuthProvider(provider);

        registerDao.save(register);
        System.out.println("hello");
    }


    public Register getRegisterByEmail(String email) {
        return registerDao.findByEmail(email);
    }

    public void updateUserAfterOAuthLoginSuccess(Register register, String name, AuthenticationProvider provider) {
        register.setName(name);

        register.setAuthProvider(provider);

        registerDao.save(register);

    }

//    @Transactional
//    public void processOAuthPostLogin(String email, String name) {
//        Register existRegister = registerDao.getUserByUsername(email);
//
//        if (existRegister == null) {
//            Register register = new Register();
//            register.setName(name);
//            register.setCollege("not Provided");
//            register.setPhone("12345");
//            register.setPass("admin");
//            register.setAuthProvider(AuthenticationProvider.GOOGLE);
//
//            registerDao.save(register);
//
//            System.out.println("Created new user: " + name);
//        }
//
//    }
}

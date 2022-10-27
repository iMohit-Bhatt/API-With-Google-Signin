# API-With-Google-Signin
I have created an API to Add, Remove and Update User in Database And Added Google OAUTH Authentication by Java - Spring and JPA.

Connection with Database had done successfully. Now we can add or remove the user(tested and working).

Added Some Functionality:-

-Login Functionality,

-Singup Functionality,

-Logout Functionality,

Added all the functionality for Employees API. Client can add there employee details in the database from view like Name, Email, Phone, Salary etc.

Added Some Functionality:-

-Add Employee.

-Remove Employee(Client has to enter the email of the employee that he want to remove from the table)

-Update Employee.

#application running properties DB portNo-3306 and server runnning on port No-8080

#database details using mysql version 8 
Database Name - sms
Tables:-
i) Registered_user -> In this user can Enter Name, Phone, Email, Password, College.
ii) Employee -> In this user can Enter Name, Phone, Email, Salary.

#Used Thymleaf for the UI design, Now user can login by running the url name localhost:8080/login and new user can signup on the application by the same url there they will find a signup button, After clicking on signup button user will get the Registeration form. After Successful login on the application user can see the employee list working in there company or Add, Remove and Update the Employee's.

Implementation:-

1. Created a Package com.Employee inside which we have 5 Packages

->Controller

->Dao

->Entity

->oauth

->Service

i) Controller:-

a) HomeController
package com.Employee.Controller;

import com.Employee.Dao.EmployeeDao;
import com.Employee.Dao.RegisterDao;
import com.Employee.Entity.Employee;
import com.Employee.Entity.Register;
import com.Employee.Service.EmployeeService;
import com.Employee.Service.RegisteredService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private RegisteredService registeredService;
    private RegisterDao registerDao;
    private EmployeeService employeeService;
    private EmployeeDao employeeDao;

    public HomeController(RegisteredService registeredService, RegisterDao     registerDao, EmployeeService employeeService, EmployeeDao employeeDao) {
        this.registeredService = registeredService;
        this.registerDao = registerDao;
        this.employeeService = employeeService;
        this.employeeDao = employeeDao;
    }

    //handler method to handle list students and return mvc
    @RequestMapping(path = "/")
    public String HomePage() {
        System.out.println("*****User is in the Homepage*****");
        return "home";
    }

    //for Registration:-
    @RequestMapping(path = "/processForm", method = RequestMethod.POST)
    public String getInfo(@ModelAttribute("register") Register register, Model model) {


        if (registerDao.existsByEmail(register.getEmail())) {
            System.out.println("*****Email Already Exist*****");
            return "home";
        } else {
            this.registeredService.RegisterUser(register);
            Long id = register.getId();
        }

        return "redirect:/";
    }



    @RequestMapping(path = "/employee", method = RequestMethod.POST)
    public String getLog(@ModelAttribute("register") Register register, Model model) {
        Register register1 = registeredService.getRegisterByEmail(register.getEmail());


        if (!registerDao.existsByEmail(register.getEmail())){
            System.out.println("****Account Not Found****");
            return "redirect:/";
        }

        model.addAttribute("register1", register1);
        System.out.println("Entered-Email "+register1.getEmail());
        System.out.println("User found with a Username = "+register1.getName());
        System.out.println("****Account Found****");
        System.out.println("******User Is in Api Page******");

        model.addAttribute("emp", employeeService.findAllEmp());


        return "Employee";
    }
    

}

b) Api Controller:-

package com.Employee.Controller;

import com.Employee.Dao.EmployeeDao;
import com.Employee.Dao.RegisterDao;
import com.Employee.Entity.Employee;
import com.Employee.Entity.Register;
import com.Employee.Service.EmployeeService;
import com.Employee.Service.RegisteredService;
import com.Employee.oauth.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ApiController {

    private EmployeeService employeeService;
    private EmployeeDao employeeDao;
    private RegisteredService registeredService;
    private RegisterDao registerDao;

    public ApiController(EmployeeService employeeService, EmployeeDao employeeDao, RegisteredService registeredService, RegisterDao registerDao) {
        this.employeeService = employeeService;
        this.employeeDao = employeeDao;
        this.registeredService = registeredService;
        this.registerDao = registerDao;
    }


    @GetMapping("/employees")
    public String listEmployees(Model model, Authentication authentication){
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        model.addAttribute("register1", registeredService.getRegisterByEmail(oAuth2User.getEmail()));
        model.addAttribute("emp", employeeService.findAllEmp());

        return "Employee";
    }

    @RequestMapping(path = "/processAddEmployee", method = RequestMethod.POST)
    public String addEmployee(@ModelAttribute("employee") Employee employee, Model model){

        if (employeeDao.existsByEmail(employee.getEmail())){
            System.out.println("Employee Already Present");
        }
        else
            this.employeeService.RegisterEmployee(employee);
        System.out.println("Employee Added");

        return "redirect:/employees";
    }

    @RequestMapping(path = "/processRemoveEmp")
    public String removeEmployee(@ModelAttribute("employee") Employee employee, Model model){
        this.employeeService.removeEmp(employee.getEmail());
        return "redirect:/employees";
    }

    @RequestMapping(path = "/processUpdate")
    public String updateEmployee(@ModelAttribute("employee") Employee employee, Model model){

        return "redirect:/employee";
    }

}

2. Dao 

a) RegisterDao(Interface)

package com.Employee.Dao;


import com.Employee.Entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterDao extends JpaRepository<Register, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByPass(String pass);
    Register findByEmail(String email);
}


b)EmployeeDao (Interface)

package com.Employee.Dao;

import com.Employee.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Long> {

    void deleteByEmail(String email);
    boolean existsByEmail(String email);
}

3. Entity

a) Register

package com.Employee.Entity;

import javax.persistence.*;

@Entity
@Table(name="registered_user")
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="phone", nullable = true)
    private String phone;

    @Column(name="email")
    private String email;

    @Column(name="pass")
    private String pass;

    @Column(name="college", nullable = true)
    private String college;

    //new
    public AuthenticationProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authProvider;
    //till here

    public Register() {
    }

    public Register(String name, String phone, String email, String pass, String college) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.college = college;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    @Override
    public String toString() {
        return "Register{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", college='" + college + '\'' +
                '}';
    }
}

b) Employee

package com.Employee.Entity;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email" )
    private String email;

    @Column(name= "salary")
    private String salary;

    public Employee() {
    }

    public Employee(String name, String phone, String email, String salary) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}

c) AuthenticationProvider (enum)
package com.Employee.Entity;

public enum AuthenticationProvider {
    LOCAL, GOOGLE
}

4. OAUTH

a) CustomOAuth2User

package com.Employee.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;


public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;

    public CustomOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }


    public String getEmail(){
        return oAuth2User.getAttribute("email");
    }

    public String getPass(){
        return oAuth2User.getAttribute("password");
    }


}


b) CustomOAuth2UserService

package com.Employee.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new CustomOAuth2User(user);
    }
}


c) OAuth2LoginSuccessHandler

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

d) WebSecurityConfig

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

4. Service 

a) RegisterService

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

}

b) EmployeeService

package com.Employee.Service;

import com.Employee.Dao.EmployeeDao;
import com.Employee.Entity.Employee;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EmployeeService {


    private EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Transactional
    public Employee RegisterEmployee(Employee employee){
        return this.employeeDao.save(employee);
    }

    public List<Employee> findAllEmp(){
        return employeeDao.findAll();
    }

    @Transactional
    public void removeEmp(String email){
        this.employeeDao.deleteByEmail(email);
    }

}


5. EmployeeApplication

package com.Employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

public class EmployeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeApplication.class, args);
	}

}


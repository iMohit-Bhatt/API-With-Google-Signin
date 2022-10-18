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

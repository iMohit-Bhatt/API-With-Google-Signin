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


    public HomeController(RegisteredService registeredService, RegisterDao registerDao, EmployeeService employeeService, EmployeeDao employeeDao) {
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

    //For LogIn Page---------(*) (*)---------------

//    @RequestMapping(path = "/loginProcess", method = RequestMethod.POST)
//    public String getLog(@ModelAttribute("register") Register register, Model model) {
//
////        Register register1 = registeredService.getR
////
////
////          egisterByEmail(register.getEmail());
////        String name = register1.getName();
////        String email = register1.getEmail();
////        System.out.println("Name = " + name);
////        System.out.println("Email = " + email);
//
//
//        if (registerDao.existsByEmail(register.getEmail())) {
//            if (registerDao.existsByPass(register.getPass())) {
//
//                model.addAttribute("name", register.getEmail());
//
//                System.out.println("****Account Found******");
//                return "redirect:/employee";
//            }
//            else {
//                System.out.println("Wrong password");
//            }
//        } else {
//            System.out.println("user not found");
//        }
//
//        return "redirect:/";
//
//
//    }

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





//    @RequestMapping("/about")
//    public String about(@ModelAttribute("register") Register register, Model model){
//        Register register1 = registeredService.getRegisterByEmail(register.getEmail());
//        String name = register1.getName();
//        String email = register1.getEmail();
//        System.out.println("Name = " + name);
//        System.out.println("Email = " +email);
//
//        model.addAttribute("name", name);
//        model.addAttribute("name", email);
//
//        return "/about";
//    }


}
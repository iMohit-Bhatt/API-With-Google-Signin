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

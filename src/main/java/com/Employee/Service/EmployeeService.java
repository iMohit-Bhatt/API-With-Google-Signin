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

package com.Employee.Dao;

import com.Employee.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Long> {

    void deleteByEmail(String email);
    boolean existsByEmail(String email);


}

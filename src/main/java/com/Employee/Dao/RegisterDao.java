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

//    @Query("SELECT u FROM Register u WHERE u.name = :name")
//    public Register getUserByUsername(@Param("name") String name);



}

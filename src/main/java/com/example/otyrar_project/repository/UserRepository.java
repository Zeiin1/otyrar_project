package com.example.otyrar_project.repository;

import com.example.otyrar_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String > {

    public User findUserByEmail(String email);
    public User findUserById(String id);
    public User findUserByName(String name);
}

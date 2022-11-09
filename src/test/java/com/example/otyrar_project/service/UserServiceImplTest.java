package com.example.otyrar_project.service;

import com.example.otyrar_project.entity.User;
import com.example.otyrar_project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public  void checkMethodOfUserRepository()
    {
        String email = "eroma@gmail.com";
        User user = userRepository.findUserByEmail(email);
        System.out.println(user.getName());
    }

}
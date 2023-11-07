package com.example.otyrar_project.service;

import com.example.otyrar_project.entity.Book;
import com.example.otyrar_project.entity.Role;
import com.example.otyrar_project.entity.User;
import com.example.otyrar_project.repository.UserRepository;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Autowired BookService bookService;

    @Test
    public  void checkMethodOfUserRepository()
    {
        String id = "63f376e7f9980f60b222227e";
        User user = userService.findUserById(id);
        System.out.println(user.getName());
    }
    /*    @Test
        public void testBookingBack()
        {
            String id = "65493a1e194b6256eaf1d4fa";
            String bookID = "63f3911b6a0f41032a056f49";
            boolean res = userService.borrowBook(id,bookID);
            Assertions.assertTrue(res);
        }*/

    @Test
    public void checkInsert()
    {
        String name = "dadada";
        String surName = "dadada";

       Long phone = 774723153021L;

      String password = "love2222G";

         String email = "hello@gmail.com";


         List<Book> books = bookService.getAllBooks();
         User user = User.builder().name(name)
                 .surname(surName)
                 .email(email)
                 .password(password)
                 .phone(phone)
                 .books(books)
                 .build();
        userService.save(user);

    }

}
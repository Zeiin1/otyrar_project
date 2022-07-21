package com.example.otyrar_project.controller;

import com.example.otyrar_project.entity.Book;
import com.example.otyrar_project.entity.User;
import com.example.otyrar_project.service.BookService;
import com.example.otyrar_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ControllerMain {
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;

    @GetMapping()
    public String indexPage()
    {
        return "index";
    }


    @GetMapping("/registration")
    public String registarUser(@ModelAttribute("user")User user)
    {
        return "reg";
    }
    @PostMapping("/registration")
    public String saveUserToDB(@ModelAttribute("user")User user, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return "reg";
        }
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage(@ModelAttribute("user")User user)
    {
        return "signup";
    }





    @GetMapping("/profile/{id}")
    public String profilePage(@PathVariable("id")String id, Model model)
    {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "profile";

    }



    @GetMapping("/user_info/{id}")
    public String userInformationPage(@PathVariable("id")String id,Model model)
    {
        model.addAttribute("user",userService.findUserById(id));
        return "backprofile";
    }

    @GetMapping("/admin")
    public String getAllUsersAdminPage(Model model)
    {
        model.addAttribute("user",userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/books")
    public String showAllBooks(Model model)
    {
       model.addAttribute("book", bookService.getAllBooks());
       return "booksPage";
    }

    @GetMapping("/add_new_book")
    public String addNewBookPage(@ModelAttribute("book")Book book)
    {
        return "addBook";
    }
    @PostMapping("add_new_book")
    public String addNewBookToDB(@ModelAttribute("book")Book book)
    {
        bookService.save(book);
        return "redirect:/admin";
    }



}

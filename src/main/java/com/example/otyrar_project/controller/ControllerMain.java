package com.example.otyrar_project.controller;

import com.example.otyrar_project.entity.Book;
import com.example.otyrar_project.entity.User;
import com.example.otyrar_project.service.BookService;
import com.example.otyrar_project.service.ElasticsearchService;
import com.example.otyrar_project.service.UserService;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;



@Controller
public class ControllerMain {
    Logger logger= LoggerFactory.getLogger(ControllerMain.class);
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ElasticsearchService elasticsearchService;

    @GetMapping("/process-elasticsearch-data")
    public String processElasticsearchData() throws IOException {
        logger.info("calculating elasticsearch data");
        elasticsearchService.processElasticsearchData();
        return "index"; // Or return a view name or response as needed
    }

    @GetMapping()
    public String indexPage() {
        logger.info("opened index page");
        return "index";
    }


    @GetMapping("/registration")
    public String registarUser(@ModelAttribute("user") User user)
    {

        return "reg";
    }

    @PostMapping("/registration")
    public String saveUserToDB(@ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("user "+ user.getId() + " is already exist");
            return "reg";
        }
        logger.info("user " + user.getName() + " has registrated successfully");
        userService.save(user);
        logger.info("redirecting to the login page");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage(@ModelAttribute("user") User user)
    {
        logger.info("login page is opened");
        return "signup";
    }


    @GetMapping("/profile")
    public String profilePage(Model model, Authentication authentication) throws ParseException {
        logger.info("user went from login successfully");

        User user = userService.findUserByEmail(authentication.getName());
        logger.info("user "+ user.getName() + " opened profile page");
        List<Book> books = user.getBooks();

        model.addAttribute("user", user);
        logger.info("displaying user personal info");
        if(books == null || books.size() == 0)
        {
            Date date = new Date();


            Book book = new Book("1004281","Info about borrowed book will be there","Author name"
                    ,1999,false,
                    date);
            model.addAttribute("book",book);
            model.addAttribute("days",5);
        }
        else {
           Book book = books.get(0);
         //  int maxDays = book.getReturnDate().getDay();
            int maxDays = 23;
            LocalDate localDate = LocalDate.now();
            int today = localDate.getDayOfMonth();
            int leftedDays = maxDays - today;

           model.addAttribute("book",book);
            model.addAttribute("days",leftedDays);
        }
        return "profile";

    }


    @GetMapping("/user_info/{id}")
    public String userInformationPage(@PathVariable("id") String id, Model model) {
        logger.info("user with id " + id + " opens personal page");
        User user = userService.findUserById(id);

        model.addAttribute("user", user);
        System.out.println(user.getName());
        return "backprofile";
    }

    @GetMapping("/deleteMe/{id}")
    public String deleteUser(@PathVariable("id") String id) {

        userService.deleteUser(id);
        return "redirect:/";
    }

    @PostMapping("/user_info/{id}")
    public String updateUser(@PathVariable("id") String id,@ModelAttribute("user") User user)
    {
        User user1 = userService.updateUser(user);
        System.out.println(user1);

        return "redirect:/user_info/"+ user.getId();
    }

    @GetMapping("/admin")
    public String getAllUsersAdminPage(Model model)
    {
        logger.info("admin has login to the system");
        model.addAttribute("user",userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/library/books/{id}")
    public String showAllBooks(Model model,@PathVariable("id")String id)
    {
        logger.info("opened books page");
        User user =userService.findUserById(id);
        model.addAttribute("user",user);
       model.addAttribute("book", bookService.getAllBooks());
       return "booksPage";
    }

    @GetMapping("/admin/add_new_book")
    public String addNewBookPage(@ModelAttribute("book")Book book)
    {
        logger.info("opened a book page");
        return "addBook";
    }
    @PostMapping("/admin/add_new_book")
    public String addNewBookToDB(@ModelAttribute("book")Book book)
    {
        logger.info("book" + book.getName() + " was created in the system");
        bookService.save(book);
        return "redirect:/admin";
    }

    @GetMapping("/books/borrow")
    public String borrowBook(Model model,@ModelAttribute("id")String id,@ModelAttribute("bookId")String bookId) throws ParseException {
       Book book =  userService.borrowBook(id,bookId);
       if(book != null) {
           logger.info("user with id " + id + " borrowed a book with id " + bookId);
           User user = userService.findUserById(id);
           model.addAttribute("user", user);
           model.addAttribute("book", book);


           model.addAttribute("days", 14);
           return "profile";
       }
        return "redirect:/library/book/"+id;
    }




}

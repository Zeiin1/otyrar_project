package com.example.otyrar_project.service;

import com.example.otyrar_project.entity.Book;
import com.example.otyrar_project.entity.Role;
import com.example.otyrar_project.entity.User;
import com.example.otyrar_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private BookService bookService;



    @Override
    public void save(User user) {
        if(user.getEmail().equals("zeyin03@gmail.com") && user.getName().equals("zeiin"))
        {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));
            userRepository.insert(user);
        }
        else {

            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setRoles(Arrays.asList(new Role("ROLE_USER")));

            userRepository.insert(user);
        }

    }

   /* @Override
    public boolean provenAccount(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if(BCrypt.checkpw(password,user.getPassword()))
            return true;
        else return false;
    }*/

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserById(String id) {
        User user =  userRepository.findUserById(id);
       return user;

    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findUserById(id);
        userRepository.delete(user);
        System.out.println("User "+user.getEmail()+ " was deleted from service");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        return  new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
    public User updateUser(User user)
    {

        User user1 = mongoTemplate.save(user);

        return user1;
    }

    @Override
    public Book borrowBook(String id, String bookId) throws ParseException {
       Book book =  bookService.findById(bookId);
       if(book!=null && book.isAvailable())
       {
           LocalDate localDate = LocalDate.now();
           localDate.plusDays(14L);
           SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
           Date date = sdf.parse(localDate.getMonthValue() + "/"+localDate.getDayOfMonth()+"/"+localDate.getYear());

           book.setReturnDate(date);

           book.setAvailable(false);
           List<Book> books = new ArrayList<>();
           books.add(book);
           User user = findUserById(id);
           user.setBooks(books);
           mongoTemplate.save(user);
           mongoTemplate.save(book);
           return  book;
       }
       else return null;
    }

}

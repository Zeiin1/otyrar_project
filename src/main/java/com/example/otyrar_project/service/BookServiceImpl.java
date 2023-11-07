package com.example.otyrar_project.service;

import com.example.otyrar_project.entity.Book;
import com.example.otyrar_project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void save(Book book) {
        book.setAvailable(true);
        bookRepository.insert(book);
    }

    @Override
    public Book findById(String bookId) {
        return bookRepository.findBookById(bookId);


    }
}

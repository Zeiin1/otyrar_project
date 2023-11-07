package com.example.otyrar_project.service;

import com.example.otyrar_project.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();

    void save(Book book);
    Book findById(String bookId);
}

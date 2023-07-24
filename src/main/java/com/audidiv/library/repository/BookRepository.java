package com.audidiv.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.audidiv.library.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
    public List<Book> findById(long id);
}

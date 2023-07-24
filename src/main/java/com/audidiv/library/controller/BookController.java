package com.audidiv.library.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.audidiv.library.dto.request.RequestCreateBookDto;
import com.audidiv.library.model.Book;
import com.audidiv.library.repository.BookRepository;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private BookRepository bookRepository;
    
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping("create")
    public ResponseEntity<String> createBook(@RequestBody RequestCreateBookDto request){
        Book newBook = new Book();
        newBook.setTitle(request.getTitle());
        newBook.setAuthor(request.getAuthor());
        newBook.setDescription(request.getDescription());
        newBook.setCreatedAt(new Date());

        bookRepository.save(newBook);
        return new ResponseEntity<String>("Book has been added", HttpStatus.OK);
    }

    @GetMapping("list")
    public ResponseEntity<List<Book>> getListBook(){
        List<Book> books = bookRepository.findAll();
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }
}

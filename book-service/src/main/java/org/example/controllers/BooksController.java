package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.example.model.Category;
import org.example.model.dto.BookDto;
import org.example.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books/api")
public class BooksController {

    private final BookService bookService;


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody @Valid BookDto bookDto) {

        return bookService.createBook(bookDto);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public BookDto updateBook(@RequestBody @Valid BookDto bookDto) {

        return bookService.updateBook(bookDto);
    }

    @GetMapping("/book/{author}/{title}")
    public BookDto findBookByAuthorAndTitle(@PathVariable("author") String author,
                                            @PathVariable("title") String title) {

        return bookService.findBookByTitleAndAuthor(author, title);

    }

    @GetMapping("/books/{category}")
    public List<Book> findAllBooksByCategory(@PathVariable("category") String category) {

        return bookService.findAllBooksByCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") String id) {

        Book book = bookService.findById(id);

        bookService.deleteBook(book);

        return ResponseEntity.ok("Book is deleted successfully");
    }


}

package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private static final String BOOKHASH = "books";
    private static final String AUTHOR_AND_TITLE_HASH = "authorAndTitleHash";

    private final HashOperations hashOperations;


    @Autowired
    public BookRepository(RedisTemplate<String, Book> redisTemplate) {

        this.hashOperations = redisTemplate.opsForHash();

    }

    public Book save(Book book) {

        String id = (book.getId() == null || book.getId().isEmpty()) ? UUID.randomUUID().toString() : book.getId();
        book.setId(id);

        String category = book.getCategory().getName();
        String authorAndTitle = book.getAuthor() + "-" + book.getTitle();

        hashOperations.put(BOOKHASH, id, book);
        hashOperations.put(AUTHOR_AND_TITLE_HASH, authorAndTitle, book);

        return book;
    }

    public Book getById(String id) {

        Book savedBook = (Book) hashOperations.get(BOOKHASH, id);

        if (savedBook != null) {
            return savedBook;
        } else {
            throw new NoSuchElementException();
        }
    }

    public List<Book> getAll() {

        Set<String> bookMap = hashOperations.keys(BOOKHASH);

        List<Book> bookList = new ArrayList<>();
        bookMap.forEach(x -> {
            Book book = getById(x);
            bookList.add(book);
        });
        return bookList;
    }

    public List<Book> getALLByCategory(String category) {

        List<Book> bookList = getAll();

        return bookList.stream().filter(x -> x.getCategory().getName().equals(category)).toList();

    }

    public Book getByAuthorAndTitle(String author, String title) {

        String authorAndTitle = author + "-" + title;

        Book book = (Book) hashOperations.get(AUTHOR_AND_TITLE_HASH, authorAndTitle);

        if (book != null) {
            return book;
        } else {
            throw new NoSuchElementException();
        }
    }

    public void delete(String id) {

        Book book = getById(id);
        String authorAndTitle = book.getAuthor() + "-" + book.getTitle();

        hashOperations.delete(BOOKHASH, id);
        hashOperations.delete(AUTHOR_AND_TITLE_HASH, authorAndTitle, book);

    }


}

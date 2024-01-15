package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.mapper.BooksMapper;
import org.example.model.Book;
import org.example.model.dto.BookDto;
import org.example.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final BooksMapper mapper;


    @Caching(evict = {@CacheEvict(value = "search_book", key = "#dto.getAuthor() + '-' + #dto.getTitle()", beforeInvocation = true),
            @CacheEvict(value = "search_books", key = "#dto.getCategory()")}
    )
    public BookDto createBook(BookDto dto) {

        Book book = mapper.fromBookDtoToBook(dto);

        Book savedBook = bookRepository.save(book);

        return mapper.fromBookToBookDto(savedBook);

    }

    @Caching(evict = {@CacheEvict(value = "search_book", key = "#dto.getAuthor() + '-' + #dto.getTitle()", beforeInvocation = true),
            @CacheEvict(value = "search_books", key = "#dto.getCategory()", beforeInvocation = true)}
    )
    public BookDto updateBook(BookDto dto) {

        Book book = bookRepository.getById(dto.getId());

        bookRepository.delete(book.getId());

        Book updatedBook = bookRepository.save(mapper.fromBookDtoToBook(dto));

        return mapper.fromBookToBookDto(updatedBook);

    }

    @Caching(evict = {@CacheEvict(value = "search_book", key = "#book.getAuthor() + '-' + #book.getTitle()", beforeInvocation = true),
            @CacheEvict(value = "search_books", key = "#book.getCategory()")}
    )
    public void deleteBook(Book book) {


        if (book != null) {

            bookRepository.delete(book.getId());
        }
    }

    public Book findById(String id) {

        return bookRepository.getById(id);
    }

    @Cacheable(value = "search_book", key = "#author + '-' + #title")
    public BookDto findBookByTitleAndAuthor(String author, String title) {

        Book book = bookRepository.getByAuthorAndTitle(author, title);

        return mapper.fromBookToBookDto(book);
    }

    @Cacheable(value = "search_books", key = "#category")
    public List<Book> findAllBooksByCategory(String category) {

        return bookRepository.getALLByCategory(category);

    }
}

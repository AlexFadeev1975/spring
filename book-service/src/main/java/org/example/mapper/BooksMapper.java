package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.example.model.dto.BookDto;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class BooksMapper {

    private final ModelMapper mapper;

    public Book fromBookDtoToBook(BookDto dto) {

        mapper.typeMap(BookDto.class, Book.class).addMapping(BookDto::getCategory, Book::addCategory);
        return mapper.map(dto, Book.class);
    }

    public BookDto fromBookToBookDto(Book book) {

        mapper.typeMap(Book.class, BookDto.class).addMapping(Book::getStringCategory, BookDto::setCategory);
        return mapper.map(book, BookDto.class);
    }
}

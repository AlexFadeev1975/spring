package org.example.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.dto.CategoryDto;
import org.example.dto.CommentDto;
import org.example.dto.NewsDto;
import org.example.dto.NewsDtoComCount;
import org.example.dto.UserDto;
import org.example.model.Category;
import org.example.model.Comment;
import org.example.model.News;
import org.example.model.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-23T05:30:31+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 20.0.2.1 (Amazon.com Inc.)"
)
@Component
public class NewsMapperImpl implements NewsMapper {

    @Override
    public User userDtoToUser(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        if ( dto.getId() != null ) {
            user.setId( Long.parseLong( dto.getId() ) );
        }
        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );

        return user;
    }

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( String.valueOf( user.getId() ) );
        userDto.setFirstName( user.getFirstName() );
        userDto.setLastName( user.getLastName() );

        return userDto;
    }

    @Override
    public News newsDtoToNews(NewsDto dto) {
        if ( dto == null ) {
            return null;
        }

        News news = new News();

        if ( dto.getId() != null ) {
            news.setId( Long.parseLong( dto.getId() ) );
        }
        news.setText( dto.getText() );
        if ( dto.getUserId() != null ) {
            news.setUserId( Long.parseLong( dto.getUserId() ) );
        }
        news.setCreatedTime( dto.getCreatedTime() );
        news.setUpdatedTime( dto.getUpdatedTime() );
        news.setCategory( dto.getCategory() );
        List<Comment> list = dto.getComments();
        if ( list != null ) {
            news.setComments( new ArrayList<Comment>( list ) );
        }

        return news;
    }

    @Override
    public NewsDto newsToNewsDto(News news) {
        if ( news == null ) {
            return null;
        }

        NewsDto.NewsDtoBuilder newsDto = NewsDto.builder();

        if ( news.getId() != null ) {
            newsDto.id( String.valueOf( news.getId() ) );
        }
        newsDto.text( news.getText() );
        if ( news.getUserId() != null ) {
            newsDto.userId( String.valueOf( news.getUserId() ) );
        }
        newsDto.createdTime( news.getCreatedTime() );
        newsDto.updatedTime( news.getUpdatedTime() );
        newsDto.category( news.getCategory() );
        List<Comment> list = news.getComments();
        if ( list != null ) {
            newsDto.comments( new ArrayList<Comment>( list ) );
        }

        return newsDto.build();
    }

    @Override
    public NewsDtoComCount newsToNewsDtoComCount(News news) {
        if ( news == null ) {
            return null;
        }

        NewsDtoComCount newsDtoComCount = new NewsDtoComCount();

        newsDtoComCount.setComments( toCountComments( news.getComments() ) );
        if ( news.getId() != null ) {
            newsDtoComCount.setId( String.valueOf( news.getId() ) );
        }
        newsDtoComCount.setText( news.getText() );
        if ( news.getUserId() != null ) {
            newsDtoComCount.setUserId( String.valueOf( news.getUserId() ) );
        }
        newsDtoComCount.setCreatedTime( news.getCreatedTime() );
        newsDtoComCount.setUpdatedTime( news.getUpdatedTime() );
        newsDtoComCount.setCategory( news.getCategory() );

        return newsDtoComCount;
    }

    @Override
    public Comment commentDtoToComment(CommentDto dto) {
        if ( dto == null ) {
            return null;
        }

        Comment comment = new Comment();

        if ( dto.getId() != null ) {
            comment.setId( Long.parseLong( dto.getId() ) );
        }
        comment.setText( dto.getText() );
        if ( dto.getUserId() != null ) {
            comment.setUserId( Long.parseLong( dto.getUserId() ) );
        }
        comment.setCreatedTime( dto.getCreatedTime() );
        comment.setUpdatedTime( dto.getUpdatedTime() );
        comment.setNews( dto.getNews() );

        return comment;
    }

    @Override
    public CommentDto commentToCommentDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        commentDto.setId( String.valueOf( comment.getId() ) );
        commentDto.setText( comment.getText() );
        if ( comment.getUserId() != null ) {
            commentDto.setUserId( String.valueOf( comment.getUserId() ) );
        }
        commentDto.setCreatedTime( comment.getCreatedTime() );
        commentDto.setUpdatedTime( comment.getUpdatedTime() );
        commentDto.setNews( comment.getNews() );

        return commentDto;
    }

    @Override
    public Category categoryDtoToCategory(CategoryDto dto) {
        if ( dto == null ) {
            return null;
        }

        Category category = new Category();

        if ( dto.getId() != null ) {
            category.setId( Long.parseLong( dto.getId() ) );
        }
        category.setCategoryName( dto.getCategoryName() );
        List<News> list = dto.getNews();
        if ( list != null ) {
            category.setNews( new ArrayList<News>( list ) );
        }

        return category;
    }

    @Override
    public CategoryDto categoryToCategoryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId( String.valueOf( category.getId() ) );
        categoryDto.setCategoryName( category.getCategoryName() );
        List<News> list = category.getNews();
        if ( list != null ) {
            categoryDto.setNews( new ArrayList<News>( list ) );
        }

        return categoryDto;
    }
}

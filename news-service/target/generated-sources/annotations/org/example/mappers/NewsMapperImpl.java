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
    date = "2024-02-29T16:44:37+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19 (Oracle Corporation)"
)
@Component
public class NewsMapperImpl implements NewsMapper {

    @Override
    public User userDtoToUser(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setRole( toUserRole( dto.getRole() ) );
        if ( dto.getId() != null ) {
            user.setId( Long.parseLong( dto.getId() ) );
        }
        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );
        user.setEmail( dto.getEmail() );
        user.setPassword( dto.getPassword() );

        return user;
    }

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        if ( user.getId() != null ) {
            userDto.id( String.valueOf( user.getId() ) );
        }
        userDto.firstName( user.getFirstName() );
        userDto.lastName( user.getLastName() );
        userDto.email( user.getEmail() );
        userDto.password( user.getPassword() );
        if ( user.getRole() != null ) {
            userDto.role( user.getRole().name() );
        }

        return userDto.build();
    }

    @Override
    public News newsDtoToNews(NewsDto dto) {
        if ( dto == null ) {
            return null;
        }

        News.NewsBuilder news = News.builder();

        if ( dto.getId() != null ) {
            news.id( Long.parseLong( dto.getId() ) );
        }
        news.text( dto.getText() );
        if ( dto.getUserId() != null ) {
            news.userId( Long.parseLong( dto.getUserId() ) );
        }
        news.createdTime( dto.getCreatedTime() );
        news.updatedTime( dto.getUpdatedTime() );
        news.category( dto.getCategory() );
        List<Comment> list = dto.getComments();
        if ( list != null ) {
            news.comments( new ArrayList<Comment>( list ) );
        }

        return news.build();
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

        Comment.CommentBuilder comment = Comment.builder();

        if ( dto.getId() != null ) {
            comment.id( Long.parseLong( dto.getId() ) );
        }
        comment.text( dto.getText() );
        if ( dto.getUserId() != null ) {
            comment.userId( Long.parseLong( dto.getUserId() ) );
        }
        comment.createdTime( dto.getCreatedTime() );
        comment.updatedTime( dto.getUpdatedTime() );
        comment.news( dto.getNews() );

        return comment.build();
    }

    @Override
    public CommentDto commentToCommentDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto.CommentDtoBuilder commentDto = CommentDto.builder();

        if ( comment.getId() != null ) {
            commentDto.id( String.valueOf( comment.getId() ) );
        }
        commentDto.text( comment.getText() );
        if ( comment.getUserId() != null ) {
            commentDto.userId( String.valueOf( comment.getUserId() ) );
        }
        commentDto.createdTime( comment.getCreatedTime() );
        commentDto.updatedTime( comment.getUpdatedTime() );
        commentDto.news( comment.getNews() );

        return commentDto.build();
    }

    @Override
    public Category categoryDtoToCategory(CategoryDto dto) {
        if ( dto == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        if ( dto.getId() != null ) {
            category.id( Long.parseLong( dto.getId() ) );
        }
        category.categoryName( dto.getCategoryName() );
        List<News> list = dto.getNews();
        if ( list != null ) {
            category.news( new ArrayList<News>( list ) );
        }

        return category.build();
    }

    @Override
    public CategoryDto categoryToCategoryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto.CategoryDtoBuilder categoryDto = CategoryDto.builder();

        if ( category.getId() != null ) {
            categoryDto.id( String.valueOf( category.getId() ) );
        }
        categoryDto.categoryName( category.getCategoryName() );
        List<News> list = category.getNews();
        if ( list != null ) {
            categoryDto.news( new ArrayList<News>( list ) );
        }

        return categoryDto.build();
    }
}

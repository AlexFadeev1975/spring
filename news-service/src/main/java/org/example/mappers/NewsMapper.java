package org.example.mappers;

import org.example.dto.*;
import org.example.model.Category;
import org.example.model.Comment;
import org.example.model.News;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {


    User userDtoToUser(UserDto dto);

    UserDto userToUserDto(User user);

    News newsDtoToNews(NewsDto dto);

    NewsDto newsToNewsDto(News news);

    @Named("toCountComments")
    default Integer toCountComments(List<Comment> comments) {

        return comments.size();
    }

    default List<NewsDto> toListNewsDto(List<News> newsList) {

        return newsList.stream().map(this::newsToNewsDto).toList();
    }


    @Mapping(source = "comments", target = "comments", qualifiedByName = "toCountComments")
    public NewsDtoComCount newsToNewsDtoComCount(News news);


    Comment commentDtoToComment(CommentDto dto);

    CommentDto commentToCommentDto(Comment comment);

    Category categoryDtoToCategory(CategoryDto dto);

    CategoryDto categoryToCategoryDto(Category category);
}

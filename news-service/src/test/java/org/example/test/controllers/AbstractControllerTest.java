package org.example.test.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.example.controllers.UserController;
import org.example.dto.NewsDto;
import org.example.dto.UserDto;
import org.example.model.Category;
import org.example.model.Comment;
import org.example.model.News;
import org.example.model.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AbstractControllerTest {


  protected ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  protected MockMvc mockMvc;

    protected Category createCategory (Long id, List<News> listNews) {

        Category category = new Category(id, "Category " + id, listNews);

        if (!listNews.isEmpty()) {

            listNews.forEach(news -> news.setCategory(category));
            category.setNews(listNews);
        }

        return category;
    }

    protected User createUser (Long id, String firstName, String lastName) {



        return new User(id, firstName, lastName);
    }
    protected UserDto createUserDto (String id, String firstName, String lastName) {



        return new UserDto (id, firstName, lastName);
    }

    protected UserDto createUserDtoRequest (String firstName, String lastName) {

        return new UserDto(firstName, lastName);
    }



    protected NewsDto createNewsDto (Long id, Long userId, LocalDateTime createdTime, LocalDateTime updatedTime, Category category, List<Comment> listComments) {

               return new NewsDto(String.valueOf(id), "News " + id, String.valueOf(userId), createdTime, updatedTime, "Category " + id, category,
                       listComments);
   }
    protected Comment createComment (Long id, Long userId, LocalDateTime createdTime, LocalDateTime updatedTime, News news) {

        Comment comment = new Comment(id, "Comment " + id, userId, createdTime, updatedTime, news);

        if (news != null) {

            List<Comment> commentList = news.getComments();
            commentList.add(comment);
            news.setComments(commentList);
            comment.setNews(news);
        }
        return comment;
    }
        protected String loadJsonUserDtoResponse (String fileName) throws IOException {

           File inputFile = new File(fileName);

           return objectMapper.writeValueAsString(objectMapper.readValue(inputFile, UserDto.class));
       }

    protected String loadJsonUserResponse (String fileName) throws IOException {

        File inputFile = new File(fileName);

        return objectMapper.writeValueAsString(objectMapper.readValue(inputFile, new TypeReference<List<User>>() {
        }));
    }
    protected String loadJsonNewsDtoResponse (String fileName) throws IOException {

        File inputFile = new File(fileName);
        objectMapper.registerModule(new JSR310Module());

        return objectMapper.writeValueAsString(objectMapper.readValue(inputFile, NewsDto.class));
    }


}

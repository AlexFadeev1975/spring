package org.example.aspects;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.dto.CommentDto;
import org.example.dto.NewsDto;
import org.example.dto.UserDto;
import org.example.exceptions.NotAccessException;
import org.example.model.Comment;
import org.example.model.News;
import org.example.model.User;
import org.example.model.enums.RoleType;
import org.example.repository.CommentRepository;
import org.example.repository.NewsRepository;
import org.example.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class ApprovingBeforeOperation {

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    @Before("execution(* org.example.services.NewsService.update(..)) && args(dto)")
    public void doCheckBeforeUpdateNews(NewsDto dto) {

        News news = newsRepository.findById(Long.valueOf(dto.getId())).orElseThrow();

        if (!getUserFromRequest().getId().equals(news.getUserId()))
            throw new NotAccessException("Доступ к операции запрещен");
    }

    @Before("execution(* org.example.services.CommentService.update(..)) && args(dto)")
    public void doCheckBeforeUpdateComment(CommentDto dto) {

        Comment comment = commentRepository.findById(Long.valueOf(dto.getId())).orElseThrow();

        if (!getUserFromRequest().getId().equals(comment.getUserId()))
            throw new NotAccessException("Доступ к операции запрещен");
    }

    @Before("execution(* org.example.services.CommentService.delete(..)) && args(commentId)")
    public void doCheckBeforeDeleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (!getUserFromRequest().getId().equals(comment.getUserId()))
            throw new NotAccessException("Доступ к операции запрещен");
    }

    @Before("execution(* org.example.services.UserService.findById(..)) && args(id)")
    public void doCheckBeforeFindUserById(Long id) {

        User user = getUserFromRequest();
        RoleType role = user.getRole();
        if (role == null) throw new NotAccessException("Доступ к операции запрещен");
        if (role.equals(RoleType.ROLE_USER)
                & !Objects.equals(user.getId(), id)) throw new NotAccessException("Доступ к операции запрещен");
    }

    @Before("execution(* org.example.services.UserService.update(..)) && args(dto)")
    public void doCheckBeforeUpdateUser(UserDto dto) {

        User user = getUserFromRequest();
        RoleType role = user.getRole();
        if (role == null) throw new NotAccessException("Доступ к операции запрещен");
        if (role.equals(RoleType.ROLE_USER)
                & !Objects.equals(user.getId(), dto.getId()))
            throw new NotAccessException("Доступ к операции запрещен");
    }


    @Before("execution(* org.example.services.UserService.delete(..)) && args(id)")
    public void doCheckBeforeDeleteUser(Long id) {

        User user = getUserFromRequest();
        RoleType role = user.getRole();
        if (role == null) throw new NotAccessException("Доступ к операции запрещен");
        if (role.equals(RoleType.ROLE_USER)
                & !Objects.equals(user.getId(), id)) throw new NotAccessException("Доступ к операции запрещен");
    }

    private User getUserFromRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        var authentication = (Authentication) request.getUserPrincipal();
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

    }
}

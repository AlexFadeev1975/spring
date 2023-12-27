package org.example.aspects;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.dto.CommentDto;
import org.example.dto.NewsDto;
import org.example.exceptions.NotAccessException;
import org.example.model.Comment;
import org.example.model.News;
import org.example.repository.CommentRepository;
import org.example.repository.NewsRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class ApprovingBeforeOperation {

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;


    @Before("execution(* org.example.services.NewsService.update(..)) && args(dto)")
    public void doCheckBeforeUpdateNews(NewsDto dto) {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        Long userId = Long.parseLong(request.getHeader("userId"));
        News news = newsRepository.findById(Long.valueOf(dto.getId())).orElseThrow();

        if (!userId.equals(news.getUserId())) throw new NotAccessException("Доступ к операции запрещен");
    }

    @Before("execution(* org.example.services.CommentService.update(..)) && args(dto)")
    public void doCheckBeforeUpdateComment(CommentDto dto) {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        Long userId = Long.parseLong(request.getHeader("userId"));
        Comment comment = commentRepository.findById(Long.valueOf(dto.getId())).orElseThrow();

        if (!userId.equals(comment.getUserId())) throw new NotAccessException("Доступ к операции запрещен");
    }

    @Before("execution(* org.example.services.NewsService.delete(..)) && args(id)")
    public void doCheckBeforeDeleteNews(Long id) {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String userId = request.getHeader("userId");

        News news = newsRepository.findById(id).orElseThrow();

        if (!userId.equals(String.valueOf(news.getUserId())))
            throw new NotAccessException("Доступ к операции запрещен");
    }

    @Before("execution(* org.example.services.CommentService.delete(..)) && args(commentId)")
    public void doCheckBeforeDeleteComment(Long commentId) {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String userId = request.getHeader("userId");

        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (!userId.equals(String.valueOf(comment.getUserId())))
            throw new NotAccessException("Доступ к операции запрещен");
    }


}

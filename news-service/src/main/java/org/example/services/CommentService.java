package org.example.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.CommentDto;
import org.example.dto.NewsDto;
import org.example.mappers.NewsMapper;
import org.example.model.Comment;
import org.example.model.News;
import org.example.model.User;
import org.example.repository.CommentRepository;
import org.example.repository.NewsRepository;
import org.example.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    private final NewsMapper mapper;

    @Transactional
    @PreAuthorize("isAuthorized()")
    public CommentDto create(CommentDto dto, Long newsId) {

        News news = newsRepository.findById(newsId).orElseThrow();
        List<Comment> commentList = news.getComments();
        Comment newComment = mapper.commentDtoToComment(dto);
        newComment.setUserId(getUserIdFromPrincipal());
        newComment.setCreatedTime(LocalDateTime.now());
        newComment.setUpdatedTime(LocalDateTime.now());
        commentList.add(newComment);
        news.setComments(commentList);
        newsRepository.save(news);


        return mapper.commentToCommentDto(getLastComment(news));
    }

    @PreAuthorize("isAuthorized()")
    public List<Comment> findAll(Long newsId) {

        Optional<News> news = newsRepository.findById(newsId);

        return (news.isPresent()) ? news.get().getComments() : new ArrayList<>();
    }

    @PreAuthorize("authentication.principal.id == #userId")
    public CommentDto approvedUpdate(CommentDto dto, Long userId) {

        Comment comment = commentRepository.findById(Long.parseLong(dto.getId())).orElseThrow();

        comment.setUpdatedTime(LocalDateTime.now());
        comment.setText(dto.getText());

        return mapper.commentToCommentDto(commentRepository.save(comment));
    }

    @PreAuthorize("authentication.principal.id == #userId || hasRole('ROLE_MODERATOR')")
    public boolean delete(Long commentId, Long userId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow();

        News news = newsRepository.findAll().stream().filter(x -> {
            return x.getComments().contains(comment);
        }).findFirst().orElseThrow();
        List<Comment> commentList = news.getComments();
        commentList.remove(comment);
        news.setComments(commentList);
        newsRepository.save(news);

        return (commentRepository.findById(commentId).isEmpty());
    }

    Comment getLastComment(News news) {

        List<Comment> commentList = newsRepository.findById(news.getId()).get().getComments();

        return commentList.stream().max(Comparator.comparing(Comment::getCreatedTime)).get();

    }

    private Long getUserIdFromPrincipal() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        var authentication = (Authentication) request.getUserPrincipal();
        var userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return user.getId();

    }

    public Long getUserIdFromComment(String id) {

        Comment comment = commentRepository.findById(Long.valueOf(id)).orElseThrow();

        return comment.getUserId();
    }
}

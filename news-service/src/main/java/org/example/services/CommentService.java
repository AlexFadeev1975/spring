package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.CommentDto;
import org.example.mappers.NewsMapper;
import org.example.model.Comment;
import org.example.model.News;
import org.example.repository.CommentRepository;
import org.example.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    private final NewsMapper mapper;

    @Transactional
    public CommentDto create(CommentDto dto, Long newsId, Long userId) {

        News news = newsRepository.findById(newsId).orElseThrow();
        List<Comment> commentList = news.getComments();
        Comment newComment = mapper.commentDtoToComment(dto);
        newComment.setUserId(userId);
        newComment.setCreatedTime(LocalDateTime.now());
        newComment.setUpdatedTime(LocalDateTime.now());
        commentList.add(newComment);
        news.setComments(commentList);
        newsRepository.save(news);


        return mapper.commentToCommentDto(getLastComment(news));
    }

    public List<Comment> findAll(Long newsId) {

        Optional<News> news = newsRepository.findById(newsId);

        return (news.isPresent()) ? news.get().getComments() : new ArrayList<>();
    }


    public CommentDto approvedUpdate(CommentDto dto) {

        Comment comment = commentRepository.findById(Long.parseLong(dto.getId())).orElseThrow();

        comment.setUpdatedTime(LocalDateTime.now());
        comment.setText(dto.getText());

        return mapper.commentToCommentDto(commentRepository.save(comment));
    }

    public boolean delete(Long commentId) {

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
}

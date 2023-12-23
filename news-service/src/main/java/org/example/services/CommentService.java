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

    @Transactional
    public CommentDto create (CommentDto dto, String newsId, String userId) {

           News news = newsRepository.findById(Long.parseLong(newsId)).orElseThrow();
       List<Comment> commentList = news.getComments();
    Comment newComment = NewsMapper.INSTANCE.commentDtoToComment(dto);
    newComment.setUserId(Long.parseLong(userId));
    newComment.setCreatedTime(LocalDateTime.now());
    newComment.setUpdatedTime(LocalDateTime.now());
       commentList.add(newComment);
           news.setComments(commentList);
           newsRepository.save(news);


    return NewsMapper.INSTANCE.commentToCommentDto(getLastComment(news));
    }

    public List<Comment> findAll (String newsId) {

        Optional<News> news = newsRepository.findById(Long.parseLong(newsId));

        return (news.isPresent()) ? news.get().getComments() : new ArrayList<>();
    }



    public CommentDto approvedUpdate (CommentDto dto) {

        Comment comment = commentRepository.findById(Long.parseLong(dto.getId())).orElseThrow();

        comment.setUpdatedTime(LocalDateTime.now());
        comment.setText(dto.getText());

        return NewsMapper.INSTANCE.commentToCommentDto(commentRepository.save(comment));
    }

    public void delete (String commentId) {

        Comment comment = commentRepository.findById(Long.parseLong(commentId)).orElseThrow();

        commentRepository.delete(comment);

    }

    Comment getLastComment (News news) {

        List<Comment> commentList = newsRepository.findById(news.getId()).get().getComments();

        return commentList.stream().max(Comparator.comparing(Comment::getCreatedTime)).get();

    }
}

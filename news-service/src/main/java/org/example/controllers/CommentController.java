package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.CommentDto;
import org.example.model.Comment;
import org.example.services.CommentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create/{news_id}")
    public CommentDto create(@PathVariable("news_id") String newsId,
                             @RequestBody @Valid CommentDto dto,
                             HttpServletRequest request) {
        String userId = request.getHeader("userId");

        return commentService.create(dto, newsId, userId);
    }

    @GetMapping("/list/{news_id}")
    public List<Comment> getComments(@PathVariable("news_id") String newsId) {

        return commentService.findAll(newsId);
    }

    @PutMapping("/update")
    public CommentDto updateComment(@RequestBody CommentDto dto) {

        return commentService.approvedUpdate(dto);
    }

    @DeleteMapping("/delete/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable("comment_id") String commentId) {

        commentService.delete(commentId);

        return ResponseEntity.ok("Comment успешно удален");
    }
}

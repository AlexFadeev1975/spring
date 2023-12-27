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
    public CommentDto create(@PathVariable("news_id") Long newsId,
                             @RequestBody @Valid CommentDto dto,
                             HttpServletRequest request) {
        Long userId = Long.valueOf(request.getHeader("userId"));

        return commentService.create(dto, newsId, userId);
    }

    @GetMapping("/list/{news_id}")
    public List<Comment> getComments(@PathVariable("news_id") Long newsId) {

        return commentService.findAll(newsId);
    }

    @PutMapping("/update")
    public CommentDto updateComment(@RequestBody CommentDto dto) {

        return commentService.approvedUpdate(dto);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) {


        return (commentService.delete(commentId)) ? ResponseEntity.ok("Comment успешно удален")
                : (ResponseEntity<?>) ResponseEntity.notFound();
    }
}

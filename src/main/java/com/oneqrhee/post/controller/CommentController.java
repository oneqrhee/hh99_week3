package com.oneqrhee.post.controller;

import com.oneqrhee.post.dto.comment.CommentRequestDto;
import com.oneqrhee.post.dto.comment.CommentResponseDto;
import com.oneqrhee.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comment/{postId}")
    public List<CommentResponseDto> getComments(@PathVariable Long postId){
        return commentService.getComments(postId);
    }

    @PostMapping("/auth/comment")
    public ResponseEntity<String> createComment(@RequestBody CommentRequestDto commentRequestDto, @RequestParam Long postId){
        return commentService.createComment(commentRequestDto, postId);
    }

    @PutMapping("/auth/comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto){
        return commentService.updateComment(commentRequestDto, commentId);
    }

    @DeleteMapping("/auth/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }
}

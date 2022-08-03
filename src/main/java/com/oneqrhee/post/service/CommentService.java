package com.oneqrhee.post.service;

import com.oneqrhee.post.dto.comment.CommentRequestDto;
import com.oneqrhee.post.dto.comment.CommentResponseDto;
import com.oneqrhee.post.entity.Comment;
import com.oneqrhee.post.entity.Post;
import com.oneqrhee.post.repository.CommentRepository;
import com.oneqrhee.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Post getPost(Long postId) {
        return postRepository.getReferenceById(postId);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long postId) {
        Post post = getPost(postId);
        List<Comment> comments = new ArrayList<>(commentRepository.findAllByPost(post));
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponseDtos.add(new CommentResponseDto(comment));
        }
        return commentResponseDtos;
    }

    @Transactional
    public ResponseEntity<String> createComment(CommentRequestDto commentRequestDto, Long postId) {
        Post post = getPost(postId);
        String nickname = SecurityContextHolder.getContext().getAuthentication().getName();
        commentRepository.save(Comment.builder()
                .post(post)
                .content(commentRequestDto.getContent())
                .author(nickname)
                .build());
        return new ResponseEntity<>("댓글이 등록되었습니다", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> updateComment(CommentRequestDto commentRequestDto, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다"));
        if(!comment.getAuthor().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            return new ResponseEntity<>("작성자만 수정할 수 있습니다", HttpStatus.UNAUTHORIZED);
        }
        comment.updateComment(commentRequestDto);
        commentRepository.save(comment);
        return new ResponseEntity<>("댓글이 수정되었습니다", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다"));
        if(!comment.getAuthor().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            return new ResponseEntity<>("작성자만 삭제할 수 있습니다", HttpStatus.UNAUTHORIZED);
        }
        commentRepository.deleteById(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

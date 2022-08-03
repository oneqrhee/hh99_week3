package com.oneqrhee.post.controller;

import com.oneqrhee.post.dto.post.PostRequestDto;
import com.oneqrhee.post.dto.post.PostResponseDto;
import com.oneqrhee.post.dto.post.PostsResponseDto;
import com.oneqrhee.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @GetMapping("/post")
    public List<PostsResponseDto> getPosts() {
       return postService.findAllByOrderByModifiedAtDesc();
    }

    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PostMapping("/auth/post")
    public ResponseEntity<String> createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

    @PutMapping("/auth/post/{id}")
    public ResponseEntity<String> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        return postService.updatePost(id, postRequestDto);
    }

    @DeleteMapping("/auth/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }
}

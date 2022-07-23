package com.oneqrhee.post.controller;

import com.oneqrhee.post.dto.PostRequestDto;
import com.oneqrhee.post.dto.PostResponseDto;
import com.oneqrhee.post.dto.PostUpdateRequestDto;
import com.oneqrhee.post.dto.PostsResponseDto;
import com.oneqrhee.post.entity.Post;
import com.oneqrhee.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @GetMapping("/api/post")
    public List<PostsResponseDto> getPosts() {
       return postService.findAllByOrderByModifiedAtDesc();
    }

    @GetMapping("/api/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PostMapping("/api/post")
    public Post createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

    @PutMapping("/api/post/{id}")
    public ResponseEntity<String> updatePost(@PathVariable Long id, @RequestParam String password,
                                             @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
        return postService.updatePost(id, password, postUpdateRequestDto);
    }

    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @RequestParam String password) {
        return postService.deletePost(id, password);
    }
}

package com.oneqrhee.post.controller;

import com.oneqrhee.post.dto.PostRequestDto;
import com.oneqrhee.post.dto.PostResponseDto;
import com.oneqrhee.post.dto.PostsResponseDto;
import com.oneqrhee.post.entity.Post;
import com.oneqrhee.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("")
    public List<PostsResponseDto> getPosts() {
       return postService.findAllByOrderByModifiedAtDesc();
    }

    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PostMapping("")
    public ResponseEntity<String> createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> checkPassword(@PathVariable Long id, @RequestParam String password){
        return postService.checkPassword(id, password);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        return postService.updatePost(id, postRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }
}

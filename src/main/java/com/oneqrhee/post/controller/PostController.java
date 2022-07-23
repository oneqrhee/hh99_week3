package com.oneqrhee.post.controller;

import com.oneqrhee.post.dto.PostRequestDto;
import com.oneqrhee.post.dto.PostResponseDto;
import com.oneqrhee.post.dto.PostUpdateRequestDto;
import com.oneqrhee.post.dto.PostsResponseDto;
import com.oneqrhee.post.entity.Post;
import com.oneqrhee.post.repository.PostRepository;
import com.oneqrhee.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostRepository postRepository;
    private final PostService postService;

    @GetMapping("/api/post")
    public List<PostsResponseDto> getPosts() {
        List<Post> postList = new ArrayList<>(postRepository.findAllByOrderByModifiedAtDesc());
        List<PostsResponseDto> postsList = new ArrayList<>();
        for (Post post : postList) {
            postsList.add(new PostsResponseDto(post));
        }
        return postsList;
    }

    @PostMapping("/api/post")
    public Post createPost(@RequestBody PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        return postRepository.save(post);
    }

    @GetMapping("/api/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Post가 존재하지 않습니다."));
        return new PostResponseDto(post);
    }

    @PutMapping("/api/post/{id}")
    public ResponseEntity<String> updatePost(@PathVariable Long id, @RequestParam String password,
                                             @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
        int authorized = postService.checkPassword(id, password);
        if (authorized == 1) {
            return new ResponseEntity<>("비밀번호가 틀렸습니다", HttpStatus.UNAUTHORIZED);
        }
        postService.updatePost(id, postUpdateRequestDto);
        return new ResponseEntity<>("글이 수정되었습니다", HttpStatus.OK);
    }

    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id, @RequestParam String password) {
        int authorized = postService.checkPassword(id, password);
        if (authorized == 1) {
            return new ResponseEntity<>("비밀번호가 틀렸습니다", HttpStatus.UNAUTHORIZED);
        }
        postRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

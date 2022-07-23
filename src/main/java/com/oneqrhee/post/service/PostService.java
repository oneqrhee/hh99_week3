package com.oneqrhee.post.service;

import com.oneqrhee.post.dto.PostRequestDto;
import com.oneqrhee.post.dto.PostResponseDto;
import com.oneqrhee.post.dto.PostUpdateRequestDto;
import com.oneqrhee.post.dto.PostsResponseDto;
import com.oneqrhee.post.entity.Post;
import com.oneqrhee.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllByOrderByModifiedAtDesc() {
        List<Post> postList = new ArrayList<>(postRepository.findAllByOrderByModifiedAtDesc());
        List<PostsResponseDto> postsResponseDtoList = new ArrayList<>();
        for (Post post : postList) {
            postsResponseDtoList.add(new PostsResponseDto(post));
        }
        return postsResponseDtoList;
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        return new PostResponseDto(post);
    }

    @Transactional
    public Post createPost(PostRequestDto postRequestDto){
        Post post = new Post(postRequestDto);
        return postRepository.save(post);
    }

    @Transactional
    public ResponseEntity<String> updatePost(Long id, String password, PostUpdateRequestDto postUpdateRequestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다"));
        int authorized = checkPassword(id, password);
        if (authorized == 1) {
            return new ResponseEntity<>("비밀번호가 틀렸습니다", HttpStatus.UNAUTHORIZED);
        }
        post.updatePost(postUpdateRequestDto);
        return new ResponseEntity<>("글이 수정되었습니다", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deletePost(Long id, String password){
        int authorized = checkPassword(id, password);
        if (authorized == 1) {
            return new ResponseEntity<>("비밀번호가 틀렸습니다", HttpStatus.UNAUTHORIZED);
        }
        postRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional(readOnly = true)
    public int checkPassword(Long id, String password) {
        int authorized = 0;
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다"));
        if (!post.getPassword().equals(password)) {
            authorized = 1;
        }
        return authorized;
    }
}

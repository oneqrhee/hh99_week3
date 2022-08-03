package com.oneqrhee.post.service;

import com.oneqrhee.post.dto.post.PostRequestDto;
import com.oneqrhee.post.dto.post.PostResponseDto;
import com.oneqrhee.post.dto.post.PostsResponseDto;
import com.oneqrhee.post.entity.Post;
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
    public ResponseEntity<String> createPost(PostRequestDto postRequestDto){
        Post post = new Post(postRequestDto);
        postRepository.save(post);
        return new ResponseEntity<>("글이 작성되었습니다", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> updatePost(Long id, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다"));
        if(!post.getAuthor().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            return new ResponseEntity<>("작성자만 수정할 수 있습니다", HttpStatus.UNAUTHORIZED);
        }
        post.updatePost(postRequestDto);
        return new ResponseEntity<>("글이 수정되었습니다", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다"));
        if (!post.getAuthor().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return new ResponseEntity<>("작성자만 삭제할 수 있습니다", HttpStatus.UNAUTHORIZED);
        }
        postRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

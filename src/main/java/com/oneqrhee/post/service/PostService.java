package com.oneqrhee.post.service;

import com.oneqrhee.post.dto.PostUpdateRequestDto;
import com.oneqrhee.post.entity.Post;
import com.oneqrhee.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Long updatePost(Long id, PostUpdateRequestDto postUpdateRequestDto){
        Post post = postRepository.findById(id).orElseThrow(
        () ->new IllegalArgumentException("해당 아이디가 존재하지 않습니다"));
        post.updatePost(postUpdateRequestDto);
        return post.getId();
    }

    public int checkPassword(Long id, String password){
        int authorized = 0;
        Post post = postRepository.findById(id).orElseThrow(
                () ->new IllegalArgumentException("해당 아이디가 존재하지 않습니다"));
        if(!post.getPassword().equals(password)){
            authorized = 1;
        }
        return authorized;
    }
}

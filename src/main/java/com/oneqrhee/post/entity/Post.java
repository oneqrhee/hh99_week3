package com.oneqrhee.post.entity;

import com.oneqrhee.post.dto.PostRequestDto;
import com.oneqrhee.post.dto.PostUpdateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Post extends Timestamped{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String password;

    public Post(PostRequestDto postRequestDto){
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.author = postRequestDto.getAuthor();
        this.password = postRequestDto.getPassword();
    }

    public void updatePost(PostUpdateRequestDto postUpdateRequestDto){
        this.content = postUpdateRequestDto.getContent();
    }
}

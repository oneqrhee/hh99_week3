package com.oneqrhee.post.repository;

import com.oneqrhee.post.entity.Comment;
import com.oneqrhee.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}

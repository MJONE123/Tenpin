package com.example.Tenpin.service;

import com.example.Tenpin.entity.BoardPost;
import com.example.Tenpin.repository.BoardPostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoardPostService {

    private final BoardPostRepository repository;

    public BoardPostService(BoardPostRepository repository) {
        this.repository = repository;
    }

    public BoardPost createPost(BoardPost post) {
        return repository.save(post);
    }

    public List<BoardPost> getPosts() {
        return repository.findAll();
    }

    public Optional<BoardPost> getPostById(Long postId) {
        return repository.findById(postId);
    }

    public boolean updatePost(BoardPost updatedPost) {
        Optional<BoardPost> existingPost = repository.findById(updatedPost.getPostId());
        if (existingPost.isPresent()) {
            BoardPost post = existingPost.get();
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setUpdatedAt(LocalDateTime.now());
            repository.save(post);
            return true;
        }
        return false;
    }

    public boolean deletePost(Long postId, String authorId) {
        Optional<BoardPost> existingPost = repository.findById(postId);
        if (existingPost.isPresent() && existingPost.get().getAuthorId().equals(authorId)) {
            repository.deleteById(postId);
            return true;
        }
        return false;
    }
}

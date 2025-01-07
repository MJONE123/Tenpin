package com.example.Tenpin.repository;

import com.example.Tenpin.entity.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    // 특정 사용자가 작성한 게시글을 조회
    List<BoardPost> findByAuthorId(String authorId);
}

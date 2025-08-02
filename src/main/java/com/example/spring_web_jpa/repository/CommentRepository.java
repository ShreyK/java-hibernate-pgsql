package com.example.spring_web_jpa.repository;

import com.example.spring_web_jpa.dto.ParentCommentDTO;
import com.example.spring_web_jpa.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Page<Comment> findByParentCommentIsNull(Pageable pageable);
    public Page<Comment> findByParentCommentId(Long parentCommentId, Pageable pageable);

    @Query("SELECT new com.example.spring_web_jpa.dto.ParentCommentDTO(c.id, c.content, c.likes)" +
            "FROM Comment c WHERE c.parentComment is NULL")
    public Page<ParentCommentDTO> findAllParentComments(Pageable pageable);

}

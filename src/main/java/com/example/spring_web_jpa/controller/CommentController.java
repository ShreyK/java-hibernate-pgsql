package com.example.spring_web_jpa.controller;

import com.example.spring_web_jpa.dto.CommentDTO;
import com.example.spring_web_jpa.dto.ParentCommentDTO;
import com.example.spring_web_jpa.entity.Comment;
import com.example.spring_web_jpa.exception.ResourceNotFoundException;
import com.example.spring_web_jpa.repository.CommentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public Page<ParentCommentDTO> getComments(
            @PageableDefault(sort = "likes", direction = Sort.Direction.DESC) Pageable pageable) {
        return commentRepository.findAllParentComments(pageable);
    }

    @GetMapping("/{parentCommentId}")
    public Page<Comment> getReplies(@PathVariable Long parentCommentId,
                                    @PageableDefault(sort = "likes", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comment> optionalComment = commentRepository.findByParentCommentId(parentCommentId, pageable);
        if (optionalComment.isEmpty()) {
            throw new ResourceNotFoundException("Comment not found");
        }
        return optionalComment;
    }

    @PostMapping
    public Comment saveComment(@Valid  @RequestBody CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setLikes(commentDTO.getLikes());
        comment.setContent(commentDTO.getContent());
        if (commentDTO.getParentCommentId() != null) {
            Optional<Comment> parentComment = commentRepository.findById(commentDTO.getParentCommentId());
            if (parentComment.isEmpty()) {
                throw new ResourceNotFoundException("Parent comment not found");
            }
            comment.setParentComment(parentComment.get());
        }
        return commentRepository.save(comment);
    }

    @PutMapping("/{commentId}")
    public Comment updateComment(@PathVariable Long commentId,
                                 @Valid  @RequestBody CommentDTO commentRequest) {
        return commentRepository.findById(commentId)
                .map(comment -> {
                    comment.setContent(commentRequest.getContent());
                    comment.setLikes(commentRequest.getLikes());
                    return commentRepository.save(comment);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new ResourceNotFoundException("Comment not found");
        }
        commentRepository.deleteById(commentId);
        return ResponseEntity.ok().build();
    }
}

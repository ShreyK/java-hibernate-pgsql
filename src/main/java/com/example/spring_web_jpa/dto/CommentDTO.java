package com.example.spring_web_jpa.dto;

public class CommentDTO {
    private String content;
    private int likes;
    private Long parentCommentId;

    public String getContent() {
        return content;
    }

    public int getLikes() {
        return likes;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }
}

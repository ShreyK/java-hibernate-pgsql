package com.example.spring_web_jpa.dto;

public class ParentCommentDTO {
    private String content;
    private int likes;
    private Long id;

    public ParentCommentDTO(Long id, String content, int likes) {
        this.id = id;
        this.content = content;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public int getLikes() {
        return likes;
    }

    public String  getContent() {
        return content;
    }

}

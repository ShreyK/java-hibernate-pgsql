package com.example.spring_web_jpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Entity
@Table(name = "comments")
public class Comment extends BaseModel {

    @Id
    @GeneratedValue(generator = "comment_generator")
    @SequenceGenerator(name = "comment_generator", sequenceName = "comment_generator", initialValue = 1)
    private Long id;

    @NotBlank
    @Column(name ="content", nullable = false, columnDefinition = "VARCHAR(100)")
    @Size(min = 3, max = 100)
    private String content;

    @Column(name = "likes", nullable = false, columnDefinition = "INT")
    private Integer likes = 0;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonBackReference
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> replies;

    public Long getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public Integer getLikes() {
        return likes;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public List<Comment> getReplies() {
        return replies;
    }
}

package com.example.spring_web_jpa.repository;

import com.example.spring_web_jpa.dto.ParentCommentDTO;
import com.example.spring_web_jpa.entity.BaseModel;
import com.example.spring_web_jpa.entity.Comment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest
@Import(BaseModel.class)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void saveAndFindById() {
        Comment comment = new Comment();
        comment.setContent("Test");
        comment.setLikes(5);

        Comment savedComment = commentRepository.save(comment);

        Assertions.assertThat(savedComment.getId()).isNotNull();

        Comment fetchedComment = commentRepository.findById(savedComment.getId()).orElse(null);
        Assertions.assertThat(fetchedComment).isNotNull();
        Assertions.assertThat(fetchedComment.getContent()).isEqualTo("Test");
        Assertions.assertThat(fetchedComment.getLikes()).isEqualTo(5);
    }

    @Test
    public void testFindParentCommentsPageable() {
        for (int i = 0; i < 20; i++) {
            Comment comment = new Comment();
            comment.setContent("Test Parent " + i);
            comment.setLikes(i);
            commentRepository.save(comment);
        }

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "likes"));
        Page<ParentCommentDTO> commentPage = commentRepository.findAllParentComments(pageRequest);

        Assertions.assertThat(commentPage).isNotNull();
        Assertions.assertThat(commentPage.getContent().size()).isEqualTo(5);
        Assertions.assertThat(commentPage.getContent().get(0).getLikes()).isGreaterThan(commentPage.getContent().get(1).getLikes());
    }

    @Test
    public void testFindRepliesPageable() {
        Comment parent = new Comment();
        parent.setLikes(10);
        parent.setContent("Test Parent");
        commentRepository.save(parent);

        for (int i = 0; i < 20; i++) {
            Comment comment = new Comment();
            comment.setContent("Test Child " + i);
            comment.setLikes(i);
            comment.setParentComment(parent);
            commentRepository.save(comment);
        }

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "likes"));
        Page<Comment> comments = commentRepository.findByParentCommentId(parent.getId(), pageRequest);
        Assertions.assertThat(comments.getContent().size()).isEqualTo(3);
        Assertions.assertThat(comments.getContent().get(0).getLikes()).isEqualTo(19);
        Assertions.assertThat(comments.getContent().get(1).getLikes()).isEqualTo(18);
        Assertions.assertThat(comments.getContent().get(2).getLikes()).isEqualTo(17);
    }
}

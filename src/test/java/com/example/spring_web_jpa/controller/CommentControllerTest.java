package com.example.spring_web_jpa.controller;

import com.example.spring_web_jpa.dto.ParentCommentDTO;
import com.example.spring_web_jpa.repository.CommentRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentRepository commentRepository;

    @Test
    public void getTopLevelComments() throws Exception {
        ParentCommentDTO dto1 = new ParentCommentDTO(1L, "First comment", 15);
        ParentCommentDTO dto2 = new ParentCommentDTO(2L, "Second comment", 10);
        ParentCommentDTO dto3 = new ParentCommentDTO(3L, "Third comment", 5);

        List<ParentCommentDTO> dtos = List.of(dto1, dto2, dto3);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "likes"));
        Page<ParentCommentDTO> page = new PageImpl<>(dtos, pageable, dtos.size());

        Mockito.when(commentRepository.findAllParentComments(Mockito.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "likes, desc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].likes", Matchers.is(15)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].content", Matchers.is("First comment")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].id", Matchers.is(3)));
    }

}

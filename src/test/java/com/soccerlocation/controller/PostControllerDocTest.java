package com.soccerlocation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soccerlocation.domain.Post;
import com.soccerlocation.repository.PostRepository;
import com.soccerlocation.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.soccerlocation.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {


    /**
     * 원래라면 세팅을 해줘야한다.
     * 근데 내용을 보면 mockMvc를 초기화해주는내용이다
     * 그러면 그냥 AutoConfigureMockMvc 로 mockMvc 주입해주자.
     */
//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation))
//                .build();
//    }


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("글 조회")
    void test1() throws Exception {

        Post post = Post.builder()
                        .title("제목")
                                .content("내용")
                                        .build();

        postRepository.save(post);

        /**
         * accept :: 이 유형의 값만 받겠다
         *  pathParameters 에 대한 설명을 해준다.
         *  ResponseFields  (identifier 를 통해 build 안에 있는 폴더 분기)
         *   -> 응답 값
         *      id : 1,
         *     title : ~
         *     content : ~
         */
        // expected
        this.mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/posts/{postId}", 1L)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-inquiry", pathParameters(
                        parameterWithName("postId").description("게시글 아이디"))
                , responseFields(
                        fieldWithPath("id").description("게시글 ID"),
                        fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("글 등록")
    void test2() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("호돌맨")
                .content("바보")
                .build();


        String json = objectMapper.writeValueAsString(request);


        // expected
        this.mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/posts")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("제목").optional(),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }
}

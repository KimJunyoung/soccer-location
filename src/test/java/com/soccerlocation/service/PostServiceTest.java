package com.soccerlocation.service;

import com.soccerlocation.domain.Post;
import com.soccerlocation.exception.PostNotFound;
import com.soccerlocation.repository.PostRepository;
import com.soccerlocation.request.PostCreate;
import com.soccerlocation.request.PostEdit;
import com.soccerlocation.request.PostSearch;
import com.soccerlocation.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목 입니다.")
                .content("내용 입니다.")
                .build();

        // when

        postService.write(postCreate);

        // then

        assertThat(1L).isEqualTo(postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {

        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse post = postService.get(requestPost.getId());

        // then
        assertNotNull(post);
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());
    }


    @Test
    @DisplayName("글 여러개 조회")
    void test3() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("호돌맨 제목 "  + i)
                            .content("반포자이 "  + i)
                            .build();
                }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        assertEquals(10, posts.size());
        assertEquals("호돌맨 제목 19", posts.get(0).getTitle());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        // 수정
        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 없습니다. id = " + post.getId()));

        assertThat(changedPost.getTitle()).isEqualTo("호돌걸");
        assertThat(changedPost.getContent()).isEqualTo("반포자이");
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() throws Exception {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        // 수정
        PostEdit postEdit = PostEdit.builder()
                .title("호돌맨")
                .content("초가집")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 없습니다. id = " + post.getId()));

        assertThat(changedPost.getContent()).isEqualTo("초가집");
    }

    @Test
    @DisplayName("글 내용 수정")
    void test6() throws Exception {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        // 수정
        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("초가집")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 없습니다. id = " + post.getId()));

        assertThat(changedPost.getTitle()).isEqualTo("호돌맨");
        assertThat(changedPost.getContent()).isEqualTo("초가집");
    }

    @Test
    @DisplayName("게시글 삭제")
    void test7(){
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertThat(postRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("글 1개 조회  존재하지 않는 글 ")
    void test8() {

        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // expected
        assertThatThrownBy(() -> postService.get(post.getId() + 1L)).isInstanceOf(PostNotFound.class);

    }

    @Test
    @DisplayName("글 삭제 - 존재하지 않는 글")
    void test9() {

        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

                // expected
        assertThatThrownBy(() -> postService.delete(post.getId() + 1L)).isInstanceOf(PostNotFound.class);

    }



}
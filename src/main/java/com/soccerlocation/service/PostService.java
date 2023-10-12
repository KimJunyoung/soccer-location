package com.soccerlocation.service;

import com.soccerlocation.domain.Post;
import com.soccerlocation.domain.PostEditor;
import com.soccerlocation.exception.PostNotFound;
import com.soccerlocation.repository.PostRepository;
import com.soccerlocation.request.PostCreate;
import com.soccerlocation.request.PostEdit;
import com.soccerlocation.request.PostSearch;
import com.soccerlocation.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    /**
     * 글 등록, 글 단건 조회, 글 리스트 조회
     * CRUD -> CREATE, READ, UPDATE, DELETE
     */

    private final PostRepository postRepository;

    @Transactional
    public Post write(PostCreate postCreate){

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                        .build();


        return postRepository.save(post);
    }


    /**
     *  Controller ->  Service -> Repository
     *                          Web
     *                           Original
     */

    public PostResponse get(Long id) {
         Post post = postRepository.findById(id)
                 .orElseThrow(() -> new PostNotFound());

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

    }

    // 글이 너무 많은 경우 -> 비용이 많이 든다.
    // 서버가 뻗을 수 있음
    // DB -> 애플리케이션 서버로 전달하는 시간, 트래픽 비용 등 많이 발생할 수 있다.

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream().map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);

//        post.edit(
//                postEdit.getTitle() != null ? postEdit.getTitle() : post.getTitle(),
//                postEdit.getContent() != null ? postEdit.getContent() : post.getContent()
//        );
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        postRepository.delete(post);
    }
}

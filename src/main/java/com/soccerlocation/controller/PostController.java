package com.soccerlocation.controller;

import com.soccerlocation.domain.Post;
import com.soccerlocation.exception.InvalidRequest;
import com.soccerlocation.request.PostCreate;
import com.soccerlocation.request.PostEdit;
import com.soccerlocation.request.PostSearch;
import com.soccerlocation.response.PostResponse;
import com.soccerlocation.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 *  사이트 글도 작성하는 곳이 있으면 좋다.
 *  사람들이 방문했던 경기장들의 평을 남길 수도 있고 ..
 *  나중에 평점도 추가하면 좋지않을까 ?
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    /**
     * Valid 한게 컨트롤러까지 넘어오지 않고 그 전에 처리된다. 김영한 강의를 들었던 기억이 스믈스믈..
     * 근데 bidingResult 사용하면 컨트롤러까지 넘어옴
     */
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        /**
         * 값을 가져와서 검증하는건 좋은게 아니다.
         */
//        if(request.getTitle().contains("바보")){
//            throw new InvalidRequest();
//        }

        request.validate();

        postService.write(request);
    }

    /**
     * /posts/{postId}
     */

    // 응답 클래스는 분리하자.

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId){
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request)
    {
        postService.edit(postId,request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {

        postService.delete(postId);
    }


    /**
     * API 문서 생성
     *  클라이언트는 API 가 어떻게 되는지 모른다.
     *  Spring RestDocs 사용할 예정
     *              ->  운영코드에 영향이 없다.
     *    나중에 .. 코드 수정을 했는데 문서를 수정 X -> 신뢰성 하락
     *    TEST CASE 작성 -> 문서 자동 생성
     */


}

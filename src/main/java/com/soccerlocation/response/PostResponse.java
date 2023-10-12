package com.soccerlocation.response;

import com.soccerlocation.domain.Post;
import lombok.Builder;
import lombok.Getter;


/**
 *
 */

@Getter
public class PostResponse {

        private final Long id;
        private final String title;
        private final String content;

        // 생성자 오버로딩
        public PostResponse(Post post){
                this.id = post.getId();
                this.title = post.getTitle().substring(0, Math.min(post.getTitle().length(), 10));
                this.content = post.getContent();
        }

        @Builder
        public PostResponse(Long id, String title, String content) {
                this.id = id;
                this.title = title.substring(0, Math.min(title.length(), 10));
                this.content = content;
        }
}

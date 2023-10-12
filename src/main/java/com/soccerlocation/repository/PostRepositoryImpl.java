package com.soccerlocation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soccerlocation.domain.Post;
import com.soccerlocation.domain.QPost;
import com.soccerlocation.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.soccerlocation.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
      return   jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset((postSearch.getOffset()))
              .orderBy(post.id.desc())
                .fetch();
    }
}

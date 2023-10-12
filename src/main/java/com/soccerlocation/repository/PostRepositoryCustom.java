package com.soccerlocation.repository;

import com.soccerlocation.domain.Post;
import com.soccerlocation.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}

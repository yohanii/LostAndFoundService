package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findAllByTypeAndContent(PostType postType, String content);
}

package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.dto.post.PostSearchRequestDto;
import com.yohanii.lostandfound.component.crud.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findAllByPostSearchRequestDto(PostSearchRequestDto dto);
}

package com.yohanii.lostandfound.component.crud.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostsResponseDto {

    private int count;
    private List<PostResponseDto> postResponseList;


    public PostsResponseDto(List<PostResponseDto> postResponseDtos) {
        this.postResponseList = postResponseDtos;
        this.count = postResponseDtos.size();
    }
}

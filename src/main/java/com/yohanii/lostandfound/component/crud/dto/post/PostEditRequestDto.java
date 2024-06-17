package com.yohanii.lostandfound.component.crud.dto.post;

import com.yohanii.lostandfound.component.crud.entity.ItemCategory;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostEditRequestDto {

    private String title;
    private String content;
    private PostType type;

    private String itemPlace;
    private ItemCategory itemCategory;
    private List<MultipartFile> itemImages;
}

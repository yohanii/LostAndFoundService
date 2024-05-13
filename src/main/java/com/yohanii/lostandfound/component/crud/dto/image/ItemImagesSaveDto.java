package com.yohanii.lostandfound.component.crud.dto.image;

import com.yohanii.lostandfound.component.crud.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemImagesSaveDto {

    private Item item;
    private List<MultipartFile> images;

}

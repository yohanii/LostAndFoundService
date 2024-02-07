package com.yohanii.lostandfound.dto.image;

import com.yohanii.lostandfound.domain.item.Item;
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

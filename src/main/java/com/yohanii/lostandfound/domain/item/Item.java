package com.yohanii.lostandfound.domain.item;

import com.yohanii.lostandfound.domain.itemcategory.ItemCategory;
import com.yohanii.lostandfound.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Item {

    private Post post;
    private ItemCategory itemCategory;
    private String name;
    private String place;
}

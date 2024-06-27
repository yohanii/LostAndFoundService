package com.yohanii.lostandfound.component.crud.entity;

import com.yohanii.lostandfound.component.crud.dto.post.PostEditRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    private String place;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    @Builder
    public Item(Long id, Post post, String place, ItemCategory itemCategory) {
        this.id = id;
        this.post = post;
        this.place = place;
        this.itemCategory = itemCategory;
    }

    public void updateItem(PostEditRequestDto dto) {
        this.place = dto.getItemPlace();
        this.itemCategory = dto.getItemCategory();
    }
}

package com.yohanii.lostandfound.component.crud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yohanii.lostandfound.component.crud.dto.post.PostEditRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @Column(name = "item_name")
    private String name;
    private String place;

    private LocalDateTime occurredTime;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    @Builder
    public Item(Post post, String name, String place, LocalDateTime occurredTime, ItemCategory itemCategory) {
        this.post = post;
        this.name = name;
        this.place = place;
        this.occurredTime = occurredTime;
        this.itemCategory = itemCategory;
    }

    public void updateItem(PostEditRequestDto dto) {
        this.name = dto.getItemName();
        this.place = dto.getItemPlace();
        this.itemCategory = dto.getItemCategory();
    }
}

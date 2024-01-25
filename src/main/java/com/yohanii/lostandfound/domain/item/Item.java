package com.yohanii.lostandfound.domain.item;

import com.yohanii.lostandfound.domain.image.Image;
import com.yohanii.lostandfound.domain.post.Post;
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

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "item")
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
}

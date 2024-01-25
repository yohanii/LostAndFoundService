package com.yohanii.lostandfound.domain.image;

import com.yohanii.lostandfound.domain.item.Item;
import com.yohanii.lostandfound.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type")
    private ImageType type;

    private String uploadFileName;
    private String storeFileName;

    @Builder
    public Image(User user, Item item, ImageType type, String uploadFileName, String storeFileName) {
        this.user = user;
        this.item = item;
        this.type = type;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}

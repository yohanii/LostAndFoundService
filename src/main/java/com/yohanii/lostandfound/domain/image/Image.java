package com.yohanii.lostandfound.domain.image;

import com.yohanii.lostandfound.domain.item.Item;
import com.yohanii.lostandfound.domain.member.Member;
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
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type")
    private ImageType type;

    private String uploadFileName;
    private String storeFileName;

    @Builder
    public Image(Member member, Item item, ImageType type, String uploadFileName, String storeFileName) {
        this.member = member;
        this.item = item;
        this.type = type;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

    public void changeFileName(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}

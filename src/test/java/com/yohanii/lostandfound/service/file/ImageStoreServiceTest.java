package com.yohanii.lostandfound.service.file;

import com.yohanii.lostandfound.domain.image.Image;
import com.yohanii.lostandfound.domain.image.ImageRepository;
import com.yohanii.lostandfound.domain.image.ImageType;
import com.yohanii.lostandfound.domain.item.Item;
import com.yohanii.lostandfound.domain.item.ItemRepository;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import com.yohanii.lostandfound.dto.image.ItemImagesSaveDto;
import com.yohanii.lostandfound.dto.image.ProfileImageSaveDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ImageStoreServiceTest {

    @Autowired
    ImageStoreService imageStoreService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    void saveImage_profileImage_isEmpty() {
        ProfileImageSaveDto dto = new ProfileImageSaveDto(null, null);

        assertThatThrownBy(() -> imageStoreService.saveImage(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ProfileImage is Empty");
    }

    @Test
    void saveImage() throws IOException {
        Member member = Member.builder().build();
        memberRepository.save(member);
        MockMultipartFile file = new MockMultipartFile("file", "test.jpeg", "", InputStream.nullInputStream());
        ProfileImageSaveDto dto = new ProfileImageSaveDto(member, file);

        Image savedImage = imageStoreService.saveImage(dto);

        assertThat(savedImage.getId()).isNotNull();
        assertThat(savedImage.getMember()).isEqualTo(member);
        assertThat(savedImage.getItem()).isNull();
        assertThat(savedImage.getType()).isEqualTo(ImageType.MEMBER);
        assertThat(savedImage.getUploadFileName()).isEqualTo(file.getOriginalFilename());
        assertThat(savedImage.getStoreFileName()).isNotBlank();
    }

    @Test
    void saveImages() throws IOException {
        Item item = Item.builder().build();
        itemRepository.save(item);
        MockMultipartFile file1 = new MockMultipartFile("file1", "test1.jpeg", "", InputStream.nullInputStream());
        MockMultipartFile file2 = new MockMultipartFile("file2", "test2.jpeg", "", InputStream.nullInputStream());
        List<MultipartFile> files = List.of(file1, file2);
        ItemImagesSaveDto dto = new ItemImagesSaveDto(item, files);

        List<Image> savedImages = imageStoreService.saveImages(dto);

        assertThat(savedImages.size()).isEqualTo(2);
        assertThat(savedImages.get(0).getId()).isNotNull();
        assertThat(savedImages.get(0).getItem()).isEqualTo(item);
        assertThat(savedImages.get(0).getMember()).isNull();
        assertThat(savedImages.get(0).getType()).isEqualTo(ImageType.ITEM);
        assertThat(savedImages.get(0).getUploadFileName()).isEqualTo(file1.getOriginalFilename());
        assertThat(savedImages.get(0).getStoreFileName()).isNotBlank();
        assertThat(savedImages.get(1).getId()).isNotNull();
        assertThat(savedImages.get(1).getItem()).isEqualTo(item);
        assertThat(savedImages.get(1).getMember()).isNull();
        assertThat(savedImages.get(1).getType()).isEqualTo(ImageType.ITEM);
        assertThat(savedImages.get(1).getUploadFileName()).isEqualTo(file2.getOriginalFilename());
        assertThat(savedImages.get(1).getStoreFileName()).isNotBlank();
    }
}
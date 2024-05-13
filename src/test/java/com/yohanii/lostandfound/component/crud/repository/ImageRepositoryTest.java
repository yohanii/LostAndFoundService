package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.entity.Image;
import com.yohanii.lostandfound.component.crud.repository.ImageRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ImageRepositoryTest {

    @Autowired
    ImageRepository imageRepository;

    @Test
    void save() {
        Image image = Image.builder()
                .build();
        imageRepository.save(image);

        Image findImage = imageRepository.find(image.getId());

        assertThat(findImage).isEqualTo(image);
    }

//    @Test
//    void saveDefaultImage() {
//        boolean status = imageRepository.saveDefaultImage();
//
//        assertThat(status).isFalse();
//    }

    @Test
    void find() {
        Image image = Image.builder()
                .build();
        Long savedId = imageRepository.save(image);

        Image findImage = imageRepository.find(savedId);

        assertThat(findImage).isEqualTo(image);
    }

    @Test
    void findAll() {
        Image image1 = Image.builder().build();
        Image image2 = Image.builder().build();

        imageRepository.save(image1);
        imageRepository.save(image2);

        List<Image> findImages = imageRepository.findAll();

        assertThat(findImages).contains(image1, image2);
    }

    @Test
    void findByStoreFileName() {
        Image image = Image.builder()
                .storeFileName("test123")
                .build();
        imageRepository.save(image);

        Optional<Image> findImage = imageRepository.findByStoreFileName(image.getStoreFileName());

        assertThat(findImage).isPresent();
        assertThat(findImage.get()).isEqualTo(image);
    }

    @Test
    void findByStoreFileName_해당하는_storeFileName_없을경우() {
        Optional<Image> findImage = imageRepository.findByStoreFileName("testtesttesttesttest");

        assertThat(findImage).isNotPresent();
    }

    @Test
    void deleteAll() {
        Image image1 = Image.builder().build();
        Image image2 = Image.builder().build();
        imageRepository.save(image1);
        imageRepository.save(image2);

        int affectedRows = imageRepository.deleteAll(List.of(image1, image2));

        List<Image> images = imageRepository.findAll();
        assertThat(images).doesNotContain(image1, image2);
        assertThat(affectedRows).isEqualTo(2);
    }

    @Test
    void deleteAll_저장안된_image가_섞여있는경우() {
        Image image1 = Image.builder().build();
        Image image2 = Image.builder().build();
        imageRepository.save(image1);

        int affectedRows = imageRepository.deleteAll(List.of(image1, image2));

        List<Image> images = imageRepository.findAll();
        assertThat(images).doesNotContain(image1, image2);
        assertThat(affectedRows).isEqualTo(1);
    }
}
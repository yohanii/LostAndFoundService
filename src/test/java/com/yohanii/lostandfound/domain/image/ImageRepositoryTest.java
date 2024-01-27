package com.yohanii.lostandfound.domain.image;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
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

    @Test
    void saveDefaultImage() {
        boolean status = imageRepository.saveDefaultImage();

        assertThat(status).isFalse();
    }

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
    void    findByStoreFileName_해당하는_storeFileName_없을경우() {
        Optional<Image> findImage = imageRepository.findByStoreFileName("testtesttesttesttest");

        assertThat(findImage).isNotPresent();
    }
}
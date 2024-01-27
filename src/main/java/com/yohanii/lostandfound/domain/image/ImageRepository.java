package com.yohanii.lostandfound.domain.image;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final EntityManager em;

    public Long save(Image image) {
        em.persist(image);
        return image.getId();
    }

    public boolean saveDefaultImage() {

        if (findByStoreFileName("default").isPresent()) {
            return false;
        }
        save(new Image(null, null, ImageType.GENERAL, "default", "default"));
        return true;
    }

    public Image find(Long id) { return em.find(Image.class, id); }

    public List<Image> findAll() {
        return em.createQuery("select i from Image i", Image.class)
                .getResultList();
    }

    public Optional<Image> findByStoreFileName(String storeFileName) {
        return findAll().stream()
                .filter(image -> image.getStoreFileName().equals(storeFileName))
                .findFirst();
    }

}

package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.entity.Image;
import com.yohanii.lostandfound.component.crud.entity.ImageType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
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

    public int deleteAll(List<Image> images) {
        List<Long> ids = images.stream()
                .map(Image::getId)
                .filter(Objects::nonNull)
                .toList();

        return em.createQuery("delete from Image i where i.id in :ids")
                .setParameter("ids", ids)
                .executeUpdate();
    }
}

package com.yohanii.lostandfound.domain.notify;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository{

    private final EntityManager em;

    public Long save(Notification notification) {
        em.persist(notification);
        return notification.getId();
    }

    public Notification find(Long id) {
        return em.find(Notification.class, id);
    }

    public List<Notification> findAll() {
        return em.createQuery("select n from Notification n", Notification.class)
                .getResultList();
    }
}

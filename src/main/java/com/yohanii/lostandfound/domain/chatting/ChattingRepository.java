package com.yohanii.lostandfound.domain.chatting;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChattingRepository {

    private final EntityManager em;

    public Long save(Chatting chatting) {
        em.persist(chatting);
        return chatting.getId();
    }

    public Chatting find(Long id) {
        return em.find(Chatting.class, id);
    }

    public List<Chatting> findAll() {
        return em.createQuery("select c from Chatting c", Chatting.class)
                .getResultList();
    }
}

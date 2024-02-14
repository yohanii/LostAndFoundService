package com.yohanii.lostandfound.domain.chatting;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    private final EntityManager em;

    public Long save(Room room) {
        em.persist(room);
        return room.getId();
    }

    public Room find(Long id) {
        return em.find(Room.class, id);
    }

    public List<Room> findAll() {
        return em.createQuery("select r from Room r", Room.class)
                .getResultList();
    }

    public Optional<Room> findByStoreRoomName(String name) {
        return findAll().stream()
                .filter(room -> room.getStoreRoomName().equals(name))
                .findFirst();
    }
}

package com.yohanii.lostandfound.domain.chatting;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Room> findByMemberId(Long id) {
        return findAll().stream()
                .filter(room -> room.getMember().getId().equals(id))
                .collect(Collectors.toList());
    }

    public List<Room> findByPartnerId(Long id) {
        return findAll().stream()
                .filter(room -> room.getPartnerId().equals(id))
                .collect(Collectors.toList());
    }

    public Optional<Room> findByIds(Long memberId, Long partnerId) {
        return findAll().stream()
                .filter(room -> (room.getMember().getId().equals(memberId) && room.getPartnerId().equals(partnerId))
                || (room.getMember().getId().equals(partnerId) && room.getPartnerId().equals(memberId)))
                .findFirst();
    }
}

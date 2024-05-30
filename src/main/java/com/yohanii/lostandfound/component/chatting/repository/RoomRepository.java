package com.yohanii.lostandfound.component.chatting.repository;

import com.yohanii.lostandfound.component.chatting.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Modifying
    @Query("delete from Room r where r.id in :roomIds")
    void deleteAll(@Param("roomIds") List<Long> roomIds);

    Optional<Room> findByStoreRoomName(String storeRoomName);

    List<Room> findAllByMemberId(Long memberId);

    List<Room> findAllByPartnerId(Long partnerId);

    Optional<Room> findByMemberIdAndPartnerId(Long memberId, Long partnerId);
}

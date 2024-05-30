package com.yohanii.lostandfound.component.chatting.repository;


import com.yohanii.lostandfound.component.chatting.entity.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRepository extends JpaRepository<Chatting, Long> {
}

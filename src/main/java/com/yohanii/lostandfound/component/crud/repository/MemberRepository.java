package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickName(String nickName);

    Optional<Member> findByLoginId(String loginId);

    @Modifying
    @Query("delete from Member m where m.id in :memberIds")
    void deleteAll(@Param("memberIds") List<Long> memberIds);

}

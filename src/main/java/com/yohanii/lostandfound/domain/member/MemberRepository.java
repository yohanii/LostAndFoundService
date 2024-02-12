package com.yohanii.lostandfound.domain.member;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select u from Member u", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select u from Member u where u.name =:name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(member -> member.getLoginId().equals(loginId))
                .findFirst();
    }

    public Optional<Member> findByNickName(String nickName) {
        return findAll().stream()
                .filter(member -> member.getNickName().equals(nickName))
                .findFirst();
    }
}

package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("입력된 memberIds에 해당하는 member들을 모두 제거한다.")
    void deleteAll() {

        //given
        Member member1 = Member.builder().build();
        Member member2 = Member.builder().build();
        Member member3 = Member.builder().build();

        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);

        //when
        memberRepository.deleteAll(List.of(savedMember1.getId(), savedMember2.getId(), savedMember3.getId()));

        //then
        List<Member> members = memberRepository.findAll();
        assertThat(members).doesNotContain(savedMember1, savedMember2, savedMember3);
    }

    @Test
    @DisplayName("입력된 memberId에 해당하는 member가 없을 경우, 존재하는 member만 제거한다.")
    void deleteAll_non_exist_memberId() {

        //given
        Member member1 = Member.builder().build();
        Member member2 = Member.builder().build();
        Member member3 = Member.builder().build();

        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);

        memberRepository.deleteById(savedMember3.getId());

        //when
        memberRepository.deleteAll(List.of(savedMember1.getId(), savedMember2.getId(), savedMember3.getId()));

        //then
        List<Member> members = memberRepository.findAll();
        assertThat(members).doesNotContain(savedMember1, savedMember2, savedMember3);
    }
}
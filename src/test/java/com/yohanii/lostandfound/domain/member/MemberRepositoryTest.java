package com.yohanii.lostandfound.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
        Member testMember = Member.builder()
                .loginId("abc")
                .password("abcd")
                .name("memberTest")
                .build();
        Long savedId = memberRepository.save(testMember);

        Member savedMember = memberRepository.find(testMember.getId());

        assertThat(savedMember.getId()).isEqualTo(testMember.getId());
        assertThat(savedMember.getLoginId()).isEqualTo(testMember.getLoginId());
        assertThat(savedMember.getPassword()).isEqualTo(testMember.getPassword());
        assertThat(savedMember.getName()).isEqualTo(testMember.getName());
    }

    @Test
    void find() {
        Member testMember = Member.builder()
                .loginId("")
                .password("")
                .name("memberTest")
                .build();
        Long savedId = memberRepository.save(testMember);

        Member savedMember = memberRepository.find(savedId);

        assertThat(savedMember).isEqualTo(testMember);
    }

    @Test
    void findAll() {
        Member member1 = Member.builder()
                .loginId("")
                .password("")
                .name("memberA")
                .build();
        Member member2 = Member.builder()
                .loginId("")
                .password("")
                .name("memberB")
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findAll();

        assertThat(findMembers).contains(member1, member2);
        assertThat(findMembers.size()).isEqualTo(2);
    }

    @Test
    void findByName() {
        Member testMember = Member.builder()
                .loginId("")
                .password("")
                .name("memberTest")
                .build();
        memberRepository.save(testMember);

        List<Member> findMember = memberRepository.findByName(testMember.getName());

        assertThat(findMember.size()).isEqualTo(1);
        assertThat(findMember).contains(testMember);
    }

    @Test
    void findByLoginId() {
        Member testMember = Member.builder()
                .loginId("test123456789")
                .password("")
                .name("memberTest")
                .build();
        memberRepository.save(testMember);

        Member findMember = memberRepository.findByLoginId(testMember.getLoginId()).get();

        assertThat(findMember).isEqualTo(testMember);
    }

    @Test
    void findByNickName() {
        Member testMember = Member.builder()
                .nickName("testNickName")
                .build();

        memberRepository.save(testMember);

        Member findMember = memberRepository.findByNickName(testMember.getNickName()).get();

        assertThat(findMember).isEqualTo(testMember);
    }
}
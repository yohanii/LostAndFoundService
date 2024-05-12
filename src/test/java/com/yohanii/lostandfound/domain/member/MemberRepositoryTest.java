package com.yohanii.lostandfound.domain.member;

import com.yohanii.lostandfound.InitComponent;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @MockBean
    InitComponent initComponent;

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
    void save_CreatedTime_UpdatedTime_같음() {
        Member member = Member.builder().build();

        memberRepository.save(member);

        Member findMember = memberRepository.find(member.getId());
        assertThat(findMember.getCreatedTime()).isEqualTo(findMember.getUpdatedTime());
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

    @Test
    void deleteAll() {
        Member member1 = Member.builder().build();
        Member member2 = Member.builder().build();
        Member member3 = Member.builder().build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        memberRepository.deleteAll(List.of(member1.getId(), member2.getId(), member3.getId()));

        assertThat(memberRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void getMemberCount() {
        Member member1 = Member.builder().build();
        Member member2 = Member.builder().build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        long memberCount = memberRepository.getMemberCount();

        assertThat(memberCount).isEqualTo(2);
    }

    @Test
    void getMemberCount_맴버_0명() {
        long memberCount = memberRepository.getMemberCount();

        assertThat(memberCount).isEqualTo(0);
    }

    @Test
    @DisplayName("admin member가 db에 정상적으로 저장되고 true를 반환해야한다.")
    void saveAdmin_정상() {
        boolean result = memberRepository.saveAdmin();

        Optional<Member> findMember = memberRepository.findByLoginId("admin");
        assertThat(result).isTrue();
        assertThat(findMember).isPresent();
        assertThat(findMember.get().getLoginId()).isEqualTo("admin");
        assertThat(findMember.get().getPassword()).isEqualTo("admin");
        assertThat(findMember.get().getName()).isEqualTo("admin");
        assertThat(findMember.get().getNickName()).isEqualTo("admin");
        assertThat(findMember.get().getAuth()).isEqualTo(MemberAuth.ADMIN);
    }

    @Test
    @DisplayName("loginId가 admin인 member가 이미 있다면, 저장되지 않고 false를 반환해야한다.")
    void saveAdmin_admin_이미_존재() {
        memberRepository.saveAdmin();

        boolean result = memberRepository.saveAdmin();

        assertThat(result).isFalse();
    }
}
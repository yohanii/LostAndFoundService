package com.yohanii.lostandfound.service.admin;

import com.yohanii.lostandfound.InitComponent;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberAuth;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AdminServiceTest {

    @MockBean
    InitComponent initComponent;
    @Autowired
    AdminService adminService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void findAllMembers() {
        Member member1 = Member.builder().build();
        Member member2 = Member.builder().build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = adminService.findAllMembers();

        assertThat(members.size()).isEqualTo(2);
        assertThat(members).contains(member1, member2);
    }

    @Test
    void deleteMembers() {
        Member member1 = Member.builder().build();
        Member member2 = Member.builder().build();
        Member member3 = Member.builder().build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        adminService.deleteMembers(List.of(member1.getId(), member2.getId()));
        List<Member> members = adminService.findAllMembers();

        assertThat(members.size()).isEqualTo(1);
        assertThat(members).doesNotContain(member1, member2);
    }

    @Test
    @DisplayName("memberIds에 해당하는 member들의 auth가 변경되어야 한다.")
    void updateMembersAuth() {
        Member member1 = Member.builder().auth(MemberAuth.ADMIN).build();
        Member member2 = Member.builder().auth(MemberAuth.MEMBER).build();
        Member member3 = Member.builder().auth(MemberAuth.MEMBER).build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        adminService.updateMembersAuth(List.of(member1.getId(), member2.getId(), member3.getId()));

        assertThat(member1.getAuth()).isEqualTo(MemberAuth.MEMBER);
        assertThat(member2.getAuth()).isEqualTo(MemberAuth.ADMIN);
        assertThat(member3.getAuth()).isEqualTo(MemberAuth.ADMIN);
    }
}
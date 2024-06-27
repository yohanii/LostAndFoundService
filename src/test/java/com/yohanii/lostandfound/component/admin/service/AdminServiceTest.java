package com.yohanii.lostandfound.component.admin.service;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    AdminService adminService;
    @Mock
    MemberRepository memberRepository;

    Member member1;
    Member member2;

    @BeforeEach
    void setUp() {
        member1 = Member.builder()
                .id(1L)
                .auth(MemberAuth.MEMBER)
                .build();

        member2 = Member.builder()
                .id(2L)
                .auth(MemberAuth.ADMIN)
                .build();
    }

    @Test
    void findAllMembers() {

        //given
        given(memberRepository.findAll()).willReturn(List.of(member1, member2));

        //when
        List<Member> members = adminService.findAllMembers();

        //then
        assertThat(members.size()).isEqualTo(2);
        assertThat(members).contains(member1, member2);
    }

    @Test
    void deleteMembers() {

        //given
        //when
        adminService.deleteMembers(List.of(member1.getId(), member2.getId()));

        //then
        then(memberRepository).should().deleteAll(List.of(member1.getId(), member2.getId()));
    }

    @Test
    @DisplayName("memberIds에 해당하는 member들의 auth가 변경되어야 한다.")
    void updateMembersAuth() {

        //given
        given(memberRepository.findAll()).willReturn(List.of(member1, member2));

        //when
        adminService.updateMembersAuth(List.of(member1.getId(), member2.getId()));

        //then
        assertThat(member1.getAuth()).isEqualTo(MemberAuth.ADMIN);
        assertThat(member2.getAuth()).isEqualTo(MemberAuth.MEMBER);
    }
}
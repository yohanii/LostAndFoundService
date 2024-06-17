package com.yohanii.lostandfound.component.init;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class InitServiceTest {

    @Autowired
    InitService initService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void before() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("admin member가 db에 정상적으로 저장되고 true를 반환해야한다.")
    void saveAdmin_정상() {
        boolean result = initService.saveAdmin();

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
        initService.saveAdmin();

        boolean result = initService.saveAdmin();

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("1000명 저장되어야한다.")
    void fillMembers_정상() {
        int result = initService.fillMembers();

        List<Member> findMembers = memberRepository.findAll();
        Member member = findMembers.get(0);

        assertThat(result).isEqualTo(1000);
        assertThat(findMembers.size()).isEqualTo(1000L);
        assertThat(member.getLoginId()).startsWith("testLoginId");
        assertThat(member.getPassword()).startsWith("testPassword");
        assertThat(member.getName()).startsWith("testName");
        assertThat(member.getNickName()).startsWith("testNickName");
        assertThat(member.getAuth()).isEqualTo(MemberAuth.MEMBER);

    }

    @Test
    @DisplayName("1000명 이상이 이미 존재하면 0 반환하고 아무것도 저장하지 않는다.")
    void fillMembers_1000명_이미_존재시() {
        initService.fillMembers();

        int result = initService.fillMembers();

        assertThat(result).isEqualTo(0);
    }
}
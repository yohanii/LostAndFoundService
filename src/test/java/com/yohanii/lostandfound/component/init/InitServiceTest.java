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
}
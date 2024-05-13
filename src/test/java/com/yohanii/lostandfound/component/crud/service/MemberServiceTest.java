package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.crud.dto.member.MemberSaveRequestDto;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("profileImage 있는 정상 입력에 대해 저장된다.")
    void saveMember_with_profileImage() {
        MemberSaveRequestDto dto = new MemberSaveRequestDto("testName", "testLoginId", "testPassword", "testNickName");
        MockMultipartFile file = new MockMultipartFile("testFile", new byte[10]);

        Long savedMemberId = memberService.saveMember(dto, file);

        Member findMember = memberRepository.findById(savedMemberId).orElse(Member.builder().build());
        assertThat(findMember.getName()).isEqualTo(dto.getName());
        assertThat(findMember.getLoginId()).isEqualTo(dto.getLoginId());
        assertThat(findMember.getPassword()).isEqualTo(dto.getPassword());
        assertThat(findMember.getNickName()).isEqualTo(dto.getNickName());
//        assertThat(findMember.getProfileImage().getUploadFileName()).isEqualTo(file.getOriginalFilename());
    }

    @Test
    @DisplayName("profileImage 없는 정상 입력도 저장된다.")
    void saveMember_no_profileImage() {
        MemberSaveRequestDto dto = new MemberSaveRequestDto("testName", "testLoginId", "testPassword", "testNickName");

        Long savedMemberId = memberService.saveMember(dto, null);

        Member findMember = memberRepository.findById(savedMemberId).orElse(Member.builder().build());
        assertThat(findMember.getName()).isEqualTo(dto.getName());
        assertThat(findMember.getLoginId()).isEqualTo(dto.getLoginId());
        assertThat(findMember.getPassword()).isEqualTo(dto.getPassword());
        assertThat(findMember.getNickName()).isEqualTo(dto.getNickName());
    }

    @Test
    @DisplayName("닉네임 중복은 안된다.")
    void saveMember_duplicated_nickName() {
        MemberSaveRequestDto dto1 = new MemberSaveRequestDto("testName1", "testLoginId1", "testPassword1", "testNickName1");
        MemberSaveRequestDto dto2 = new MemberSaveRequestDto("testName2", "testLoginId2", "testPassword2", "testNickName1");

        memberService.saveMember(dto1, null);

        assertThatThrownBy(() -> memberService.saveMember(dto2, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("dto의 name, loginId, password, nickName은 빈 값일 수 없다.")
    void saveMember_dto_notBlank() {
        MemberSaveRequestDto dto1 = new MemberSaveRequestDto("", "testLoginId", "testPassword", "testNickName");
        MemberSaveRequestDto dto2 = new MemberSaveRequestDto("testName", "", "testPassword", "testNickName");
        MemberSaveRequestDto dto3 = new MemberSaveRequestDto("testName", "testLoginId", "", "testNickName");
        MemberSaveRequestDto dto4 = new MemberSaveRequestDto("testName", "testLoginId", "testPassword", "");

        assertThatThrownBy(() -> memberService.saveMember(dto1, null))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> memberService.saveMember(dto2, null))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> memberService.saveMember(dto3, null))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> memberService.saveMember(dto4, null))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("dto의 name, loginId, password, nickName은 null일 수 없다.")
    void saveMember_dto_notNull() {
        MemberSaveRequestDto dto1 = new MemberSaveRequestDto(null, "testLoginId", "testPassword", "testNickName");
        MemberSaveRequestDto dto2 = new MemberSaveRequestDto("testName", null, "testPassword", "testNickName");
        MemberSaveRequestDto dto3 = new MemberSaveRequestDto("testName", "testLoginId", null, "testNickName");
        MemberSaveRequestDto dto4 = new MemberSaveRequestDto("testName", "testLoginId", "testPassword", null);

        assertThatThrownBy(() -> memberService.saveMember(dto1, null))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> memberService.saveMember(dto2, null))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> memberService.saveMember(dto3, null))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> memberService.saveMember(dto4, null))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("admin이 존재하지 않을 때, saveAdmin 호출 시 새로운 admin을 생성합니다. admin의 권한은 MemberAuth.ADMIN입니다.")
    void saveAdmin_create_new_admin() {
        Optional<Member> admin = memberRepository.findByNickName("admin");
        if (admin.isPresent()) {
            memberRepository.deleteById(admin.get().getId());
        }

        Long savedAdminId = memberService.saveAdmin();

        Optional<Member> findAdmin = memberRepository.findById(savedAdminId);
        assertThat(findAdmin.isPresent()).isTrue();
        assertThat(findAdmin.get().getName()).isEqualTo("admin");
        assertThat(findAdmin.get().getLoginId()).isEqualTo("admin");
        assertThat(findAdmin.get().getPassword()).isEqualTo("admin");
        assertThat(findAdmin.get().getNickName()).isEqualTo("admin");
        assertThat(findAdmin.get().getAuth()).isEqualTo(MemberAuth.ADMIN);
    }

    @Test
    @DisplayName("admin이 존재할 경우, 해당 member의 id를 반환합니다.")
    void saveAdmin_admin_exist() {
        Optional<Member> admin = memberRepository.findByNickName("admin");
        if (admin.isPresent()) {
            memberRepository.deleteById(admin.get().getId());
        }
        Long savedAdminId = memberService.saveAdmin();

        Long result = memberService.saveAdmin();

        assertThat(result).isEqualTo(savedAdminId);
    }
}
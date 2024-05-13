package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.crud.dto.image.ProfileImageSaveDto;
import com.yohanii.lostandfound.component.crud.dto.member.MemberSaveRequestDto;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ImageStoreService imageStoreService;

    @Transactional
    public Long saveMember(@Valid MemberSaveRequestDto dto, MultipartFile profileImage) {

        if (memberRepository.findByNickName(dto.getNickName()).isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }

        Long savedMemberId = memberRepository.save(dto.toEntity()).getId();
        Member savedMember = memberRepository.findById(savedMemberId)
                .orElseThrow(() -> new IllegalStateException("해당 유저 정보가 저장되지 않았습니다."));

        if (profileImage != null) {
            imageStoreService.saveImage(new ProfileImageSaveDto(savedMember, profileImage));
        }
        return savedMemberId;
    }

    @Transactional
    public Long saveAdmin() {

        Optional<Member> admin = memberRepository.findByNickName("admin");
        if (admin.isPresent()) {
            log.info("admin이 이미 존재합니다.");
            return admin.get().getId();
        }

        log.info("admin을 새로 생성합니다.");
        MemberSaveRequestDto dto = new MemberSaveRequestDto("admin", "admin", "admin", "admin");

        Long savedAdminId = saveMember(dto, null);
        Member findAdmin = memberRepository.findById(savedAdminId)
                .orElseThrow(() -> new IllegalStateException("admin이 저장되지 않았습니다."));
        findAdmin.changeAuth();

        return savedAdminId;
    }
}

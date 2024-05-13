package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.crud.dto.image.ProfileImageSaveDto;
import com.yohanii.lostandfound.component.crud.dto.member.MemberSaveRequestDto;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

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

        Long savedMemberId = memberRepository.save(dto.toEntity());
        Member savedMember = memberRepository.find(savedMemberId);

        if (profileImage != null) {
            imageStoreService.saveImage(new ProfileImageSaveDto(savedMember, profileImage));
        }
        return savedMemberId;
    }
}

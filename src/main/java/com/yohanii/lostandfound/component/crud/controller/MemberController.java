package com.yohanii.lostandfound.component.crud.controller;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.dto.image.ProfileImageSaveDto;
import com.yohanii.lostandfound.component.crud.dto.member.MemberSaveRequestDto;
import com.yohanii.lostandfound.component.crud.service.ImageStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final ImageStoreService imageStoreService;

    @GetMapping("/add-form")
    public String addForm(@ModelAttribute MemberSaveRequestDto dto) {
        return "members/addMemberForm";
    }

    @PostMapping
    @Transactional
    public String save(@Validated @ModelAttribute MemberSaveRequestDto dto, BindingResult bindingResult) {

        Optional<Member> findMember = memberRepository.findByNickName(dto.getNickName());
        if (findMember.isPresent()) {
            bindingResult.reject("errorCode입니다", "닉네임 중복입니다.");
        }

        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        Long savedMemberId = memberRepository.save(dto.toEntity());
        Member savedMember = memberRepository.find(savedMemberId);

        if (!dto.getProfileImage().isEmpty()) {
            imageStoreService.saveImage(new ProfileImageSaveDto(savedMember, dto.getProfileImage()));
        }
        log.info("Save Success!!!!!!!!!");
        return "redirect:/login";
    }
}

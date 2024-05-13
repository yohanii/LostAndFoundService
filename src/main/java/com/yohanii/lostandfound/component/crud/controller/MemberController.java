package com.yohanii.lostandfound.component.crud.controller;

import com.yohanii.lostandfound.component.crud.dto.member.MemberSaveRequestDto;
import com.yohanii.lostandfound.component.crud.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Long> save(@RequestPart MemberSaveRequestDto dto, @RequestPart(required = false) MultipartFile file) {

        Long savedMemberId = memberService.saveMember(dto, file);

        return ResponseEntity.ok(savedMemberId);
    }
}

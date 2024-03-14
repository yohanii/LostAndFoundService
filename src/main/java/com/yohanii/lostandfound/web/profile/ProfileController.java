package com.yohanii.lostandfound.web.profile;

import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import com.yohanii.lostandfound.dto.image.ProfileImageSaveDto;
import com.yohanii.lostandfound.dto.profile.ProfileEditRequestDto;
import com.yohanii.lostandfound.service.file.ImageStoreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final MemberRepository memberRepository;
    private final ImageStoreService imageStoreService;

    @GetMapping("/{nickName}")
    public String profile(@PathVariable String nickName, @RequestParam(defaultValue = "/") String redirectURL, Model model) {
        log.info("ProfileController.profile");

        Member findMember = memberRepository.findByNickName(nickName).orElseThrow(() -> new IllegalStateException("해당 nickName으로 찾을 수 없습니다."));
        log.info("findMember.name() = " + findMember.getName());
        log.info("findMember.nickName = " + findMember.getNickName());

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember != null && loginMember.getId().equals(findMember.getId())) {
            model.addAttribute("isLoginMember", true);
        }

        model.addAttribute("member", findMember);
        model.addAttribute("redirectURL", redirectURL);

        return "profile/profile";
    }

    @GetMapping("/edit-form/{nickName}")
    public String profileEditForm(@PathVariable String nickName, @ModelAttribute ProfileEditRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL, Model model) {
        log.info("ProfileController.profileEditForm");

        Member findMember = memberRepository.findByNickName(nickName).orElseThrow(() -> new IllegalStateException("해당 nickName으로 찾을 수 없습니다."));
        dto.setName(findMember.getName());
        dto.setNickName(findMember.getNickName());

        model.addAttribute("redirectURL", redirectURL);

        return "profile/editProfileForm";
    }

    @PatchMapping("/{nickName}")
    @Transactional
    public String profileEdit(@PathVariable String nickName, @ModelAttribute ProfileEditRequestDto dto, BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL, Model model) {
        log.info("ProfileController.profileEdit");

        Member findMember = memberRepository.findByNickName(nickName).orElseThrow(() -> new IllegalStateException("해당 nickName으로 찾을 수 없습니다."));

        Optional<Member> memberOfDto = memberRepository.findByNickName(dto.getNickName());
        if (memberOfDto.isPresent() && !memberOfDto.get().getId().equals(findMember.getId())) {
            bindingResult.reject("errorCode입니다", "닉네임 중복입니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute("redirectURL", redirectURL);
            dto.setNickName(nickName);
            return "profile/editProfileForm";
        }

        if (!dto.getProfileImage().isEmpty()) {
            imageStoreService.saveImage(new ProfileImageSaveDto(findMember, dto.getProfileImage()));
        }
        findMember.updateMember(dto);

        return "redirect:/profiles/" + dto.getNickName() + "?redirectURL=" + redirectURL;
    }
}

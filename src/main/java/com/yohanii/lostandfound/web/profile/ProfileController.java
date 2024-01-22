package com.yohanii.lostandfound.web.profile;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.domain.user.UserRepository;
import com.yohanii.lostandfound.dto.profile.ProfileEditRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final UserRepository userRepository;

    @GetMapping("/{nickName}")
    public String profile(@PathVariable String nickName, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        User findUser = userRepository.findByNickName(nickName).orElseThrow(() -> new IllegalStateException("해당 nickName으로 찾을 수 없습니다."));
        log.info("findUser.name() = " + findUser.getName());
        log.info("findUser.nickName = " + findUser.getNickName());

        User loginUser = (User) model.getAttribute("user");
        if (loginUser != null && loginUser.getId().equals(findUser.getId())) {
            log.info("ProfileController.profile isLoginUser = true");
            model.addAttribute("isLoginUser", true);
        }
        model.addAttribute("user", findUser);
        model.addAttribute("redirectURL", redirectURL);

        return "profile/profile";
    }

    @GetMapping("/edit-form/{nickName}")
    public String profileEditForm(@PathVariable String nickName, @ModelAttribute ProfileEditRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        User findUser = userRepository.findByNickName(nickName).orElseThrow(() -> new IllegalStateException("해당 nickName으로 찾을 수 없습니다."));
        dto.setName(findUser.getName());
        dto.setNickName(findUser.getNickName());

        model.addAttribute("redirectURL", redirectURL);

        return "profile/editProfileForm";
    }

    @PatchMapping("/{nickName}")
    @Transactional
    public String profileEdit(@PathVariable String nickName, @ModelAttribute ProfileEditRequestDto dto, BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        User findUser = userRepository.findByNickName(nickName).orElseThrow(() -> new IllegalStateException("해당 nickName으로 찾을 수 없습니다."));

        if (userRepository.findByNickName(dto.getNickName()).isPresent()) {
            bindingResult.reject("errorCode입니다", "닉네임 중복입니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute("redirectURL", redirectURL);
            dto.setNickName(nickName);
            return "profile/editProfileForm";
        }

        findUser.updateUser(dto);

        return "redirect:/profiles/" + dto.getNickName() + "?redirectURL=" + redirectURL;
    }
}

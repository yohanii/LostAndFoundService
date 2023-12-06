package com.yohanii.lostandfound.web.user;

import com.yohanii.lostandfound.domain.user.UserRepository;
import com.yohanii.lostandfound.dto.user.UserSaveRequestDto;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute UserSaveRequestDto dto) {
        return "users/addUserForm";
    }

    @PostMapping("/add")
    @Transactional
    public String save(@Validated @ModelAttribute UserSaveRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/addUserForm";
        }

        userRepository.save(dto.toEntity());
        return "redirect:/";
    }
}

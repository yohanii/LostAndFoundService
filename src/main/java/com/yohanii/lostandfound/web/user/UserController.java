package com.yohanii.lostandfound.web.user;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.domain.user.UserMemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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

    private final UserMemoryRepository userRepository = new UserMemoryRepository();

    @GetMapping("/add")
    public String addForm(@ModelAttribute User user) {
        return "users/addUserForm";
    }

    @PostMapping("/add")
    public String save(@Validated @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/addUserForm";
        }

        userRepository.save(user);
        return "redirect:/";
    }
}

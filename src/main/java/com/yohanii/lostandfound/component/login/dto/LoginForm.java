package com.yohanii.lostandfound.component.login.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class LoginForm {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}

package com.example.finpick.api.controller.user.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserRequest {

    private String username;
    private String password;

    @Data
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }
}
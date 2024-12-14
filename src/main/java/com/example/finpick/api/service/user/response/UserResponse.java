package com.example.finpick.api.service.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String token;
}
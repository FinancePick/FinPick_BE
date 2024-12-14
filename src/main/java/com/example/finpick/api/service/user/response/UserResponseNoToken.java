package com.example.finpick.api.service.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class UserResponseNoToken {
    private Long id;
    private String name;
}
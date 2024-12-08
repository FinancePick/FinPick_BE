package com.example.finpick.api.service.user.response;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserResponseNoToken {
    private Long id;
    private String name;
}
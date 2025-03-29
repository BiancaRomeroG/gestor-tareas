package com.example.gestortareas.data.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}

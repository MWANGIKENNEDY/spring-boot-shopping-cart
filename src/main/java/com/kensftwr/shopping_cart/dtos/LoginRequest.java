package com.kensftwr.shopping_cart.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}


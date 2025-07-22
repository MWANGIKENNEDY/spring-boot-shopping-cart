package com.kensftwr.shopping_cart.dtos;

import lombok.Data;

@Data
public class UserCreateRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}

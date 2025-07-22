package com.kensftwr.shopping_cart.service.user;

import com.kensftwr.shopping_cart.dtos.UserCreateRequest;
import com.kensftwr.shopping_cart.dtos.UserDto;
import com.kensftwr.shopping_cart.dtos.UserUpdateRequest;
import com.kensftwr.shopping_cart.models.User;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(UserCreateRequest userCreateRequest);


    User updateUser(UserUpdateRequest userUpdateRequest, Long userId);

    void deleteUser(Long userId);

    //expose this method to service level
    UserDto convertUserDto(User user);
}

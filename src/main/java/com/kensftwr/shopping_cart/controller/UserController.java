package com.kensftwr.shopping_cart.controller;

import com.kensftwr.shopping_cart.dtos.ApiResponse;
import com.kensftwr.shopping_cart.dtos.UserCreateRequest;
import com.kensftwr.shopping_cart.dtos.UserDto;
import com.kensftwr.shopping_cart.dtos.UserUpdateRequest;
import com.kensftwr.shopping_cart.exceptions.ResourceNotFoundException;
import com.kensftwr.shopping_cart.models.Order;
import com.kensftwr.shopping_cart.models.User;
import com.kensftwr.shopping_cart.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try{
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Success!",userDto));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error fetching a single user!",e.getMessage()));
        }
    }

    @PostMapping("/add/user")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateRequest userCreateRequest){
        try{
            User user = userService.createUser(userCreateRequest);
            UserDto userDto = userService.convertUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Success!",userDto));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @PutMapping("/edit/{userId}/user")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                                  @PathVariable Long userId){
        try{
            User user = userService.updateUser(userUpdateRequest,userId);
            UserDto userDto = userService.convertUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Success!",userDto));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/delete/{userId}/user")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId){
        try{
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User deleted!",null));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}

package com.kensftwr.shopping_cart.service.user;

import com.kensftwr.shopping_cart.dtos.UserCreateRequest;
import com.kensftwr.shopping_cart.dtos.UserDto;
import com.kensftwr.shopping_cart.dtos.UserUpdateRequest;
import com.kensftwr.shopping_cart.exceptions.AlreadyExistsException;
import com.kensftwr.shopping_cart.exceptions.ResourceNotFoundException;
import com.kensftwr.shopping_cart.models.Role;
import com.kensftwr.shopping_cart.models.User;
import com.kensftwr.shopping_cart.repository.RoleRepository;
import com.kensftwr.shopping_cart.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User getUserById(Long userId) {
        return  userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found!"));
    }

    @Override
    @Transactional
    public User createUser(UserCreateRequest userCreateRequest) {
        return Optional.of(userCreateRequest)
                .filter(user -> !userRepository.existsByEmail(userCreateRequest.getEmail()))
                .map(req -> {
                    User user  = new User();
                    user.setEmail(userCreateRequest.getEmail());
                    //encrypt password before saving it to DB
                    user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
                    user.setFirstName(userCreateRequest.getFirstName());
                    user.setLastName(userCreateRequest.getLastName());
                    Role userRole = roleRepository.findByName("USER")
                            .orElseGet(() -> {
                                Role newRole = new Role("USER");
                                return roleRepository.save(newRole);
                            });
                    user.getRoles().add(userRole);

                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException("User with that email already exists!"));
    }

    @Override
    public User updateUser(UserUpdateRequest userUpdateRequest, Long userId) {
        return userRepository.findById(userId).map(existingUser-> {
            existingUser.setFirstName(userUpdateRequest.getFirstName());
            existingUser.setLastName(userUpdateRequest.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(()-> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository :: delete, ()->{
            throw new ResourceNotFoundException("User not found!");
        });
    }

    //expose this method to service level
    @Override
    public UserDto convertUserDto(User user){
        return modelMapper.map(user,UserDto.class);
    }

    //get the authenticated user

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

}

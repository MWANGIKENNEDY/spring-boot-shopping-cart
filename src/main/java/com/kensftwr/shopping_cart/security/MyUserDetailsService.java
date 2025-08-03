package com.kensftwr.shopping_cart.security;

import com.kensftwr.shopping_cart.models.User;
import com.kensftwr.shopping_cart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepo.findByEmail(email)).orElseThrow(()->new UsernameNotFoundException("User not found!"));
        return MyUserDetails.createUserDetails(user);
    }
}


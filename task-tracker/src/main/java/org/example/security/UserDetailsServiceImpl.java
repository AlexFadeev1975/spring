package org.example.security;


import lombok.RequiredArgsConstructor;
import org.example.services.UserService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

   private final UserService userService;


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.findUserFromUsername(username).map(UserDetailsImpl::build);
    }
}

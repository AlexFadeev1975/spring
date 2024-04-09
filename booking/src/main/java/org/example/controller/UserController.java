package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/api/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public UserDto createUser(@RequestBody @Valid UserDto dto) {

        return userService.create(dto);

    }
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {

        return userService.findAll(PageRequest.of(0, 10));
    }

    @GetMapping("/search/{userName}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto getAllUsersByName(@PathVariable("userName") String userName) {

        return userService.findByUsername(userName);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto getUserById(@PathVariable("id") String id) {

        return userService.findById(Long.parseLong(id));
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto update(@RequestBody UserDto dto) {

        return userService.update(dto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {

        return userService.delete(userId);


    }
}

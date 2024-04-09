package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/v1/list")
    public List<User> getAllUsers() {

        return userService.findAll(PageRequest.of(0, 10));
    }

    @GetMapping("/v1/search/{userName}")
    public UserDto getAllUsersByName(@PathVariable("userName") @Valid String userName) {

        return userService.findByUsername(userName);
    }

    @GetMapping("/v1/find/{id}")
    public UserDto getUserById(@PathVariable("id") @Valid String id) {

        return userService.findById(Long.parseLong(id));
    }


    @PutMapping("/v1/update")
    public UserDto update(@RequestBody UserDto dto) {

        return userService.update(dto);
    }

    @DeleteMapping("/v1/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") @Valid Long userId) {

        userService.delete(userId);

        return ResponseEntity.ok("User успешно удален");
    }
}

package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public UserDto createUser(@RequestBody @Valid UserDto dto) {

        return userService.create(dto);

    }

    @GetMapping("/list")
    public List<User> getAllUsers() {

        return userService.findAll(PageRequest.of(0, 10));
    }

    @PutMapping("/update")
    public UserDto update(@RequestBody @Valid UserDto dto) {

        return userService.update(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String userId) {

        userService.delete(userId);

        return ResponseEntity.ok("User успешно удален");
    }


}
package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.mappers.NewsMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final NewsMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto create(UserDto dto) {

        Optional<User> usr = repository.findByEmail(dto.getEmail());

        if (usr.isPresent()) {
            throw new RuntimeException("User with such email already exist");
        }

        String password = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(password);


        User user = repository.save(mapper.userDtoToUser(dto));

        return mapper.userToUserDto(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> findAll(Pageable pageable) {

        return repository.findAll(pageable).getContent();
    }

    public UserDto findById(Long id) {

        User user = repository.findById(id).orElseThrow();

        return mapper.userToUserDto(user);
    }

    public UserDto update(UserDto dto) {

        User user = repository.findById(Long.parseLong(dto.getId())).orElseThrow();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return mapper.userToUserDto(repository.save(user));
    }

    public void delete(Long userId) {

        User user = repository.findById(userId).orElseThrow();

        repository.delete(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserDto findByUsername(String email) {

        User user = repository.findByEmail(email).orElseThrow();

        return mapper.userToUserDto(user);

    }


}

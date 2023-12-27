package org.example.services;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.mappers.NewsMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final NewsMapper mapper;

    @Transactional
    public UserDto create(UserDto dto) {

        User user = repository.save(mapper.userDtoToUser(dto));

        return mapper.userToUserDto(user);
    }


    public List<User> findAll(Pageable pageable) {

        return repository.findAll(pageable).getContent();
    }

    public User findById(Long id) {

        return repository.findById(id).orElse(null);
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


}

package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.exceptions.SuchItemExistException;
import org.example.mappers.UserMapper;
import org.example.model.User;
import org.example.model.enums.RoleType;
import org.example.model.kafka.RegMessage;
import org.example.repository.UserRepository;
import org.example.search.filters.UserFilterBuilder;
import org.example.search.utils.Filter;
import org.example.search.utils.SpecificationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;

    private final UserFilterBuilder userFilterBuilder;

    private final SpecificationBuilder<User> specificationBuilder;

    @Value(value = "${spring.kafka.kafkaRegMessageTopic}")
    private String staticTopic;


    private final KafkaTemplate<String, RegMessage> kafkaTemplate;


    @Transactional
    public UserDto create(UserDto dto) {

        if (isExistUserWithUserNameAndEmail(dto.getUserName(), dto.getEmail())) {
            throw new SuchItemExistException("Пользователь с таким именем уже существует");
        }

        String password = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(password);


        User user = repository.save(mapper.userDtoToUser(dto));

        kafkaTemplate.send(staticTopic, new RegMessage(user.getId().toString()));


        return mapper.userToUserDto(user);
    }


    public List<User> findAll(Pageable pageable) {

        return repository.findAll(pageable).getContent();
    }

    public UserDto findById(Long id) {

        User user = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Пользователь с таким id не найден"));

        return mapper.userToUserDto(user);
    }

    public UserDto update(UserDto dto) {

        User user = repository.findById(dto.getId()).orElseThrow(() -> new NoSuchElementException("Пользователь с таким именем не найден"));

        if (!dto.getUserName().isEmpty()) {
            user.setUserName(dto.getUserName());
        }
        ;
        if (!dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }
        if (!dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (!dto.getStringRole().isEmpty()) {
            user.setRole(RoleType.valueOf(dto.getStringRole().toUpperCase()));
        }
        return mapper.userToUserDto(repository.save(user));
    }

    public ResponseEntity<?> delete(Long userId) {

        User user = repository.findById(userId).orElseThrow(() -> new NoSuchElementException("Пользователь с таким именем не существует"));

        repository.delete(user);

        return ResponseEntity.ok("Пользователь успешно удален");
    }


    public UserDto findByUsername(String userName) {

        User user = repository.findByUserName(userName).orElseThrow(() -> new NoSuchElementException("Пользователь с таким именем не найден"));

        return mapper.userToUserDto(user);

    }

    public boolean isExistUserWithUserNameAndEmail(String userName, String email) {

        List<Filter> filters = userFilterBuilder.createFilter(userName, email);

        Specification<User> specification = specificationBuilder.getSpecificationFromFilters(filters);

        List<User> users = repository.findAll(specification);

        return !users.isEmpty();
    }

}

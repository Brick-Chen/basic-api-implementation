package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user/register")
    public ResponseEntity registration(@Valid @RequestBody UserDto userDto) {
        UserEntity userEntity = UserEntity.builder()
                .userName(userDto.getName())
                .age(userDto.getAge())
                .gender(userDto.getGender())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .build();
        userRepository.save(userEntity);
//        userService.getUsersList().add(userDto);
//        int pos = userService.getUsersList().size();
//        HttpHeaders httpHeaders = userService.setHeaders(pos);
//        return ResponseEntity.status(201).headers(httpHeaders).build();
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUserList() {
        return ResponseEntity.ok(userService.getUsersList());
    }
}

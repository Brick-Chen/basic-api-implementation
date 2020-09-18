package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.CommentError;
import com.thoughtworks.rslist.exceptions.InvalidUserIdException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    private final UserRepository userRepository;

    private final RsEventRepository rsEventRepository;

    public UserController(UserRepository userRepository,RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
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

        return ResponseEntity.status(201).build();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) throws InvalidUserIdException {
        Optional<UserEntity> target = userRepository.findById(id);
        if (target.isPresent()) {
            UserEntity userEntity = target.get();
            UserDto userDto = UserDto.builder()
                    .name(userEntity.getUserName())
                    .age(userEntity.getAge())
                    .gender(userEntity.getGender())
                    .email(userEntity.getEmail())
                    .phone(userEntity.getPhone())
                    .build();
            return ResponseEntity.ok(userDto);
        }
        throw new InvalidUserIdException();
    }

    @DeleteMapping("/del/users/{id}")
    public ResponseEntity deleteUserById(@PathVariable Integer id) {
        Optional<UserEntity> target = userRepository.findById(id);
        if (target.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(400).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUserList() {
        return ResponseEntity.ok(userService.getUsersList());
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<CommentError> handleInvalidUserIdException(InvalidUserIdException invalidUserIdException) {
        CommentError commentError = new CommentError();
        commentError.setErrorMessage("invalid param");
        return ResponseEntity.badRequest().body(commentError);
    }

    private static UserDto mapFromUserEntityToUserDto(UserEntity userEntity) {
        return null;
    }
}

package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exceptions.CommentError;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity registration(@Valid @RequestBody UserDto userDto) {
        userService.getUsersList().add(userDto);
        int pos = userService.getUsersList().size();
        HttpHeaders httpHeaders = userService.setHeaders(pos);
        return ResponseEntity.status(201).headers(httpHeaders).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUserList() {
        return ResponseEntity.ok(userService.getUsersList());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommentError> handleInvalidUserRegisterException(MethodArgumentNotValidException ex) {
        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.status(400).body(commentError);
    }
}

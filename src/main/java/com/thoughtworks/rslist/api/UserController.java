package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
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

    @PostMapping("/user/register")
    public ResponseEntity registration(@Valid @RequestBody UserDto userDto) {
        userService.getUsersList().add(userDto);
        int pos = userService.getUsersList().size();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("index", String.valueOf(pos));
        return ResponseEntity.status(201).headers(httpHeaders).build();
//        return ResponseEntity.created(null).build();
    }

    @GetMapping("/user/list")
    public ResponseEntity<List<UserDto>> getUserList() {
        return ResponseEntity.ok(userService.getUsersList());
    }
}

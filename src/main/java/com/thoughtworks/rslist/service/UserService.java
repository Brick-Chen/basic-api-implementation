package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    List<UserDto> users = new ArrayList<>();

    public List<UserDto> getUsersList() {
        return users;
    }


}

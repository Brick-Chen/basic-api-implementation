package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    List<UserDto> users = new ArrayList<>();
    List<RsEvent> rsEvents = initRsEvent();

    public List<UserDto> getUsersList() {
        return users;
    }

    public List<RsEvent> getRsEvents() {
        return rsEvents;
    }

    private List<RsEvent> initRsEvent() {
        List<RsEvent> events = new ArrayList<>();
        return events;
    }

    public HttpHeaders setHeaders(int num) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("index", String.valueOf(num));
        return  httpHeaders;
    }

}

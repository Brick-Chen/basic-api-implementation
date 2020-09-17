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
        UserDto userDto =
                new UserDto("admin", 99, "male", "admin@twu.com","18888888888");
        events.add(new RsEvent("第一条事件","无分类",userDto));
        events.add(new RsEvent("第二条事件","无分类", userDto));
        events.add(new RsEvent("第三条事件", "无分类", userDto));
        users.add(userDto);
        return events;
    }

    public HttpHeaders setHeaders(int num) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("index", String.valueOf(num));
        return  httpHeaders;
    }

}

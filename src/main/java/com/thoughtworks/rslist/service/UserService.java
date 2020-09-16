package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
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
        events.add(new RsEvent("第一条事件","无分类",null));
        events.add(new RsEvent("第二条事件","无分类", null));
        events.add(new RsEvent("第三条事件", "无分类", null));
        return events;
    }

}

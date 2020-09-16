package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {

  @Autowired
  UserService userService;

  @GetMapping("/rs/{index}")
  public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index) {
    List<RsEvent> rsList = userService.getRsEvents();
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEvent>> getRsEventsByRange(@RequestParam(required = false) Integer start,
                                          @RequestParam(required = false) Integer end) {
    List<RsEvent> rsList = userService.getRsEvents();
    if (start == null || end == null) {
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start - 1, end));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) {
    List<RsEvent> rsList = userService.getRsEvents();
    List<UserDto> userList = userService.getUsersList();

    UserDto userDto = new UserDto(rsEvent.getUser());

    if (!userList.contains(userDto)) {
      userList.add(userDto);
    }
    rsList.add(rsEvent);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("index", String.valueOf(rsList.size()));
    return ResponseEntity.status(201).headers(httpHeaders).build();
//    return ResponseEntity.created(null).build();
  }

  @PostMapping("/rs/event/{index}")
  public ResponseEntity modifyEvent(@PathVariable int index, @RequestBody String modificationInfo) throws Exception {
    List<RsEvent> rsList = userService.getRsEvents();
    RsEvent target = rsList.get(index - 1);
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent event = objectMapper.readValue(modificationInfo, RsEvent.class);

    String modifyName = event.getEventName();
    if (modifyName != null && modifyName.length() != 0) {
      target.setEventName(modifyName);
    }

    String modifyKeyword = event.getKeyword();
    if(modifyKeyword != null && modifyKeyword.length() != 0) {
      target.setKeyword(modifyKeyword);
    }
    return ResponseEntity.created(null).build();
  }

  @PostMapping("/rs/event/del/{index}")
  public ResponseEntity delEvent(@PathVariable int index) {
    List<RsEvent> rsList = userService.getRsEvents();
    if (index < 1 || index > rsList.size()) {
      return ResponseEntity.created(null).build();
    }
    rsList.remove(index - 1);
    return ResponseEntity.created(null).build();
  }

}

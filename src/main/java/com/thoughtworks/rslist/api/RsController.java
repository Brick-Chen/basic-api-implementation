package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exceptions.RsListIndexOutOfBoundException;
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

  @JsonView(RsEvent.WithoutUserView.class)
  @GetMapping("/rs/{index}")
  public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index) throws RsListIndexOutOfBoundException {
    List<RsEvent> rsList = userService.getRsEvents();
    if (index < 1 || index > rsList.size()) {
      throw new RsListIndexOutOfBoundException();
    }
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @JsonView(RsEvent.WithoutUserView.class)
  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEvent>> getRsEventsByRange(@RequestParam(required = false) Integer start,
                                          @RequestParam(required = false) Integer end) {
    List<RsEvent> rsList = userService.getRsEvents();
    if (start == null || end == null) {
      return ResponseEntity.ok(rsList);
    }
    if (start < 1 || start > rsList.size() || end < 0 || end > rsList.size()) {
      throw new IndexOutOfBoundsException();
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
    HttpHeaders httpHeaders = userService.setHeaders(rsList.size());
    return ResponseEntity.status(201).headers(httpHeaders).build();
  }

  @PutMapping("/rs/event/{index}")
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
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/event/del/{index}")
  public ResponseEntity delEvent(@PathVariable int index) {
    List<RsEvent> rsList = userService.getRsEvents();
    if (index < 1 || index > rsList.size()) {
      return ResponseEntity.status(200).build();
    }
    rsList.remove(index - 1);
    return ResponseEntity.status(200).build();
  }

}

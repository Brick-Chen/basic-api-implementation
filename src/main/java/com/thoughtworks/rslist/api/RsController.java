package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RsController {

  @Autowired
  UserService userService;

  @GetMapping("/rs/{index}")
  public RsEvent getRsEvent(@PathVariable int index) {
    List<RsEvent> rsList = userService.getRsEvents();
    return rsList.get(index - 1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventsByRange(@RequestParam(required = false) Integer start,
                                          @RequestParam(required = false) Integer end) {
    List<RsEvent> rsList = userService.getRsEvents();
    if (start == null || end == null) {
      return rsList;
    }
    return rsList.subList(start - 1, end);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody RsEvent rsEvent) {
    List<RsEvent> rsList = userService.getRsEvents();
    rsList.add(rsEvent);
  }

  @PostMapping("/rs/event/{index}")
  public void modifyEvent(@PathVariable int index, @RequestBody String modificationInfo) throws Exception {
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
  }

  @PostMapping("/rs/event/del/{index}")
  public void delEvent(@PathVariable int index) {
    List<RsEvent> rsList = userService.getRsEvents();
    if (index < 1 || index > rsList.size()) {
      return;
    }
    rsList.remove(index - 1);
  }

}

package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsEvent();

  private List<RsEvent> initRsEvent() {
    List<RsEvent> events = new ArrayList<>();
    events.add(new RsEvent("第一条事件", "无分类"));
    events.add(new RsEvent("第二条事件", "无分类"));
    events.add(new RsEvent("第三条事件", "无分类"));
    return events;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsEvent(@PathVariable int index) {
    return rsList.get(index - 1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventsByRange(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if (start == null || end == null) {
      return rsList;
    }
    return rsList.subList(start - 1, end);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody RsEvent rsEvent) {
    rsList.add(rsEvent);
  }

  @PostMapping("/rs/event/{index}")
  public void modifyEvent(@PathVariable int index, @RequestBody String modificationInfo) throws JsonProcessingException {
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
    if (index < 1 || index > rsList.size()) {
      return;
    }
    rsList.remove(index - 1);
  }
}

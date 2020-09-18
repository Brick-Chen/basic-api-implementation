package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exceptions.CommentError;
import com.thoughtworks.rslist.exceptions.InvalidRsEventIndexException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

  @Autowired
  UserService userService;

  private final UserRepository userRepository;
  private final RsEventRepository rsEventRepository;

  public RsController(UserRepository userRepository, RsEventRepository rsEventRepository) {
    this.userRepository = userRepository;
    this.rsEventRepository = rsEventRepository;
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) {
    if (!userRepository.existsById(rsEvent.getUserId())) {
      return ResponseEntity.badRequest().build();
    }
    RsEventEntity rsEventEntity = RsEventEntity.builder()
            .eventName(rsEvent.getEventName())
            .keyword(rsEvent.getKeyword())
            .userId(rsEvent.getUserId())
            .build();
    rsEventRepository.save(rsEventEntity);
    return ResponseEntity.created(null).build();
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index) throws InvalidRsEventIndexException {
    Optional<RsEventEntity> rsEventEntity= rsEventRepository.findById(index);
    if (!rsEventEntity.isPresent()) {
      throw new InvalidRsEventIndexException();
    }
    RsEventEntity rsEvent = rsEventEntity.get();
    RsEvent target = RsEvent.builder()
            .eventName(rsEvent.getEventName())
            .keyword(rsEvent.getKeyword())
            .build();
    return ResponseEntity.ok(target);
  }

//  @GetMapping("/rs/list")
//  public ResponseEntity<List<RsEvent>> getRsEventsByRange(@RequestParam(required = false) Integer start,
//                                          @RequestParam(required = false) Integer end) {
//    List<RsEvent> rsList = userService.getRsEvents();
//    if (start == null || end == null) {
//      return ResponseEntity.ok(rsList);
//    }
//    return ResponseEntity.ok(rsList.subList(start - 1, end));
//  }


//  @PutMapping("/rs/event/{index}")
//  public ResponseEntity modifyEvent(@PathVariable int index, @RequestBody String modificationInfo) throws Exception {
//    List<RsEvent> rsList = userService.getRsEvents();
//    RsEvent target = rsList.get(index - 1);
//    ObjectMapper objectMapper = new ObjectMapper();
//    RsEvent event = objectMapper.readValue(modificationInfo, RsEvent.class);
//
//    String modifyName = event.getEventName();
//    if (modifyName != null && modifyName.length() != 0) {
//      target.setEventName(modifyName);
//    }
//
//    String modifyKeyword = event.getKeyword();
//    if(modifyKeyword != null && modifyKeyword.length() != 0) {
//      target.setKeyword(modifyKeyword);
//    }
//    return ResponseEntity.ok().build();
//  }
//
//  @DeleteMapping("/rs/event/del/{index}")
//  public ResponseEntity delEvent(@PathVariable int index) {
//    List<RsEvent> rsList = userService.getRsEvents();
//    if (index < 1 || index > rsList.size()) {
//      return ResponseEntity.status(200).build();
//    }
//    rsList.remove(index - 1);
//    return ResponseEntity.status(200).build();
//  }

  @ExceptionHandler(InvalidRsEventIndexException.class)
  public ResponseEntity<CommentError> handleInvalidRsEventIndex(InvalidRsEventIndexException ex) {
    CommentError commentError = new CommentError();
    commentError.setErrorMessage("invalid id");
    return ResponseEntity.badRequest().body(commentError);
  }


}

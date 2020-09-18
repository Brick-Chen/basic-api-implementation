package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exceptions.CommentError;
import com.thoughtworks.rslist.exceptions.InvalidRsEventIndexException;
import com.thoughtworks.rslist.exceptions.InvalidUserIdException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class RsController {

  @Autowired
  UserService userService;

  private final UserRepository userRepository;
  private final RsEventRepository rsEventRepository;
  private final VoteRepository voteRepository;

  public RsController(UserRepository userRepository, RsEventRepository rsEventRepository,
                      VoteRepository voteRepository) {
    this.userRepository = userRepository;
    this.rsEventRepository = rsEventRepository;
    this.voteRepository = voteRepository;
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) {
    if (!userRepository.existsById(rsEvent.getUserId())) {
      return ResponseEntity.badRequest().build();
    }
    RsEventEntity rsEventEntity = RsEventEntity.builder()
            .eventName(rsEvent.getEventName())
            .keyword(rsEvent.getKeyword())
            .user(UserEntity
                    .builder()
                    .id(rsEvent.getUserId())
                    .build())
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
    UserEntity user = rsEvent.getUser();

    RsEvent target = RsEvent.builder()
            .eventName(rsEvent.getEventName())
            .keyword(rsEvent.getKeyword())
            .user(new UserDto(user.getUserName(),
                    user.getAge(),
                    user.getGender(),
                    user.getEmail(),
                    user.getPhone()))
            .build();
    return ResponseEntity.ok(target);
  }

  @PatchMapping("/rs/{rsEventId}")
  public ResponseEntity updateRsEvent(@Valid @RequestBody RsEvent rsEvent, @PathVariable int rsEventId)
          throws Exception {
    Optional<RsEventEntity> targetEvent = rsEventRepository.findById(rsEventId);
    if(!targetEvent.isPresent()) {
      throw new InvalidRsEventIndexException();
    }

    RsEventEntity targetEventEntity = targetEvent.get();
    if (rsEvent.getUserId() != targetEventEntity.getUser().getId()) {
      return ResponseEntity.badRequest().build();
    }

    String modifiedKeyword = rsEvent.getKeyword();
    String modifiedEventName = rsEvent.getEventName();
    if (modifiedEventName != null && !modifiedEventName.equals("")) {
      targetEventEntity.setEventName(modifiedEventName);
    }

    if(modifiedKeyword != null && !modifiedKeyword.equals("")) {
      targetEventEntity.setKeyword(modifiedKeyword);
    }
    rsEventRepository.save(targetEventEntity);
    return ResponseEntity.created(null).build();
  }

  @PostMapping("/rs/vote/{rsEventId}")
  public ResponseEntity voteRsEvent(@RequestBody Vote vote, @PathVariable int rsEventId) {
    Optional<RsEventEntity> rsEventEntity = rsEventRepository.findById(rsEventId);
    Optional<UserEntity> userEntity = userRepository.findById(vote.getUserId());
    if (!rsEventEntity.isPresent() || !userEntity.isPresent() || userEntity.get().getVoteNum() < vote.getVoteNum()) {
      return ResponseEntity.badRequest().build();
    }
    VoteEntity voteEntity = VoteEntity.builder()
            .user(userEntity.get())
            .num(vote.getVoteNum())
            .rsEvent(rsEventEntity.get())
            .time(vote.getVoteTime())
            .build();
    voteRepository.save(voteEntity);

    UserEntity user = userEntity.get();
    user.setVoteNum(user.getVoteNum() - vote.getVoteNum());
    userRepository.save(user);
    return ResponseEntity.created(null).build();
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

  @ExceptionHandler(InvalidUserIdException.class)
  public ResponseEntity<CommentError> handleInvalidUserIdException(InvalidUserIdException invalidUserIdException) {
    CommentError commentError = new CommentError();
    commentError.setErrorMessage("invalid param");
    return ResponseEntity.badRequest().body(commentError);
  }


}

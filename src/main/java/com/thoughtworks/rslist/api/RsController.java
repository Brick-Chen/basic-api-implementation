package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exceptions.CommentError;
import com.thoughtworks.rslist.exceptions.InvalidRequestParam;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

  @PostMapping("/rsEvent")
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
            .voteNum(rsEvent.getVoteNum())
            .id(rsEvent.getId())
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

  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEvent>> getRsEventsByRange(@RequestParam(required = false) Integer start,
                                                          @RequestParam(required = false) Integer end) throws InvalidRequestParam {
    List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
    if (start == null || end == null) {
      List<RsEvent> events =  rsEventEntities.stream()
              .map(RsController::mapFromRsEventEntityToRsEvent).collect(Collectors.toList());
      return ResponseEntity.ok(events);
    }

    if(start < 1 || start > rsEventEntities.size() || end < start || end > rsEventEntities.size()) {
      throw new InvalidRequestParam();
    }
    List<RsEvent> subEvents = rsEventEntities.subList(start - 1, end).stream()
            .map(RsController::mapFromRsEventEntityToRsEvent).collect(Collectors.toList());
    return ResponseEntity.ok(subEvents);
  }

  @DeleteMapping("/rs/del/{index}")
  public ResponseEntity delEvent(@PathVariable int index) {
    if (index < 1) {
      throw new IndexOutOfBoundsException();
    }
    Optional<RsEventEntity> target = rsEventRepository.findById(index);
    if (target.isPresent()) {
      rsEventRepository.deleteById(index);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.status(400).build();
  }

  @ExceptionHandler(InvalidRsEventIndexException.class)
  public ResponseEntity<CommentError> handleInvalidRsEventIndex(InvalidRsEventIndexException ex) {
    CommentError commentError = new CommentError();
    commentError.setErrorMessage("invalid id");
    return ResponseEntity.badRequest().body(commentError);
  }

  @ExceptionHandler(IndexOutOfBoundsException.class)
  public ResponseEntity<CommentError> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
    CommentError commentError = new CommentError();
    commentError.setErrorMessage("index out of bounds");
    return ResponseEntity.badRequest().body(commentError);
  }

  @ExceptionHandler(InvalidUserIdException.class)
  public ResponseEntity<CommentError> handleInvalidUserIdException(InvalidUserIdException invalidUserIdException) {
    CommentError commentError = new CommentError();
    commentError.setErrorMessage("invalid param");
    return ResponseEntity.badRequest().body(commentError);
  }

  @ExceptionHandler(InvalidRequestParam.class)
  public ResponseEntity<CommentError> handleInvalidRequestParamException(InvalidRequestParam ex) {
    CommentError commentError = new CommentError();
    commentError.setErrorMessage("invalid request param");
    return ResponseEntity.badRequest().body(commentError);
  }

  private static RsEvent mapFromRsEventEntityToRsEvent(RsEventEntity rsEventEntity) {
    if (rsEventEntity == null) {
      return null;
    }
    return RsEvent.builder()
            .eventName(rsEventEntity.getEventName())
            .keyword(rsEventEntity.getKeyword())
            .id(rsEventEntity.getId())
            .voteNum(rsEventEntity.getVoteNum())
            .build();
  }

}

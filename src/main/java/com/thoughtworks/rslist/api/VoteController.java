package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    private final VoteRepository voteRepository;

    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/votes/range")
    public ResponseEntity<List<Vote>> getVotesByRange(@RequestParam long start,
                                                      @RequestParam long end) {
        Timestamp time1 = new Timestamp(start);
        Timestamp time2 = new Timestamp(end);
        List<VoteEntity> voteEntities = voteRepository.findByTimeBetween(time1, time2);
        List<Vote> votes = voteEntities.stream()
                .map(VoteController::mapFromVoteEntityToVote).collect(Collectors.toList());
        return ResponseEntity.ok(votes);
    }

    private static Vote mapFromVoteEntityToVote(VoteEntity voteEntity) {
        if (voteEntity == null) {
            return null;
        }
        return Vote.builder()
                .voteTime(voteEntity.getTime())
                .userId(voteEntity.getUser().getId())
                .rsEventId(voteEntity.getRsEvent().getId())
                .voteNum(voteEntity.getNum())
                .build();
    }
}

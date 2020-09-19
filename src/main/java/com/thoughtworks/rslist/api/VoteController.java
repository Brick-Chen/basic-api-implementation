package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class VoteController {
    private final VoteRepository voteRepository;

    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/votes/range")
    public ResponseEntity<List<Vote>> getVotesByRange(@RequestParam String start,
                                                      @RequestParam String end) {
        LocalDateTime time1 = LocalDateTime.parse(start);
        LocalDateTime time2 = LocalDateTime.parse(end);
        List<VoteEntity> voteEntities = voteRepository.findAllByTimeBetween(time1, time2);
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

package com.thoughtworks.rslist.apiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.sql.Timestamp;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    private UserEntity userEntity;

    private RsEventEntity rsEventEntity;

    void deleteAllData() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        deleteAllData();
        setData();
    }

    @Test
    public void should_return_votes_by_range() throws Exception {
        Timestamp start = Timestamp.valueOf("2020-07-01 00:00:00");
        Timestamp end = Timestamp.valueOf("2020-08-31 23:59:59");

        ObjectMapper objectMapper = new ObjectMapper();
        String time1 = objectMapper.writeValueAsString(start);
        String time2 = objectMapper.writeValueAsString(end);

        mockMvc.perform(get("/votes/range?start=" + time1 + "&end=" + time2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].voteNum", is(2)))
                .andExpect(jsonPath("$[0].userId", is(userEntity.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$[1].voteNum", is(4)))
                .andExpect(jsonPath("$[1].userId", is(userEntity.getId())))
                .andExpect(jsonPath("$[1].rsEventId", is(rsEventEntity.getId())));
    }

    void setData() {
        userEntity = UserEntity.builder()
                .userName("chen")
                .age(24)
                .gender("male")
                .email("cym@twu.com")
                .phone("15297134217")
                .voteNum(10)
                .build();

        userRepository.save(userEntity);

        rsEventEntity = RsEventEntity.builder()
                .eventName("只有风暴才能击倒大树")
                .keyword("游戏类")
                .user(userEntity)
                .build();

        rsEventRepository.save(rsEventEntity);

        Timestamp voteTime1 = Timestamp.valueOf("2020-07-27 10:31:11");
        Timestamp voteTime2 = Timestamp.valueOf("2020-06-13 08:30:27");
        Timestamp voteTime3 = Timestamp.valueOf("2020-08-20 22:10:11");

        VoteEntity voteEntity = VoteEntity.builder()
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .num(2)
                .time(voteTime1)
                .build();
        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.builder()
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .num(3)
                .time(voteTime2)
                .build();
        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.
                builder()
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .time(voteTime3)
                .num(4)
                .build();
        voteRepository.save(voteEntity);
    }
}

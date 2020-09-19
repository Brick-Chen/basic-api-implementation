package com.thoughtworks.rslist.apiTest;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
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
import java.time.LocalDateTime;

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

//    @AfterEach
//    void deleteAllData() {
//        userRepository.deleteAll();
//        rsEventRepository.deleteAll();
//        voteRepository.deleteAll();
//    }
//
//    @BeforeEach
//    void setUp() {
//        setData();
//    }

   // @Test
//    public void should_return_votes_by_range() throws Exception {
//        String start = "2020-07-01T00:00:00";
//        String end = "2020-08-31T23:59:59";
//
//        mockMvc.perform(get("/votes/range")
//                .param("start", start)
//                .param("end", end))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].eventName", is("只有风暴才能击倒大树")))
//                .andExpect(jsonPath("$[0].keyword", is("游戏类")))
//                .andExpect(jsonPath("$[0].voteNum", is(2)))
//                .andExpect(jsonPath("$[1].eventName", is("只有风暴才能击倒大树")))
//                .andExpect(jsonPath("$[1].keyword", is("游戏类")))
//                .andExpect(jsonPath("$[1].voteNum", is(4)));
//    }

//    void setData() {
//        userEntity = UserEntity.builder()
//                .userName("chen")
//                .age(24)
//                .gender("male")
//                .email("cym@twu.com")
//                .phone("15297134217")
//                .voteNum(10)
//                .build();
//
//        userRepository.save(userEntity);
//
//        rsEventEntity = RsEventEntity.builder()
//                .eventName("只有风暴才能击倒大树")
//                .keyword("游戏类")
//                .user(userEntity)
//                .build();
//
//        rsEventRepository.save(rsEventEntity);
//
//        LocalDateTime time1 = LocalDateTime.parse("2020-07-27T10:31:11");
//        LocalDateTime time2 = LocalDateTime.parse("2020-06-13T08:30:27");
//        LocalDateTime time3 = LocalDateTime.parse("2020-08-20T22:10:11");
//
//        VoteEntity voteEntity = VoteEntity.builder()
//                .user(userEntity)
//                .rsEvent(rsEventEntity)
//                .num(2)
//                .time(time1)
//                .build();
//        voteRepository.save(voteEntity);
//
//        voteEntity = VoteEntity.builder()
//                .user(userEntity)
//                .rsEvent(rsEventEntity)
//                .num(3)
//                .time(time2)
//                .build();
//        voteRepository.save(voteEntity);
//
//        voteEntity = VoteEntity.
//                builder()
//                .user(userEntity)
//                .rsEvent(rsEventEntity)
//                .time(time3)
//                .num(4)
//                .build();
//        voteRepository.save(voteEntity);
//    }
}

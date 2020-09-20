package com.thoughtworks.rslist.apiTest;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
    }

    @Test
    public void should_add_rs_event_when_user_exists() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("chen")
                .age(22)
                .gender("male")
                .email("xxx@123.com")
                .phone("15297134217")
                .voteNum(10)
                .build();
        userRepository.save(userEntity);

        String jason = "{\"eventName\":\"只有风暴才能击倒大树\", \"keyword\":\"游戏类\", \"userId\":"
                + userEntity.getId() + "}";

        mockMvc.perform(post("/rsEvent")
                .content(jason).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        Assertions.assertEquals(1, rsEvents.size());
        Assertions.assertEquals("只有风暴才能击倒大树", rsEvents.get(0).getEventName());
        Assertions.assertEquals("游戏类", rsEvents.get(0).getKeyword());
        Assertions.assertEquals(userEntity.getId(), rsEvents.get(0).getUser().getId());
        Assertions.assertEquals(0, rsEvents.get(0).getVoteNum());
    }

    @Test
    public void should_not_add_event_when_user_not_exists() throws Exception {
        String jason = "{\"eventName\":\"只有风暴才能击倒大树\", \"keyword\":\"游戏类\", \"userId\":1}";

        mockMvc.perform(post("/rsEvent")
                .content(jason).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        Assertions.assertEquals(0, rsEvents.size());
    }

    @Test
    public void should_return_one_rs_event() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("chen")
                .age(22)
                .gender("male")
                .email("xxx@123.com")
                .phone("15297134217")
                .voteNum(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("只有风暴才能击倒大树")
                .keyword("游戏类")
                .user(userEntity)
                .voteNum(0)
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("只有风暴才能击倒大树")))
                .andExpect(jsonPath("$.keyword", is("游戏类")))
                .andExpect(jsonPath("$.id", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$.voteNum", is(0)));
    }

    @Test
    public void should_not_update_rs_event_name_when_rs_id_not_matches_user_id() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("chen")
                .age(22)
                .gender("male")
                .email("xxx@123.com")
                .phone("15297134217")
                .voteNum(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("只有风暴才能击倒大树")
                .keyword("游戏类")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("只有风暴才能击倒大树")))
                .andExpect(jsonPath("$.keyword", is("游戏类")))
                .andExpect(jsonPath("$.id", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$.voteNum", is(0)));

        String jason = "{\"eventName\":\"现在是大老爹拿球\", \"keyword\":\"篮球类\", \"userId\":" + 20  + "}";
        mockMvc.perform(patch("/rs/{rsEventId}", rsEventEntity.getId())
                .content(jason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_rs_event_name_when_rs_id_matches_user_id() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("chen")
                .age(22)
                .gender("male")
                .email("xxx@123.com")
                .phone("15297134217")
                .voteNum(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("只有风暴才能击倒大树")
                .keyword("游戏类")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("只有风暴才能击倒大树")))
                .andExpect(jsonPath("$.keyword", is("游戏类")))
                .andExpect(jsonPath("$.id", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$.voteNum", is(0)));

        String jason = "{\"eventName\":\"现在是大老爹拿球\", \"keyword\":\"\", \"userId\":" + userEntity.getId()  + "}";
        mockMvc.perform(patch("/rs/{rsEventId}", rsEventEntity.getId())
                .content(jason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("现在是大老爹拿球")))
                .andExpect(jsonPath("$.keyword", is("游戏类")))
                .andExpect(jsonPath("$.id", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$.voteNum", is(0)));
    }

    @Test
    public void should_update_rs_keyword_name_when_rs_id_matches_user_id() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("chen")
                .age(22)
                .gender("male")
                .email("xxx@123.com")
                .phone("15297134217")
                .voteNum(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("只有风暴才能击倒大树")
                .keyword("游戏类")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("只有风暴才能击倒大树")))
                .andExpect(jsonPath("$.keyword", is("游戏类")))
                .andExpect(jsonPath("$.id", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$.voteNum", is(0)));

        String jason = "{\"eventName\":\"\", \"keyword\":\"篮球类\", \"userId\":" + userEntity.getId()  + "}";
        mockMvc.perform(patch("/rs/{rsEventId}", rsEventEntity.getId())
                .content(jason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("只有风暴才能击倒大树")))
                .andExpect(jsonPath("$.keyword", is("篮球类")))
                .andExpect(jsonPath("$.id", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$.voteNum", is(0)));
    }

    @Test
    public void should_update_rs_event_name_and_keyword_name_when_rs_id_matches_user_id() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("chen")
                .age(22)
                .gender("male")
                .email("xxx@123.com")
                .phone("15297134217")
                .voteNum(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("只有风暴才能击倒大树")
                .keyword("游戏类")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("只有风暴才能击倒大树")))
                .andExpect(jsonPath("$.keyword", is("游戏类")))
                .andExpect(jsonPath("$.id", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$.voteNum", is(0)));

        String jason = "{\"eventName\":\"现在是大老爹拿球\", \"keyword\":\"篮球类\", \"userId\":" + userEntity.getId()  + "}";
        mockMvc.perform(patch("/rs/{rsEventId}", rsEventEntity.getId())
                .content(jason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("现在是大老爹拿球")))
                .andExpect(jsonPath("$.keyword", is("篮球类")))
                .andExpect(jsonPath("$.id", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$.voteNum", is(0)));
    }

    @Test
    public void should_vote_rs_event_when_user_vote_tickets_num_greater_than_vote_num() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("chen")
                .age(22)
                .gender("male")
                .email("xxx@123.com")
                .phone("15297134217")
                .voteNum(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("只有风暴才能击倒大树")
                .keyword("游戏类")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);

        int userNum = userRepository.findAll().size();
        int rsEventNum = rsEventRepository.findAll().size();
        int voteNum = voteRepository.findAll().size();

        Assertions.assertEquals(1, userNum);
        Assertions.assertEquals(1, rsEventNum);
        Assertions.assertEquals(0, voteNum);

        Timestamp voteTime = new Timestamp(System.currentTimeMillis());
        ObjectMapper objectMapper = new ObjectMapper();

        Vote vote = Vote.builder()
                .voteNum(4)
                .voteTime(voteTime)
                .userId(userEntity.getId())
                .build();
        String jason = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/{rsEventId}",rsEventEntity.getId())
                .content(jason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<VoteEntity> voteRecords = voteRepository.findAll();
        userEntity = userRepository.findById(voteRecords.get(0).getUser().getId()).get();
        voteTime.setNanos(0);

        Assertions.assertEquals(4, voteRecords.get(0).getNum());
        Assertions.assertEquals(userEntity.getId(), voteRecords.get(0).getUser().getId());
        Assertions.assertEquals(rsEventEntity.getId(), voteRecords.get(0).getRsEvent().getId());
        Assertions.assertEquals(voteTime.toString(), voteRecords.get(0).getTime().toString());
        Assertions.assertEquals(6, userEntity.getVoteNum());
    }

    @Test
    public void should_not_vote_rs_event_when_user_vote_tickets_num_less_than_vote_num() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("chen")
                .age(22)
                .gender("male")
                .email("xxx@123.com")
                .phone("15297134217")
                .voteNum(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("只有风暴才能击倒大树")
                .keyword("游戏类")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);

        int userNum = userRepository.findAll().size();
        int rsEventNum = rsEventRepository.findAll().size();
        int voteNum = voteRepository.findAll().size();

        Assertions.assertEquals(1, userNum);
        Assertions.assertEquals(1, rsEventNum);
        Assertions.assertEquals(0, voteNum);

        Timestamp voteTime = new Timestamp(System.currentTimeMillis());
        ObjectMapper objectMapper = new ObjectMapper();

        Vote vote = Vote.builder()
                .voteNum(12)
                .voteTime(voteTime)
                .userId(userEntity.getId())
                .build();
        String jason = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventEntity.getId())
                .content(jason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<VoteEntity> voteRecords = voteRepository.findAll();
        Assertions.assertEquals(0, voteRecords.size());
    }
//
    @Test
    public void should_return_a_list_of_rs_events_by_range() throws Exception {
        RsEventEntity rsEventEntity1 = RsEventEntity.builder()
                .eventName("第一条事件")
                .keyword("无分类")
                .voteNum(1)
                .build();

        RsEventEntity rsEventEntity2 = RsEventEntity.builder()
                .eventName("第二条事件")
                .keyword("无分类")
                .voteNum(2)
                .build();

        RsEventEntity rsEventEntity3 = RsEventEntity.builder()
                .eventName("第三条事件")
                .keyword("无分类")
                .voteNum(3)
                .build();

        rsEventRepository.save(rsEventEntity1);
        rsEventRepository.save(rsEventEntity2);
        rsEventRepository.save(rsEventEntity3);

        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].voteNum", is(3)));
    }

    @Test
    public void should_delete_rs_event_given_number() throws Exception {
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("第一条事件")
                .keyword("无分类")
                .voteNum(1)
                .build();

        rsEventRepository.save(rsEventEntity);
        mockMvc.perform(delete("/rs/del/" + rsEventEntity.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}

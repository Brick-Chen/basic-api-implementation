package com.thoughtworks.rslist.apiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    public void should_rs_event_when_user_exists() throws Exception {
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

        mockMvc.perform(post("/rs/event")
                .content(jason).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        Assertions.assertEquals(1, rsEvents.size());
        Assertions.assertEquals("只有风暴才能击倒大树", rsEvents.get(0).getEventName());
        Assertions.assertEquals("游戏类", rsEvents.get(0).getKeyword());
        Assertions.assertEquals(userEntity.getId(), rsEvents.get(0).getUser().getId());
    }

    @Test
    public void should_not_add_event_when_user_not_exists() throws Exception {
        String jason = "{\"eventName\":\"只有风暴才能击倒大树\", \"keyword\":\"游戏类\", \"userId\":1}";

        mockMvc.perform(post("/rs/event")
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
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("只有风暴才能击倒大树")))
                .andExpect(jsonPath("$.keyword", is("游戏类")))
                .andExpect(jsonPath("$.user.user_name", is("chen")));
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
                .andExpect(jsonPath("$.user.user_name", is("chen")));

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
                .andExpect(jsonPath("$.user.user_name", is("chen")));

        String jason = "{\"eventName\":\"现在是大老爹拿球\", \"keyword\":\"\", \"userId\":" + userEntity.getId()  + "}";
        mockMvc.perform(patch("/rs/{rsEventId}", rsEventEntity.getId())
                .content(jason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("现在是大老爹拿球")))
                .andExpect(jsonPath("$.keyword", is("游戏类")))
                .andExpect(jsonPath("$.user.user_name", is("chen")));
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
                .andExpect(jsonPath("$.user.user_name", is("chen")));

        String jason = "{\"eventName\":\"\", \"keyword\":\"篮球类\", \"userId\":" + userEntity.getId()  + "}";
        mockMvc.perform(patch("/rs/{rsEventId}", rsEventEntity.getId())
                .content(jason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("只有风暴才能击倒大树")))
                .andExpect(jsonPath("$.keyword", is("篮球类")))
                .andExpect(jsonPath("$.user.user_name", is("chen")));
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
                .andExpect(jsonPath("$.user.user_name", is("chen")));

        String jason = "{\"eventName\":\"现在是大老爹拿球\", \"keyword\":\"篮球类\", \"userId\":" + userEntity.getId()  + "}";
        mockMvc.perform(patch("/rs/{rsEventId}", rsEventEntity.getId())
                .content(jason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/{index}", rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("现在是大老爹拿球")))
                .andExpect(jsonPath("$.keyword", is("篮球类")))
                .andExpect(jsonPath("$.user.user_name", is("chen")));
    }
//
//    @Test
//    public void should_return_a_list_of_rs_events_by_range() throws Exception {
//        mockMvc.perform(get("/rs/list?start=1&end=3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyword", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyword", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyword", is("无分类")));
//    }
//
//    @Test
//    public void should_not_add_a_rs_when_eventName_is_null() throws Exception {
//        UserDto user1 = new UserDto("haha", 20, "male", "aaa@123.com", "10123456789");
//        RsEvent rsEvent = new RsEvent("", "经济", user1);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_add_a_rs_when_keyword_is_null() throws Exception {
//        UserDto user1 = new UserDto("haha", 20, "male", "aaa@123.com", "10123456789");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "", user1);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_add_a_rs_when_user_name_not_valid() throws Exception {
//        UserDto user = new UserDto("", 24, "male", "aaa@123.com", "10123456789");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_add_a_rs_when_user_age_below_valid() throws Exception {
//        UserDto user1 = new UserDto("haha", 17, "male", "aaa@123.com", "10123456789");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_add_a_rs_when_user_age_above_valid() throws Exception {
//        UserDto user1 = new UserDto("haha", 101, "male", "aaa@123.com", "10123456789");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_add_a_rs_when_user_gender_is_null() throws Exception {
//        UserDto user1 = new UserDto("haha", 27, "", "aaa@123.com", "10123456789");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_add_a_rs_when_user_mail_not_valid() throws Exception {
//        UserDto user1 = new UserDto("haha", 20, "male", "aaa@", "10123456789");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_add_a_rs_when_user_phone_number_len_less_than_11() throws Exception {
//        UserDto user1 = new UserDto("haha", 20, "male", "aaa@123.com", "1012345678");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_not_add_a_rs_when_user_phone_number_len_more_than_11() throws Exception {
//        UserDto user1 = new UserDto("haha", 20, "male", "aaa@123.com", "101234567899");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void should_only_add_a_rs_event_when_username_is_in_user_list() throws Exception {
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].user_name", is("admin")))
//                .andExpect(jsonPath("$[0].user_age", is(99)))
//                .andExpect(jsonPath("$[0].user_gender", is("male")))
//                .andExpect(jsonPath("$[0].user_email", is("admin@twu.com")))
//                .andExpect(jsonPath("$[0].user_phone", is("18888888888")));
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyword", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyword", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyword", is("无分类")));
//
//        UserDto user = new UserDto("admin", 24, "male", "aaa@123.com", "10123456789");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        objectMapper.writerWithView(RsEvent.WithUserView.class).writeValue(byteArrayOutputStream, rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(byteArrayOutputStream.toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("index", "4"));
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyword", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyword", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyword", is("无分类")))
//                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
//                .andExpect(jsonPath("$[3].keyword", is("经济")));
//
//
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].user_name", is("admin")))
//                .andExpect(jsonPath("$[0].user_age", is(99)))
//                .andExpect(jsonPath("$[0].user_gender", is("male")))
//                .andExpect(jsonPath("$[0].user_email", is("admin@twu.com")))
//                .andExpect(jsonPath("$[0].user_phone", is("18888888888")));
//
//    }
//
//    @Test
//    public void should_add_both_rs_event_and_user_when_user_name_not_in_user_list() throws Exception {
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].user_name", is("admin")))
//                .andExpect(jsonPath("$[0].user_age", is(99)))
//                .andExpect(jsonPath("$[0].user_gender", is("male")))
//                .andExpect(jsonPath("$[0].user_email", is("admin@twu.com")))
//                .andExpect(jsonPath("$[0].user_phone", is("18888888888")));
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyword", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyword", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyword", is("无分类")));
//
//        UserDto user = new UserDto("max", 22, "male", "max@twu.com", "10123456789");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        objectMapper.writerWithView(RsEvent.WithUserView.class).writeValue(byteArrayOutputStream, rsEvent);
//        mockMvc.perform(post("/rs/event")
//                .content(byteArrayOutputStream.toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("index", "4"));
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyword", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyword", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyword", is("无分类")));
//
//
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].user_name", is("admin")))
//                .andExpect(jsonPath("$[0].user_age", is(99)))
//                .andExpect(jsonPath("$[0].user_gender", is("male")))
//                .andExpect(jsonPath("$[0].user_email", is("admin@twu.com")))
//                .andExpect(jsonPath("$[0].user_phone", is("18888888888")))
//                .andExpect(jsonPath("$[1].user_name", is("max")))
//                .andExpect(jsonPath("$[1].user_age", is(22)))
//                .andExpect(jsonPath("$[1].user_gender", is("male")))
//                .andExpect(jsonPath("$[1].user_email", is("max@twu.com")))
//                .andExpect(jsonPath("$[1].user_phone", is("10123456789")));
//
//    }
//
//    @Test
//    public void should_modify_a_rs_eventName_or_keyword() throws Exception {
//        RsEvent onlyModifyEventName = new RsEvent("只有风暴才能击倒大树", "", null);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json1 = objectMapper.writeValueAsString(onlyModifyEventName);
//
//        mockMvc.perform(put("/rs/event/1")
//                .content(json1)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/rs/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("只有风暴才能击倒大树")))
//                .andExpect(jsonPath("$.keyword", is("无分类")));
//
//        RsEvent onlyModifyKeyword = new RsEvent("", "科学类", null);
//        String json2 = objectMapper.writeValueAsString(onlyModifyKeyword);
//
//        mockMvc.perform(put("/rs/event/2")
//                .content(json2)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/rs/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第二条事件")))
//                .andExpect(jsonPath("$.keyword", is("科学类")));
//
//        RsEvent ModifyBothEventNameAndKeyword = new RsEvent("现在是大老爹拿球", "篮球类", null);
//        String json3 = objectMapper.writeValueAsString(ModifyBothEventNameAndKeyword);
//
//        mockMvc.perform(put("/rs/event/3")
//                .content(json3)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/rs/3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("现在是大老爹拿球")))
//                .andExpect(jsonPath("$.keyword", is("篮球类")));
//    }
//
//    @Test
//    public void should_delete_rs_event_given_number() throws Exception {
//        mockMvc.perform(delete("/rs/event/del/2"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyword", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[1].keyword", is("无分类")));
//    }

}

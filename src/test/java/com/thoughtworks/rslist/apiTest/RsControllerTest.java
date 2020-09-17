package com.thoughtworks.rslist.apiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    public void should_return_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));

        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));

        mockMvc.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));
    }

    @Test
    public void should_return_a_list_of_rs_events_by_range() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")));
    }

    @Test
    public void should_not_add_a_rs_when_eventName_is_null() throws Exception {
        UserDto user1 = new UserDto("haha", 20, "male", "aaa@123.com", "10123456789");
        RsEvent rsEvent = new RsEvent("", "经济", user1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_a_rs_when_keyword_is_null() throws Exception {
        UserDto user1 = new UserDto("haha", 20, "male", "aaa@123.com", "10123456789");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "", user1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_a_rs_when_user_name_not_valid() throws Exception {
        UserDto user = new UserDto("", 24, "male", "aaa@123.com", "10123456789");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_a_rs_when_user_age_below_valid() throws Exception {
        UserDto user1 = new UserDto("haha", 17, "male", "aaa@123.com", "10123456789");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_a_rs_when_user_age_above_valid() throws Exception {
        UserDto user1 = new UserDto("haha", 101, "male", "aaa@123.com", "10123456789");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_a_rs_when_user_gender_is_null() throws Exception {
        UserDto user1 = new UserDto("haha", 27, "", "aaa@123.com", "10123456789");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_a_rs_when_user_mail_not_valid() throws Exception {
        UserDto user1 = new UserDto("haha", 20, "male", "aaa@", "10123456789");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_a_rs_when_user_phone_number_len_less_than_11() throws Exception {
        UserDto user1 = new UserDto("haha", 20, "male", "aaa@123.com", "1012345678");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_add_a_rs_when_user_phone_number_len_more_than_11() throws Exception {
        UserDto user1 = new UserDto("haha", 20, "male", "aaa@123.com", "101234567899");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_only_add_a_rs_event_when_username_is_in_user_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[0].user.name", is("admin")))
                .andExpect(jsonPath("$[0].user.age", is(99)))
                .andExpect(jsonPath("$[0].user.gender", is("male")))
                .andExpect(jsonPath("$[0].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[0].user.phone", is("18888888888")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].user.name", is("admin")))
                .andExpect(jsonPath("$[1].user.age", is(99)))
                .andExpect(jsonPath("$[1].user.gender", is("male")))
                .andExpect(jsonPath("$[1].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[1].user.phone", is("18888888888")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].user.name", is("admin")))
                .andExpect(jsonPath("$[2].user.age", is(99)))
                .andExpect(jsonPath("$[2].user.gender", is("male")))
                .andExpect(jsonPath("$[2].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[2].user.phone", is("18888888888")));

        UserDto user = new UserDto("admin", 24, "male", "aaa@123.com", "10123456789");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[0].user.name", is("admin")))
                .andExpect(jsonPath("$[0].user.age", is(99)))
                .andExpect(jsonPath("$[0].user.gender", is("male")))
                .andExpect(jsonPath("$[0].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[0].user.phone", is("18888888888")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].user.name", is("admin")))
                .andExpect(jsonPath("$[1].user.age", is(99)))
                .andExpect(jsonPath("$[1].user.gender", is("male")))
                .andExpect(jsonPath("$[1].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[1].user.phone", is("18888888888")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].user.name", is("admin")))
                .andExpect(jsonPath("$[2].user.age", is(99)))
                .andExpect(jsonPath("$[2].user.gender", is("male")))
                .andExpect(jsonPath("$[2].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[2].user.phone", is("18888888888")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyword", is("经济")));


        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("admin")))
                .andExpect(jsonPath("$[0].age", is(99)))
                .andExpect(jsonPath("$[0].gender", is("male")))
                .andExpect(jsonPath("$[0].email", is("admin@twu.com")))
                .andExpect(jsonPath("$[0].phone", is("18888888888")));

    }

    @Test
    public void should_add_both_rs_event_and_user_when_user_name_not_in_user_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[0].user.name", is("admin")))
                .andExpect(jsonPath("$[0].user.age", is(99)))
                .andExpect(jsonPath("$[0].user.gender", is("male")))
                .andExpect(jsonPath("$[0].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[0].user.phone", is("18888888888")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].user.name", is("admin")))
                .andExpect(jsonPath("$[1].user.age", is(99)))
                .andExpect(jsonPath("$[1].user.gender", is("male")))
                .andExpect(jsonPath("$[1].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[1].user.phone", is("18888888888")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].user.name", is("admin")))
                .andExpect(jsonPath("$[2].user.age", is(99)))
                .andExpect(jsonPath("$[2].user.gender", is("male")))
                .andExpect(jsonPath("$[2].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[2].user.phone", is("18888888888")));

        UserDto user = new UserDto("max", 22, "male", "max@twu.com", "10123456789");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index", "4"));

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[0].user.name", is("admin")))
                .andExpect(jsonPath("$[0].user.age", is(99)))
                .andExpect(jsonPath("$[0].user.gender", is("male")))
                .andExpect(jsonPath("$[0].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[0].user.phone", is("18888888888")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].user.name", is("admin")))
                .andExpect(jsonPath("$[1].user.age", is(99)))
                .andExpect(jsonPath("$[1].user.gender", is("male")))
                .andExpect(jsonPath("$[1].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[1].user.phone", is("18888888888")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].user.name", is("admin")))
                .andExpect(jsonPath("$[2].user.age", is(99)))
                .andExpect(jsonPath("$[2].user.gender", is("male")))
                .andExpect(jsonPath("$[2].user.email", is("admin@twu.com")))
                .andExpect(jsonPath("$[2].user.phone", is("18888888888")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyword", is("经济")));


        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("admin")))
                .andExpect(jsonPath("$[0].age", is(99)))
                .andExpect(jsonPath("$[0].gender", is("male")))
                .andExpect(jsonPath("$[0].email", is("admin@twu.com")))
                .andExpect(jsonPath("$[0].phone", is("18888888888")))
                .andExpect(jsonPath("$[1].name", is("max")))
                .andExpect(jsonPath("$[1].age", is(22)))
                .andExpect(jsonPath("$[1].gender", is("male")))
                .andExpect(jsonPath("$[1].email", is("max@twu.com")))
                .andExpect(jsonPath("$[1].phone", is("10123456789")));

    }

    @Test
    public void should_modify_a_rs_eventName_or_keyword() throws Exception {
        RsEvent onlyModifyEventName = new RsEvent("只有风暴才能击倒大树", "", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String json1 = objectMapper.writeValueAsString(onlyModifyEventName);

        mockMvc.perform(put("/rs/event/1")
                .content(json1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("只有风暴才能击倒大树")))
                .andExpect(jsonPath("$.keyword", is("无分类")));

        RsEvent onlyModifyKeyword = new RsEvent("", "科学类", null);
        String json2 = objectMapper.writeValueAsString(onlyModifyKeyword);

        mockMvc.perform(put("/rs/event/2")
                .content(json2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("科学类")));

        RsEvent ModifyBothEventNameAndKeyword = new RsEvent("现在是大老爹拿球", "篮球类", null);
        String json3 = objectMapper.writeValueAsString(ModifyBothEventNameAndKeyword);

        mockMvc.perform(put("/rs/event/3")
                .content(json3)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("现在是大老爹拿球")))
                .andExpect(jsonPath("$.keyword", is("篮球类")));
    }

    @Test
    public void should_delete_rs_event_given_number() throws Exception {
        mockMvc.perform(put("/rs/event/del/2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")));
    }

}

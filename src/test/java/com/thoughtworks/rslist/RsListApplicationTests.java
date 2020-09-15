package com.thoughtworks.rslist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void should_return_one_rs_event() throws Exception {
        mockMvc.perform(get("/list/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("第一条事件"));

        mockMvc.perform(get("/list/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("第二条事件"));

        mockMvc.perform(get("list/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("第三条事件"));
    }

}

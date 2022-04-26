package com.shop.application.integrationTestsWithMock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.application.bussineslogic.CustomerSessionService;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerSessionIntegrationTests {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            APPLICATION_JSON.getType(),
            APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8
    );

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerSessionService service;

    private MockMvc mockMvc;
    private ObjectMapper om;
    private String test_session_id = "a1b2c3d4e5f6g7h8";

    @Autowired
    private void setupOM() {
        om = new ObjectMapper();
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testRegisterNewSession() throws Exception{
        System.out.println("-----------------------------------");
        System.out.println("Session id: " + test_session_id);
        System.out.println("-----------------------------------");
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/sessions/register/new/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(test_session_id)
        )
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(1, service.getCurrent_page().get(test_session_id));
    }

    @After
    @Test
    public void testGetSessionIDOfRegisteredSession() throws Exception {
        this.service.getCurrent_page().put(test_session_id, 1);
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/sessions/request/page/" + test_session_id))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

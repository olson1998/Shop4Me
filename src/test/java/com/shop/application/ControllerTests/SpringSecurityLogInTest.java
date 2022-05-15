package com.shop.application.ControllerTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shop.application.entities.userdb.LoginDetails;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2

@SpringBootTest
public class SpringSecurityLogInTest extends MockConfigSetup{

    private final LoginDetails existing_user = new LoginDetails(
            2,
            "olson",
            "pass"
    );

    private final LoginDetails non_existing_user = new LoginDetails(
            0,
            "NOT_EXIST",
            "NOT_EXIST"
    );

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testLogInWithExistingUserAndObtainAccessAndRefreshTokens_expecting200() {
        String not_expected_response = "wrong logging credentials... access forbidden";
        String response = "";
        try{
            response = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", existing_user.getUsername())
                    .param("password", existing_user.getPassword())
            ).andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

        }catch (Exception e){
            log.error("Something went wrong..., Exception: " + e);
        }
        log.info("Got response : '" + response + "'");
        assertNotEquals(not_expected_response, response);
    }

    @Test
    public void testLogInWithNonExistingUserAndObtainAccessAndRefreshTokens_expecting200() {
        String expected_response = "wrong logging credentials... access forbidden";
        String response = "";
        try{
            response = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", non_existing_user.getUsername())
                    .param("password", non_existing_user.getPassword())
            ).andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

        }catch (JsonProcessingException e){
            log.error("Couldn't map the object, Exception:" + e);
        }catch (Exception e){
            log.error("Something went wrong..., Exception: " + e);
        }
        log.info("Got response : '" + response + "'");
        assertEquals(expected_response, response);
    }
}

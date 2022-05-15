package com.shop.application.ControllerTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.application.entities.auctionsdb.Customer;
import com.shop.application.entities.userdb.LoginDetails;
import com.shop.application.repositories.userdbdao.LoginDetailsRepo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2

@SpringBootTest
public class PrivateCustomerControllerTest extends MockConfigSetup{

    @Autowired
    private LoginDetailsRepo loginDetailsRepo;

    private final Customer test_customer = new Customer(
            0,
            true,
            "test@test.pl",
            "048510720780",
            "sir",
            "Tester",
            "Testerski",
            "Testers HQ",
            "Test st",
            1,
            null,
            "Testers City",
            "Testers County",
            "Republic of Testers",
            "T01 001"
    );

    private final LoginDetails test_customer_login_details = new LoginDetails(
            0,
            "tester",
            "pass"
    );

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("save new customer")
    public void performSaveNewCustomer_expect_200(){
        String response = null;
        log.info("login: " + test_customer_login_details.getUsername());
        log.info("Pass: " + test_customer_login_details.getPassword());
        try{
            //login details
            response =  this.mockMvc.perform(MockMvcRequestBuilders.post("/customer/save/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(test_customer_login_details))
            ).andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertEquals("{\"customerID\":" + loginDetailsRepo.findLastID() + "}", response);

            assertNotEquals("User was not saved because there is no user with username: '" +
                    test_customer_login_details.getUsername() + "'",
                    response
            );
            //personal data
            response =  this.mockMvc.perform(MockMvcRequestBuilders.post("/customer/save/personaldata?username=" +
                    test_customer_login_details.getUsername())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(test_customer))
            ).andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertEquals("{\"message\":\"Saved new customer personal data for user: '"+
                    test_customer_login_details.getUsername()+
                    "'\"}",
                    response
            );
            //login
            this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", test_customer_login_details.getUsername())
                    .param("password", test_customer_login_details.getPassword())
            ).andExpect(status().isOk());

        }catch (JsonProcessingException e){
            log.error("Couldn't map the object...");
        }catch (Exception e){
            log.error(e);
        }
        assertNotNull(response);
    }

    @Test
    public void performGetUserPersonalData_expect_200(){
        String mapped_test_customer = "";
        String response = null;
        test_customer.setCustomerID(
                loginDetailsRepo.getCustomerIDbyUsername(
                        test_customer_login_details.getUsername()
                )
        );
        try{
            mapped_test_customer = new ObjectMapper().writeValueAsString(test_customer);
        }catch (JsonProcessingException e){
            log.error(e);
        }
        try{
            //auth
            response = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", test_customer_login_details.getUsername())
                    .param("password", test_customer_login_details.getPassword())
            ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

            HashMap<String, String> tokens = new ObjectMapper().readValue(response, HashMap.class);
            //get personal data
            response = this.mockMvc.perform(MockMvcRequestBuilders.get("/customer/private/personaldata")
                    .header("Authorization", "Auth "+tokens.get("access_token"))
            ).andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            log.info("Got response: " + response);
        }catch (Exception e){
            log.error(e);
        }
        assertEquals(mapped_test_customer, response);
    }



}


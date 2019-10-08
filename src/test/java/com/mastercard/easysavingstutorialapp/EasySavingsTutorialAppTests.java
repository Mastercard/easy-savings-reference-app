package com.mastercard.easysavingstutorialapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@PropertySource("application.properties")

public class EasySavingsTutorialAppTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testLoyaltyOffer() throws Exception {
        mvc.perform(post("/loyaltyRequest")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.flash().attribute("loyaltySuccess", "Offer retrieved successfully!"));
    }

    @Test
    public void testLocations() throws Exception {
        mvc.perform(get("/locationsRequest")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.flash().attribute("success", "Locations retrieved successfully!"));
    }

}

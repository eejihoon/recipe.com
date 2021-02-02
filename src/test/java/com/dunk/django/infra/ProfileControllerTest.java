package com.dunk.django.infra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired TestRestTemplate testRestTemplate;

    @Test
    void testRequestProfileWithAnonymous () {
        String expected = "local";

        ResponseEntity<String> response = testRestTemplate.getForEntity("/profile", String.class);
        
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), expected);
    }

}

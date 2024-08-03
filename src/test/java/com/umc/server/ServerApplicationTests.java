package com.umc.server;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.umc.server.util.JwtAuthFilter;
import com.umc.server.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@ComponentScan(basePackages = "com.umc.server")
class ServerApplicationTests {

    @Autowired private JwtTokenUtil jwtTokenUtil;

    @Autowired private JwtAuthFilter jwtAuthFilter;

    @Test
    void contextLoads() {
        assertNotNull(jwtTokenUtil, "JwtTokenUtil should not be null");
        assertNotNull(jwtAuthFilter, "JwtAuthFilter should not be null");
    }
}

package com.crpf.forms;

import com.crpf.forms.dto.AuthRequest;
import com.crpf.forms.entity.User;
import com.crpf.forms.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        User admin = userRepository.findByUsername("admin").orElseThrow();
        admin.setPassword(passwordEncoder.encode("admin123"));
        userRepository.save(admin);

        User user = userRepository.findByUsername("user").orElseThrow();
        user.setPassword(passwordEncoder.encode("user123"));
        userRepository.save(user);
    }

    @Test
    void testUnauthenticatedRequestIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/forms"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
    }

    @Test
    void testAuthenticationFlow() throws Exception {
        // 1. Authenticate as Admin user seeded in Flyway V5
        AuthRequest loginAdmin = AuthRequest.builder()
                .username("admin")
                .password("admin123")
                .build();

        MvcResult resultAdmin = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginAdmin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.roles").value(org.hamcrest.Matchers.hasItem("ROLE_ADMIN")))
                .andReturn();

        String responseStrAdmin = resultAdmin.getResponse().getContentAsString();
        String adminToken = objectMapper.readTree(responseStrAdmin).get("token").asText();

        // 2. Fetch forms using Admin token -> Success (VIEW permission)
        mockMvc.perform(get("/api/forms")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        // 3. Authenticate as standard User seeded in Flyway V5
        AuthRequest loginUser = AuthRequest.builder()
                .username("user")
                .password("user123")
                .build();

        MvcResult resultUser = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.roles").value(org.hamcrest.Matchers.hasItem("ROLE_USER")))
                .andReturn();

        String responseStrUser = resultUser.getResponse().getContentAsString();
        String userToken = objectMapper.readTree(responseStrUser).get("token").asText();

        // 4. Attempt to delete an education record with User token -> Forbidden (requires DELETE permission / ROLE_ADMIN)
        mockMvc.perform(delete("/api/education/1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        // 5. Attempt to delete with Admin token -> Should result in Not Found or Success instead of Forbidden
        mockMvc.perform(delete("/api/education/999") // ID that does not exist
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound()); // Confirms Admin reached the logic (meaning bypassed Forbidden)
    }
}

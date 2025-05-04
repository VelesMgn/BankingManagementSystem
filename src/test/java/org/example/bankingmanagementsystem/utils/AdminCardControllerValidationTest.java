package org.example.bankingmanagementsystem.utils;

import org.example.bankingmanagementsystem.config.ValidationConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class AdminCardControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllCards_ShouldValidateNegativePage() throws Exception {
        mockMvc.perform(get("/api/cards/admin/get-all")
                        .param("page", "-1")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value(ValidationConstants.PAGE_MIN_MESSAGE));
    }

    @Test
    void getAllCards_ShouldValidateZeroSize() throws Exception {
        mockMvc.perform(get("/api/cards/admin/get-all")
                        .param("page", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value(ValidationConstants.SIZE_MIN_MESSAGE));
    }

    @Test
    void createCard_ShouldValidateUserIdPath() throws Exception {
        mockMvc.perform(post("/api/cards/admin/create/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value(ValidationConstants.ID_POSITIVE_MESSAGE));
    }
}

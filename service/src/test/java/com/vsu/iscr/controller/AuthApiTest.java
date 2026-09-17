package com.vsu.iscr.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 认证与预约（软取消）自动化测试。
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthApiTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldRegisterNewUser() throws Exception {
        String body = "{\"userName\":\"apitester\",\"email\":\"apitester@test.com\",\"password\":\"secret123\",\"phone\":\"13911112222\"}";
        mockMvc.perform(post("/api/auth/register").contentType("application/json").content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    void shouldRejectDuplicateUsername() throws Exception {
        String body = "{\"userName\":\"admin\",\"email\":\"x@test.com\",\"password\":\"secret123\",\"phone\":\"13911112222\"}";
        mockMvc.perform(post("/api/auth/register").contentType("application/json").content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("用户名已存在"));
    }

    @Test
    void shouldRejectInvalidEmail() throws Exception {
        String body = "{\"userName\":\"emailtest\",\"email\":\"bad\",\"password\":\"secret123\",\"phone\":\"13911112222\"}";
        mockMvc.perform(post("/api/auth/register").contentType("application/json").content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("邮箱格式不正确"));
    }

    @Test
    void shouldRejectInvalidPhone() throws Exception {
        String body = "{\"userName\":\"phonetest\",\"email\":\"phonetest@test.com\",\"password\":\"secret123\",\"phone\":\"123\"}";
        mockMvc.perform(post("/api/auth/register").contentType("application/json").content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("手机号格式不正确"));
    }

    @Test
    void shouldLoginAdmin() throws Exception {
        String body = "{\"userName\":\"admin\",\"password\":\"admin123\"}";
        mockMvc.perform(post("/api/auth/login").contentType("application/json").content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.role").value(1));
    }

    @Test
    void shouldRejectWrongPassword() throws Exception {
        String body = "{\"userName\":\"admin\",\"password\":\"wrong\"}";
        mockMvc.perform(post("/api/auth/login").contentType("application/json").content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("密码错误"));
    }

    @Test
    void shouldSoftCancelReservation() throws Exception {
        String createBody = "{\"userId\":2,\"carId\":2,\"startDate\":\"2030-01-01\",\"endDate\":\"2030-01-02\"}";
        String content = mockMvc.perform(post("/api/reservations").contentType("application/json").content(createBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn().getResponse().getContentAsString();

        Integer reservationId = JsonPath.read(content, "$.data.reservationId");

        mockMvc.perform(delete("/api/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        // 软取消：记录应仍在，状态为 2（已取消）
        mockMvc.perform(get("/api/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.status").value(2));
    }
}

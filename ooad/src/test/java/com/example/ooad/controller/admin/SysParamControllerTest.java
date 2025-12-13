package com.example.ooad.controller.admin;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.ooad.service.sysparam.interfaces.SysParamService;
import com.example.ooad.service.auth.interfaces.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import java.util.Objects;

@WebMvcTest(SysParamController.class)
@Import(SysParamControllerTest.TestConfig.class)
public class SysParamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SysParamService sysParamService;

    @Test
    @WithMockUser
    void getParamValueByCode_returnsValue() throws Exception {
        when(sysParamService.getParamValueByCode("EXAM_FEE")).thenReturn("100000");

        mockMvc.perform(get("/admin/sys-params/value/EXAM_FEE"))
                .andExpect(status().isOk())
                .andExpect(content().string("100000"));
    }

    @Test
    @WithMockUser
    void getParamValueByCode_notFound_returns404() throws Exception {
        when(sysParamService.getParamValueByCode("MISSING"))
                .thenThrow(new com.example.ooad.exception.NotFoundException("not found"));

        mockMvc.perform(get("/admin/sys-params/value/MISSING"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getParamValueByCode_unauthenticated_returns401() throws Exception {
        // no @WithMockUser -> should be unauthorized
        mockMvc.perform(get("/admin/sys-params/value/EXAM_FEE"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void createSysParam_badRequest_returns400() throws Exception {
        Mockito.reset(sysParamService);
        when(sysParamService.createSysParam(any(), any()))
                .thenThrow(new com.example.ooad.exception.BadRequestException("bad request"));

        String body = "{\"paramCode\":\"X\",\"paramName\":\"X name\",\"groupId\":1,\"active\":true}";
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/sys-params")
                .with(Objects.requireNonNull(SecurityMockMvcRequestPostProcessors.csrf()))
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void createSysParam_conflict_returns409() throws Exception {
        Mockito.reset(sysParamService);
        when(sysParamService.createSysParam(any(), any()))
                .thenThrow(new com.example.ooad.exception.ConflictException("conflict"));

        String body = "{\"paramCode\":\"X\",\"paramName\":\"X name\",\"groupId\":1,\"active\":true}";
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/sys-params")
                .with(Objects.requireNonNull(SecurityMockMvcRequestPostProcessors.csrf()))
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void deleteSysParam_notFound_returns404() throws Exception {
        Mockito.reset(sysParamService);
        doThrow(new com.example.ooad.exception.NotFoundException("not found")).when(sysParamService)
                .deleteSysParam(999);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/admin/sys-params/999")
                        .with(Objects.requireNonNull(SecurityMockMvcRequestPostProcessors.csrf())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateSysParam_notFound_returns404() throws Exception {
        Mockito.reset(sysParamService);
        when(sysParamService.updateSysParam(eq(123), any(), any()))
                .thenThrow(new com.example.ooad.exception.NotFoundException("not found"));

        String body = "{\"paramCode\":\"X\",\"paramName\":\"X name\",\"groupId\":1,\"active\":true}";
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/sys-params/123")
                .with(Objects.requireNonNull(SecurityMockMvcRequestPostProcessors.csrf()))
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(body))
                .andExpect(status().isNotFound());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SysParamService sysParamService() {
            return Mockito.mock(SysParamService.class);
        }

        @Bean
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }
    }
}

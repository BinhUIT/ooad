package com.example.ooad.controller.refdiseasetype;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ooad.dto.response.RefDiseaseTypeResponse;
import com.example.ooad.service.refdiseasetype.interfaces.RefDiseaseTypeService;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
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
import java.util.List;
import com.example.ooad.service.auth.interfaces.JwtService;
import org.springframework.data.domain.Page;

@WebMvcTest(RefDiseaseTypeController.class)
@Import(RefDiseaseTypeControllerTest.TestConfig.class)
public class RefDiseaseTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RefDiseaseTypeService refDiseaseTypeService;

    @Test
    @WithMockUser
    void getDiseaseTypeById_returns200() throws Exception {
        RefDiseaseTypeResponse r = new RefDiseaseTypeResponse(1, "C1", "Name", "desc", false, false, true);
        when(refDiseaseTypeService.getDiseaseTypeById(1)).thenReturn(r);

        mockMvc.perform(get("/admin/disease-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.diseaseCode").value("C1"));
    }

    @Test
    @WithMockUser
    void getDiseaseTypeById_notFound_returns404() throws Exception {
        when(refDiseaseTypeService.getDiseaseTypeById(9)).thenThrow(new NotFoundException("not found"));

        mockMvc.perform(get("/admin/disease-types/9")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createDiseaseType_success_returns201() throws Exception {
        String body = "{\"diseaseCode\":\"C2\",\"diseaseName\":\"Name2\"}";
        RefDiseaseTypeResponse r = new RefDiseaseTypeResponse(2, "C2", "Name2", null, false, false, true);
        when(refDiseaseTypeService.createDiseaseType(any(), any())).thenReturn(r);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/disease-types")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.diseaseTypeId").value(2));
    }

    @Test
    @WithMockUser
    void createDiseaseType_badRequest_returns400() throws Exception {
        String body = "{\"diseaseCode\":\"\"}";
        when(refDiseaseTypeService.createDiseaseType(any(), any())).thenThrow(new BadRequestException("bad"));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/disease-types")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void updateDiseaseType_conflict_returns409() throws Exception {
        String body = "{\"diseaseCode\":\"X\"}";
        when(refDiseaseTypeService.updateDiseaseType(eq(5), any(), any())).thenThrow(new ConflictException("conflict"));

        mockMvc.perform(MockMvcRequestBuilders.put("/admin/disease-types/5")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void deleteDiseaseType_notFound_returns404() throws Exception {
        doThrow(new NotFoundException("not found")).when(refDiseaseTypeService).deleteDiseaseType(99);

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/disease-types/99")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getAll_and_active_and_search_return200() throws Exception {
        when(refDiseaseTypeService.getAllDiseaseTypes()).thenReturn(List.of());
        when(refDiseaseTypeService.getAllActiveDiseaseTypes()).thenReturn(List.of());
        when(refDiseaseTypeService.searchDiseaseTypes(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/admin/disease-types/all")).andExpect(status().isOk());
        mockMvc.perform(get("/admin/disease-types/active")).andExpect(status().isOk());
        mockMvc.perform(get("/admin/disease-types")).andExpect(status().isOk());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RefDiseaseTypeService refDiseaseTypeService() {
            return Mockito.mock(RefDiseaseTypeService.class);
        }

        @Bean
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }
    }
}

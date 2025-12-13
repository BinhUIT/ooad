package com.example.ooad.service.refdiseasetype;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.ooad.domain.entity.RefDiseaseType;
import com.example.ooad.dto.request.RefDiseaseTypeRequest;
import com.example.ooad.dto.request.RefDiseaseTypeFilterRequest;
import com.example.ooad.dto.response.RefDiseaseTypeResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.RefDiseaseTypeRepository;
import com.example.ooad.service.refdiseasetype.implementation.RefDiseaseTypeServiceImplementation;

@ExtendWith(MockitoExtension.class)
public class RefDiseaseTypeServiceImplementationTest {

    @Mock
    private RefDiseaseTypeRepository repo;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RefDiseaseTypeServiceImplementation service;

    private RefDiseaseType makeEntity(int id, String code, String name, boolean active) {
        RefDiseaseType e = new RefDiseaseType();
        e.setDiseaseTypeId(id);
        e.setDiseaseCode(code);
        e.setDiseaseName(name);
        e.setActive(active);
        return e;
    }

    private RefDiseaseTypeRequest makeRequest(String code, String name) {
        RefDiseaseTypeRequest r = new RefDiseaseTypeRequest();
        r.setDiseaseCode(code);
        r.setDiseaseName(name);
        return r;
    }

    @Test
    public void createDiseaseType_success() {
        RefDiseaseTypeRequest req = makeRequest("C001", "Test disease");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(repo.existsByDiseaseCode("C001")).thenReturn(false);

        RefDiseaseType saved = makeEntity(10, "C001", "Test disease", true);
        when(repo.save(any(RefDiseaseType.class))).thenReturn(saved);

        RefDiseaseTypeResponse resp = service.createDiseaseType(req, bindingResult);

        assertEquals(10, resp.getDiseaseTypeId());
        assertEquals("C001", resp.getDiseaseCode());
        assertEquals("Test disease", resp.getDiseaseName());
    }

    @Test
    public void createDiseaseType_badRequest_throws() {
        RefDiseaseTypeRequest req = makeRequest("C002", "x");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(new ObjectError("field", "bad")));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> service.createDiseaseType(req, bindingResult));

        assertTrue(ex.getMessage().contains("bad"));
    }

    @Test
    public void createDiseaseType_conflict_throws() {
        RefDiseaseTypeRequest req = makeRequest("C003", "name");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(repo.existsByDiseaseCode("C003")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.createDiseaseType(req, bindingResult));
    }

    @Test
    public void getDiseaseTypeById_notFound_throws() {
        when(repo.findById(5)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getDiseaseTypeById(5));
    }

    @Test
    public void getDiseaseTypeById_success() {
        RefDiseaseType e = makeEntity(7, "X7", "name7", true);
        when(repo.findById(7)).thenReturn(Optional.of(e));

        RefDiseaseTypeResponse resp = service.getDiseaseTypeById(7);

        assertEquals(7, resp.getDiseaseTypeId());
        assertEquals("X7", resp.getDiseaseCode());
    }

    @Test
    public void deleteDiseaseType_notFound_throws() {
        when(repo.existsById(42)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deleteDiseaseType(42));
    }

    @Test
    public void deleteDiseaseType_success_callsDelete() {
        when(repo.existsById(3)).thenReturn(true);

        service.deleteDiseaseType(3);

        verify(repo).deleteById(3);
    }

    @Test
    public void updateDiseaseType_conflictWhenChangeCode_throws() {
        RefDiseaseType existing = makeEntity(11, "OLD", "oldname", true);
        RefDiseaseTypeRequest req = makeRequest("NEW", "newname");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(repo.findById(11)).thenReturn(Optional.of(existing));
        when(repo.existsByDiseaseCode("NEW")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.updateDiseaseType(11, req, bindingResult));
    }

    @Test
    public void updateDiseaseType_success() {
        RefDiseaseType existing = makeEntity(12, "CODE12", "n12", true);
        RefDiseaseTypeRequest req = makeRequest("CODE12", "updated name");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(repo.findById(12)).thenReturn(Optional.of(existing));
        when(repo.save(any(RefDiseaseType.class))).thenAnswer(inv -> inv.getArgument(0));

        RefDiseaseTypeResponse resp = service.updateDiseaseType(12, req, bindingResult);

        assertEquals(12, resp.getDiseaseTypeId());
        assertEquals("CODE12", resp.getDiseaseCode());
        assertEquals("updated name", resp.getDiseaseName());
    }

    @Test
    public void getAllDiseaseTypes_and_getAllActiveDiseaseTypes() {
        RefDiseaseType a = makeEntity(1, "A", "a", true);
        RefDiseaseType b = makeEntity(2, "B", "b", false);
        when(repo.findAll()).thenReturn(Arrays.asList(a, b));

        List<RefDiseaseTypeResponse> all = service.getAllDiseaseTypes();
        assertEquals(2, all.size());

        List<RefDiseaseTypeResponse> active = service.getAllActiveDiseaseTypes();
        assertEquals(1, active.size());
        assertEquals("A", active.get(0).getDiseaseCode());
    }

    @Test
    public void searchDiseaseTypes_delegatesToRepository() {
        RefDiseaseTypeFilterRequest filter = new RefDiseaseTypeFilterRequest();
        filter.setPage(0);
        filter.setSize(5);

        when(repo.findAll((Specification<RefDiseaseType>) any(), (Pageable) any())).thenReturn(Page.empty());

        assertNotNull(service.searchDiseaseTypes(filter));
    }
}

package com.example.ooad.controller.staff;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.enums.ERole;
import com.example.ooad.dto.request.StaffFilterRequest;
import com.example.ooad.dto.request.StaffRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.StaffResponse;
import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/staff")
@Tag(name = "Staff Management")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<StaffResponse>> createStaff(
            @RequestBody @Valid StaffRequest request,
            BindingResult bindingResult) {

        StaffResponse result = staffService.createStaff(request, bindingResult);
        GlobalResponse<StaffResponse> response = new GlobalResponse<>(result, Message.success, 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<StaffResponse>> updateStaff(
            @PathVariable int id,
            @RequestBody @Valid StaffRequest request,
            BindingResult bindingResult) {

        StaffResponse result = staffService.updateStaff(id, request, bindingResult);
        GlobalResponse<StaffResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<StaffResponse>> getStaffById(@PathVariable int id) {
        StaffResponse result = staffService.getStaffById(id);
        GlobalResponse<StaffResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<Page<StaffResponse>>> searchStaffs(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ERole role,
            @RequestParam(required = false) Boolean active) {

        StaffFilterRequest filter = new StaffFilterRequest();
        if (page != null)
            filter.setPage(page);
        if (size != null)
            filter.setSize(size);
        if (sortBy != null)
            filter.setSortBy(sortBy);
        if (sortType != null)
            filter.setSortType(sortType);
        if (search != null)
            filter.setSearch(search);
        if (role != null)
            filter.setRole(role);
        if (active != null)
            filter.setActive(active);

        Page<StaffResponse> result = staffService.searchStaffs(filter);
        GlobalResponse<Page<StaffResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteStaff(@PathVariable int id) {
        staffService.deleteStaff(id);
        GlobalResponse<Void> response = new GlobalResponse<>(null, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

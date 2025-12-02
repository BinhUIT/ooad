package com.example.ooad.controller.admin;

import com.example.ooad.dto.request.SysParamGroupRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.SysParamGroupResponse;
import com.example.ooad.service.sysparam.interfaces.SysParamGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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

@RestController
@RequestMapping("/admin/sys-param-groups")
@SecurityRequirement(name = "Bearer Auth")
@Tag(name = "System Parameter Groups")
public class SysParamGroupController {

    private final SysParamGroupService sysParamGroupService;

    public SysParamGroupController(SysParamGroupService sysParamGroupService) {
        this.sysParamGroupService = sysParamGroupService;
    }

    @Operation(description = "Create a new system parameter group", responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamGroupResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
            @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
    })
    @PostMapping
    public ResponseEntity<?> createSysParamGroup(
            @Valid @RequestBody SysParamGroupRequest request,
            BindingResult bindingResult) {
        SysParamGroupResponse response = sysParamGroupService.createSysParamGroup(
                request,
                bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(description = "Update an existing system parameter group", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamGroupResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSysParamGroup(
            @PathVariable int id,
            @Valid @RequestBody SysParamGroupRequest request,
            BindingResult bindingResult) {
        SysParamGroupResponse response = sysParamGroupService.updateSysParamGroup(
                id,
                request,
                bindingResult);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Get system parameter group by ID", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamGroupResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getSysParamGroupById(@PathVariable int id) {
        SysParamGroupResponse response = sysParamGroupService.getSysParamGroupById(
                id);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Get system parameter group by code", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamGroupResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
    })
    @GetMapping("/code/{groupCode}")
    public ResponseEntity<?> getSysParamGroupByCode(
            @PathVariable String groupCode) {
        SysParamGroupResponse response = sysParamGroupService.getSysParamGroupByCode(groupCode);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Get all system parameter groups", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamGroupResponse.class))),
    })
    @GetMapping
    public ResponseEntity<?> getAllSysParamGroups() {
        List<SysParamGroupResponse> response = sysParamGroupService.getAllSysParamGroups();
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Get all active system parameter groups", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamGroupResponse.class))),
    })
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveSysParamGroups() {
        List<SysParamGroupResponse> response = sysParamGroupService.getAllActiveSysParamGroups();
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Search system parameter groups with pagination", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchSysParamGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        var response = sysParamGroupService.searchSysParamGroups(
                page,
                size,
                keyword);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Delete system parameter group by ID", responses = {
            @ApiResponse(description = "Success", responseCode = "204"),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSysParamGroup(@PathVariable int id) {
        sysParamGroupService.deleteSysParamGroup(id);
        return ResponseEntity.noContent().build();
    }
}

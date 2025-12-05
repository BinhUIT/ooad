package com.example.ooad.controller.admin;

import com.example.ooad.dto.request.SysParamRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.SysParamResponse;
import com.example.ooad.service.sysparam.interfaces.SysParamService;
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
@RequestMapping("/admin/sys-params")
@SecurityRequirement(name = "Bearer Auth")
@Tag(name = "System Parameters")
public class SysParamController {

    private final SysParamService sysParamService;

    public SysParamController(SysParamService sysParamService) {
        this.sysParamService = sysParamService;
    }

    @Operation(description = "Create a new system parameter", responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
            @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
    })
    @PostMapping
    public ResponseEntity<?> createSysParam(
            @Valid @RequestBody SysParamRequest request,
            BindingResult bindingResult) {
        SysParamResponse response = sysParamService.createSysParam(
                request,
                bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(description = "Update an existing system parameter", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSysParam(
            @PathVariable int id,
            @Valid @RequestBody SysParamRequest request,
            BindingResult bindingResult) {
        SysParamResponse response = sysParamService.updateSysParam(
                id,
                request,
                bindingResult);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Get system parameter by ID", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getSysParamById(@PathVariable int id) {
        SysParamResponse response = sysParamService.getSysParamById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Get system parameter by code", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
    })
    @GetMapping("/code/{paramCode}")
    public ResponseEntity<?> getSysParamByCode(@PathVariable String paramCode) {
        SysParamResponse response = sysParamService.getSysParamByCode(paramCode);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Get all system parameters", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamResponse.class))),
    })
    @GetMapping
    public ResponseEntity<?> getAllSysParams() {
        List<SysParamResponse> response = sysParamService.getAllSysParams();
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Get all active system parameters", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamResponse.class))),
    })
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveSysParams() {
        List<SysParamResponse> response = sysParamService.getAllActiveSysParams();
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Get system parameters by group ID", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamResponse.class))),
    })
    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getSysParamsByGroupId(@PathVariable int groupId) {
        List<SysParamResponse> response = sysParamService.getSysParamsByGroupId(
                groupId);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Get system parameters by group code", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SysParamResponse.class))),
    })
    @GetMapping("/group/code/{groupCode}")
    public ResponseEntity<?> getSysParamsByGroupCode(
            @PathVariable String groupCode) {
        List<SysParamResponse> response = sysParamService.getSysParamsByGroupCode(
                groupCode);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Search system parameters with pagination and filters", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchSysParams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer groupId) {
        var response = sysParamService.searchSysParams(
                page,
                size,
                keyword,
                groupId);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Delete system parameter by ID", responses = {
            @ApiResponse(description = "Success", responseCode = "204"),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSysParam(@PathVariable int id) {
        sysParamService.deleteSysParam(id);
        return ResponseEntity.noContent().build();
    }
}

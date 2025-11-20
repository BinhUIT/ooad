package com.example.ooad.controller.patient;

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
import org.springframework.web.bind.annotation.RestController;


import com.example.ooad.dto.request.PatientRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.PatientResponse;
import com.example.ooad.service.patient.implementation.PatientServiceImplementation;
import com.example.ooad.service.patient.interfaces.PatientService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name="Patient")
public class PatientController {
    private final PatientService patientService;
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    @PostMapping({"/receptionist/create_patient"})
    @Operation(
        description="Create Schedule",
        responses={
            @ApiResponse(
                description="Bad Request",
                responseCode="400",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
            @ApiResponse
        }
    )
    public ResponseEntity<GlobalResponse<PatientResponse>> createPatient(@RequestBody @Valid PatientRequest request, BindingResult bindingResult) {
        PatientResponse result = patientService.createPatient(request, bindingResult);
        GlobalResponse<PatientResponse> response = new GlobalResponse<PatientResponse>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping({"/receptionist/get_all_patients","/doctor/get_all_patients"})
    public ResponseEntity<GlobalResponse<List<PatientResponse>>> getAllPatients() {
        List<PatientResponse> result = patientService.getAllPatients();
        GlobalResponse<List<PatientResponse>> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping({"/receptionist/get_patient_by_id/{patientId}","/doctor/get_patient_by_id/{patientId}"})
     @PostMapping({"/receptionist/create_patient"})
    @Operation(
        description="Create Schedule",
        responses={
            @ApiResponse(
                description="Not Found",
                responseCode="404",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
            @ApiResponse
        }
    )
    public ResponseEntity<GlobalResponse<PatientResponse>> getPatientById(@PathVariable int patientId) {
        PatientResponse result = patientService.getPatientById(patientId);
        GlobalResponse<PatientResponse> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping({"/receptionist/update_patient/{patientId}","/doctor/update_patient/{patientId}"})
    @Operation(
        description="Create Schedule",
        responses={
            @ApiResponse(
                description="Not found",
                responseCode="404",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
             @ApiResponse(
                description="Bad Request",
                responseCode="400",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
            @ApiResponse
        }
    )
    public ResponseEntity<GlobalResponse<PatientResponse>> updatePatient(@RequestBody @Valid PatientRequest request, BindingResult bindingResult, @PathVariable int patientId) {
        PatientResponse result = patientService.updatePatient(request, bindingResult, patientId);
        GlobalResponse<PatientResponse> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping({"/receptionist/delete_patient/{patientId}","/doctor/delete_patient/{patientId}"})
    @Operation(
        description="Create Schedule",
        responses={
            @ApiResponse(
                description="Not found",
                responseCode="404",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
            @ApiResponse
        }
    )
    public ResponseEntity<GlobalResponse<PatientResponse>> deletePatient(@PathVariable int patientId) {
        patientService.deletePatient(patientId);
        return new ResponseEntity<>(new GlobalResponse<>(null, Message.success,200), HttpStatus.OK);
    }

}

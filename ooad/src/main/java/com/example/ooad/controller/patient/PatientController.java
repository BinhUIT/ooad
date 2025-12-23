package com.example.ooad.controller.patient;

import java.util.List;

import com.example.ooad.domain.entity.Invoice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.dto.request.PatientRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.PatientResponse;
import com.example.ooad.dto.response.PatientTabsResponse;

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
    @PostMapping({"/receptionist/create_patient","/admin/create_patient"})
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
            @ApiResponse(
                description="Success",
                content = @Content(
                    mediaType="application/json"
                )
            )
        }
    )
    public ResponseEntity<GlobalResponse<PatientResponse>> createPatient(@RequestBody @Valid PatientRequest request, BindingResult bindingResult) {
        PatientResponse result = patientService.createPatient(request, bindingResult);
        GlobalResponse<PatientResponse> response = new GlobalResponse<PatientResponse>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping({"/receptionist/get_all_patients","/doctor/get_all_patients","/admin/get_all_patients"})
    public ResponseEntity<GlobalResponse<List<PatientResponse>>> getAllPatients() {
        List<PatientResponse> result = patientService.getAllPatients();
        GlobalResponse<List<PatientResponse>> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping({"/receptionist/get_patient_by_id/{patientId}","/doctor/get_patient_by_id/{patientId}","/admin/get_patient_by_id/{patientId}"})
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
            @ApiResponse(
                description="Success",
                content = @Content(
                    mediaType="application/json"
                )
            )
        }
    )
    public ResponseEntity<GlobalResponse<PatientResponse>> getPatientById(@PathVariable int patientId) {
        PatientResponse result = patientService.getPatientById(patientId);
        GlobalResponse<PatientResponse> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping({"/receptionist/update_patient/{patientId}","/admin/update_patient/{patientId}"})
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
            @ApiResponse(
                description="Success",
                content = @Content(
                    mediaType="application/json"
                )
            )
        }
    )
    public ResponseEntity<GlobalResponse<PatientResponse>> updatePatient(@RequestBody @Valid PatientRequest request, BindingResult bindingResult, @PathVariable int patientId) {
        PatientResponse result = patientService.updatePatient(request, bindingResult, patientId);
        GlobalResponse<PatientResponse> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping({"/receptionist/delete_patient/{patientId}","/admin/delete_patient/{patientId}"})
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
                description="Success",
                content = @Content(
                    mediaType="application/json"
                )
            )
        }
    )
    public ResponseEntity<GlobalResponse<PatientResponse>> deletePatient(@PathVariable int patientId) {
        patientService.deletePatient(patientId);
        return new ResponseEntity<>(new GlobalResponse<>(null, Message.success,200), HttpStatus.OK);
    }

    @GetMapping({"/receptionist/patient_tabs/{patientId}","/admin/patient_tabs/{patientId}","/doctor/patient_tabs/{patientId}"}) 
    public ResponseEntity<GlobalResponse<PatientTabsResponse>> getPatientTabs(@PathVariable int patientId) {
        List<Appointment> appointments = patientService.getAppointmentsOfPatient(patientId);
        List<MedicalRecord> medicalRecords = patientService.getMedicalRecordsOfPatient(patientId);
        List<Invoice> invoices= patientService.getInvoiceOfPatient(patientId);
        PatientTabsResponse result = new PatientTabsResponse(appointments, medicalRecords, invoices);
        GlobalResponse<PatientTabsResponse> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/auth")
    public ResponseEntity<GlobalResponse<PatientResponse>> getPatientFromAuth(Authentication auth) {
        PatientResponse result = patientService.getPatientResponseFromAuth(auth);
        GlobalResponse<PatientResponse> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/receptionist/find_patient")
    public ResponseEntity<GlobalResponse<PatientResponse>> findPatientByIdCard(@RequestParam(defaultValue = "") String idCard) {
        PatientResponse result = patientService.findPatientByIdCard(idCard);
        GlobalResponse<PatientResponse> response = new GlobalResponse<>(result,Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

package com.example.ooad.controller.reception;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.dto.request.CreateReceptionRequest;
import com.example.ooad.dto.request.UpdateReceptionRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.PatientResponse;
import com.example.ooad.service.account.interfaces.AccountService;
import com.example.ooad.service.patient.interfaces.PatientService;
import com.example.ooad.service.reception.interfaces.ReceptionService;
import com.example.ooad.utils.Message;

@RestController
public class ReceptionController {
    private final ReceptionService receptionService;
    private final AccountService accountService;
    private final PatientService patientService;
    public ReceptionController(ReceptionService receptionService, AccountService accountService, PatientService patientServive) {
        this.receptionService = receptionService;
        this.accountService = accountService;
        this.patientService = patientServive;
    }
    @GetMapping("/receptionist/all_receptions")
    public ResponseEntity<GlobalResponse<Page<Reception>>> getAllReceptions(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "7") int pageSize,
@RequestParam Optional<EReceptionStatus> status, @RequestParam Optional<Date> date) {
        Page<Reception> result = receptionService.getListReceptions(pageNumber, pageSize, status , date);
        GlobalResponse<Page<Reception>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/receptionist/reception/{id}")
    public ResponseEntity<GlobalResponse<Reception>> getReceptionById(@PathVariable int id) {
        Reception result = receptionService.getReceptionById(id);
        GlobalResponse<Reception> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } 
    @PutMapping({"/receptionist/reception/update","/doctor/reception/update"})
    public ResponseEntity<GlobalResponse<Reception>> updateReception(@RequestBody UpdateReceptionRequest request) {
        Reception result = receptionService.editReception(request);
        GlobalResponse<Reception> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/receptionist/reception/create")
    public ResponseEntity<GlobalResponse<Reception>> createReception(@RequestBody CreateReceptionRequest request, Authentication auth) {
        String accountName = auth.getName();
        Staff receptionist = accountService.getStaffIdFromAccountName(accountName);
        Reception result = receptionService.createReception(request, receptionist);
        GlobalResponse<Reception> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/receptionist/find_patient")
    public ResponseEntity<GlobalResponse<PatientResponse>> findPatientByIdCard(@RequestParam(defaultValue = "") String idCard) {
        PatientResponse result = patientService.findPatientByIdCard(idCard);
        GlobalResponse<PatientResponse> response = new GlobalResponse<>(result,Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

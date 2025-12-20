package com.example.ooad.controller.prescription;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Prescription;
import com.example.ooad.domain.entity.PrescriptionDetail;
import com.example.ooad.dto.request.PrescriptionRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.service.prescription.interfaces.PrescriptionService;
import com.example.ooad.utils.Message;

@RestController
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService= prescriptionService;
    }
    @GetMapping({"/unsecure/prescriptions","/doctor/prescriptions"}) 
    public ResponseEntity<GlobalResponse<Page<Prescription>>> getAllPrescription(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "7") int pageSize,
     @RequestParam Optional<Date> prescriptionDate, @RequestParam Optional<String> patientName) {
        Page<Prescription> result = prescriptionService.getAllPrescription(pageNumber, pageSize, prescriptionDate,patientName);
        GlobalResponse<Page<Prescription>> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping({"/unsecure/prescription/{prescriptionId}","/doctor/prescription/{prescriptionId}"})
    public ResponseEntity<GlobalResponse<Prescription>> getPrescriptionById(@PathVariable int prescriptionId) {
        Prescription result = prescriptionService.getPrescriptionById(prescriptionId);
        GlobalResponse<Prescription> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } 
    @GetMapping({"/unsecure/prescription_details/{prescriptionId}","/doctor/prescription_details/{prescriptionId}"})
    public ResponseEntity<GlobalResponse<List<PrescriptionDetail>>> getPrescriptionDetails(@PathVariable int prescriptionId) {
        List<PrescriptionDetail> result = prescriptionService.getPrescriptionDetailOfPrescription(prescriptionId);
        GlobalResponse<List<PrescriptionDetail>> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/doctor/prescription/create")
    public ResponseEntity<GlobalResponse<Prescription>> createPrescription(@RequestBody PrescriptionRequest request) {
         Prescription result = prescriptionService.createPrescription(request);
        GlobalResponse<Prescription> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/doctor/prescription/update/{prescriptionId}")
    public ResponseEntity<GlobalResponse<Prescription>> updatePrescription(@RequestBody PrescriptionRequest request, @PathVariable int prescriptionId) {
         Prescription result = prescriptionService.updatePrescription(request, prescriptionId);
        GlobalResponse<Prescription> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.example.ooad.controller.service;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Service;
import com.example.ooad.dto.request.ServiceRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.service.clinicservice.interfaces.ClinicService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/unsecure")
public class ServiceController {
    private final ClinicService clinicService;
    public ServiceController(ClinicService clinicService) {
        this.clinicService = clinicService;
    } 

    @GetMapping("/all_services")
    public ResponseEntity<GlobalResponse<Page<Service>>> getAllServices(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Page<Service> result = clinicService.findAllService(pageNumber, pageSize);
        GlobalResponse<Page<Service>> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<GlobalResponse<Service>> getServiceById(@PathVariable int serviceId) {
        Service result = clinicService.findServiceById(serviceId);
        GlobalResponse<Service> response = new GlobalResponse<>(result,Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } 

    @PostMapping("/service/create")
    public ResponseEntity<GlobalResponse<Service>> createService(@RequestBody ServiceRequest request) {
        Service result = clinicService.createService(request);
        GlobalResponse<Service> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } 
    @PutMapping("/service/update/{serviceId}")
    public ResponseEntity<GlobalResponse<Service>> updateService(@RequestBody ServiceRequest request, @PathVariable int serviceId) {
        Service result = clinicService.updateService(request, serviceId);
        GlobalResponse<Service> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/service/delete/{serviceId}")
    public ResponseEntity<GlobalResponse<String>> deleteService(@PathVariable int serviceId) {
        clinicService.deleteService(serviceId);
        GlobalResponse<String> response = new GlobalResponse<String>(Message.success,Message.success, 200);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}

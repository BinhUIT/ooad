package com.example.ooad.controller.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.example.ooad.domain.entity.Service;
import com.example.ooad.dto.request.ServiceRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.mapper.ServiceRequestMapper;
import com.example.ooad.service.clinicservice.interfaces.ClinicService;
import com.example.ooad.utils.Message;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ServiceController {
    private final ClinicService clinicService;
    private final RequestMappingHandlerAdapter adapter;
    public ServiceController(ClinicService clinicService, RequestMappingHandlerAdapter adapter) {
        this.clinicService = clinicService;
        this.adapter = adapter;
    } 

    @GetMapping("/unsecure/all_services")
    public ResponseEntity<GlobalResponse<Page<Service>>> getAllServices(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Page<Service> result = clinicService.findAllService(pageNumber, pageSize);
        GlobalResponse<Page<Service>> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/services/all")
    public ResponseEntity<GlobalResponse<java.util.List<Service>>> getAllServicesForAdmin() {
        java.util.List<Service> result = clinicService.findAllServices();
        GlobalResponse<java.util.List<Service>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/receptionist/services/all")
    public ResponseEntity<GlobalResponse<java.util.List<Service>>> getAllServicesForReceptionist() {
        java.util.List<Service> result = clinicService.findAllServices();
        GlobalResponse<java.util.List<Service>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/unsecure/test-body")
public ResponseEntity<String> testBody(HttpServletRequest request) throws IOException {
    System.out.println("Content-Type: " + request.getContentType());

    BufferedReader reader = request.getReader();
    String body = reader.lines().collect(Collectors.joining("\n"));
    System.out.println("RAW BODY: " + body);

    return ResponseEntity.ok("ok");
}
    @PutMapping("/unsecure/service/update-test")
public ResponseEntity<Map<String, Object>> test(@RequestBody HashMap<String,Object> body){
    System.out.println(body);
    return ResponseEntity.ok(body);
}

    @GetMapping("/unsecure/service/{serviceId}")
    public ResponseEntity<GlobalResponse<Service>> getServiceById(@PathVariable int serviceId) {
        Service result = clinicService.findServiceById(serviceId);
        GlobalResponse<Service> response = new GlobalResponse<>(result,Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } 

    @PostMapping("/unsecure/service/create")
    public ResponseEntity<GlobalResponse<Service>> createService(@RequestBody ServiceRequest request) {
        Service result = clinicService.createService(request);
        GlobalResponse<Service> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } 
    @PostMapping("/unsecure/service/update/{serviceId}")
    public ResponseEntity<GlobalResponse<Service>> updateService(@RequestBody ServiceRequest request, @PathVariable int serviceId) {
        
        System.out.println("Service name"+request.getServiceName());
        Service result = clinicService.updateService(request, serviceId);
        
        GlobalResponse<Service> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/unsecure/service/delete/{serviceId}")
    public ResponseEntity<GlobalResponse<String>> deleteService(@PathVariable int serviceId) {
        clinicService.deleteService(serviceId);
        GlobalResponse<String> response = new GlobalResponse<String>(Message.success,Message.success, 200);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/admin/service/search")
    public ResponseEntity<GlobalResponse<Page<Service>>> searchService(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam Optional<String> keyWord) {
        Page<Service> result = clinicService.searchService(pageNumber, pageSize,keyWord);
        GlobalResponse<Page<Service>> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    

}

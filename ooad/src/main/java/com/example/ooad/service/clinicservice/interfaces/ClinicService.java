package com.example.ooad.service.clinicservice.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.ooad.domain.entity.Service;
import com.example.ooad.dto.request.ServiceRequest;

public interface ClinicService {
    public Page<Service> findAllService(int pageNumber, int pageSize);
    public List<Service> findAllServices();
    public Service findServiceById(int serviceId);
    public Service createService(ServiceRequest request);
    public Service updateService(ServiceRequest request, int serviceId);
    public void deleteService(int serviceId);
}

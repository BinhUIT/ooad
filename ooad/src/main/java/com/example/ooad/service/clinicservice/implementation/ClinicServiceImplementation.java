package com.example.ooad.service.clinicservice.implementation;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.ooad.domain.entity.Service;
import com.example.ooad.dto.request.ServiceRequest;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.ServiceRepository;
import com.example.ooad.service.clinicservice.interfaces.ClinicService;
import com.example.ooad.utils.Message;
@org.springframework.stereotype.Service
public class ClinicServiceImplementation implements ClinicService {
    private final ServiceRepository serviceRepo;
    public ClinicServiceImplementation(ServiceRepository serviceRepo) {
        this.serviceRepo = serviceRepo;
    }
    @Override
    public Page<Service> findAllService(int pageNumber, int pageSize) {
       Pageable pageable = PageRequest.of(pageNumber, pageSize);
       return serviceRepo.findAll(pageable);
    }
    @Override
    public Service findServiceById(int serviceId) {
        return serviceRepo.findById(serviceId).orElseThrow(()-> new NotFoundException(Message.serviceNotFound));
    }
    @Override
    public Service createService(ServiceRequest request) {
        Service isServiceExist = serviceRepo.findByServiceName(request.getServiceName());
        if(isServiceExist!=null) {
            throw new ConflictException(Message.serviceExist);
        } 
        Service newService = Service.builder().serviceName(request.getServiceName()).unitPrice(request.getUnitPrice()).build();
        return serviceRepo.save(newService);
    }
    @Override
    public Service updateService(ServiceRequest request, int serviceId) {
       Service updatedService = serviceRepo.findById(serviceId).orElseThrow(()-> new NotFoundException(Message.serviceNotFound));
       Service isServiceExist = serviceRepo.findByServiceName(request.getServiceName());
       if(isServiceExist!=null&&isServiceExist.getServiceId()!=serviceId) {
            throw new ConflictException(Message.serviceExist);
       }
       updatedService.setServiceName(request.getServiceName());
       updatedService.setUnitPrice(request.getUnitPrice());
       return serviceRepo.save(updatedService);
    
    }
    @Override
    public void deleteService(int serviceId){
        Service deletedService = serviceRepo.findById(serviceId).orElseThrow(()-> new NotFoundException(Message.serviceNotFound));
        serviceRepo.delete(deletedService);
    }
    
}

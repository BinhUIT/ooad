package com.example.ooad.service.clinicservice.implementation;



import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.ooad.domain.entity.InvoiceServiceDetail;
import com.example.ooad.domain.entity.Service;
import com.example.ooad.dto.request.ServiceRequest;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.InvoiceServiceDetailRepository;
import com.example.ooad.repository.ServiceRepository;
import com.example.ooad.utils.Message;
@ExtendWith(MockitoExtension.class)
public class ClinicServiceImplementationTest {
    @Mock
    private ServiceRepository serviceRepo;
    @Mock
    private InvoiceServiceDetailRepository invoiceServiceDetailRepo;
    @InjectMocks
    private ClinicServiceImplementation clinicService;
    @Captor
    ArgumentCaptor<List<InvoiceServiceDetail>> invoiceServiceDetailListCaptor;

    @Test
    void findAllServiceTest() {
        
        Page<Service> fakeResult = new PageImpl<>(Arrays.asList(new Service()));

        when(serviceRepo.findAll(any(Pageable.class))).thenReturn(fakeResult);

        Page<Service> result = clinicService.findAllService(3, 4);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

    }

    @Test
    void findServiceByIdSuccess() {
        Service fakeResult = new Service();
        fakeResult.setServiceId(1);
        
        when(serviceRepo.findById(1)).thenReturn(Optional.of(fakeResult));

        Service result = clinicService.findServiceById(1);
        
        assertNotNull(result);
    }

    @Test
    void findServiceByIdFail() {

        when(serviceRepo.findById(1)).thenReturn(Optional.empty());

         NotFoundException exception = assertThrows(NotFoundException.class, ()->{
            clinicService.findServiceById(1);
        });

        assertNotNull(exception);
        assertEquals(Message.serviceNotFound, exception.getMessage());
        
    }

    @Test
    void createServiceSuccess() {
        ServiceRequest fakRequest = new ServiceRequest("", 0);

        when(serviceRepo.findByServiceName(any(String.class))).thenReturn(null);
        when(serviceRepo.save(any(Service.class))).thenReturn(new Service());

        Service result = clinicService.createService(fakRequest);

        assertNotNull(result);
        verify(serviceRepo,times(1)).save(any(Service.class));


    }

    @Test
    void createService_Fail() {
        ServiceRequest fakRequest = new ServiceRequest("", 0);

        when(serviceRepo.findByServiceName(any(String.class))).thenReturn(new Service());

        ConflictException exception = assertThrows(ConflictException.class, ()->{
            clinicService.createService(fakRequest);
        });

        assertNotNull(exception);
        assertEquals(Message.serviceExist,exception.getMessage());
        verify(serviceRepo,never()).save(any(Service.class));
    }

    @Test
    void updateService_Success() {
        ServiceRequest fakRequest = new ServiceRequest("", 0);
        Service fakeService = new Service();
        fakeService.setServiceId(1);
        Service fakeService2 = new Service();
        fakeService2.setServiceId(1);

        when(serviceRepo.findById(1)).thenReturn(Optional.of(fakeService));
        when(serviceRepo.findByServiceName(any(String.class))).thenReturn(fakeService2);
        when(serviceRepo.save(any(Service.class))).thenReturn(fakeService);

        Service result = clinicService.updateService(fakRequest,1);

        assertNotNull(result);
        verify(serviceRepo,times(1)).save(any(Service.class));
    }

    @Test
    void updateService_Fail_ServiceNotFound() {
        ServiceRequest fakRequest = new ServiceRequest("", 0);

        when(serviceRepo.findById(1)).thenReturn(Optional.empty());

         NotFoundException exception = assertThrows(NotFoundException.class, ()->{
            clinicService.updateService(fakRequest, 1);
        });

        assertNotNull(exception);
        assertEquals(Message.serviceNotFound, exception.getMessage());
        verify(serviceRepo, never()).save(any(Service.class));
    }
    @Test
    void updateService_Fail_Conflict() {
        ServiceRequest fakRequest = new ServiceRequest("", 0);
        Service fakeService = new Service();
        fakeService.setServiceId(1);
        Service fakeService2 = new Service();
        fakeService2.setServiceId(2);

        when(serviceRepo.findById(1)).thenReturn(Optional.of(fakeService));
        when(serviceRepo.findByServiceName(any(String.class))).thenReturn(fakeService2);

        ConflictException exception = assertThrows(ConflictException.class, ()->{
            clinicService.updateService(fakRequest,1);
        });

        assertNotNull(exception);
        assertEquals(Message.serviceExist,exception.getMessage());
        verify(serviceRepo,never()).save(any(Service.class));
    }

    @Test
    void deleteServiceSuccess() {
        Service fakeService = new Service();
        fakeService.setServiceId(1);
        InvoiceServiceDetail invoiceServiceDetail1= new InvoiceServiceDetail();
        invoiceServiceDetail1.setService(fakeService);
        InvoiceServiceDetail invoiceServiceDetail2= new InvoiceServiceDetail();
        invoiceServiceDetail2.setService(fakeService);
        List<InvoiceServiceDetail> fakeInvoiceServiceDetails = Arrays.asList(invoiceServiceDetail1,invoiceServiceDetail2);

        when(serviceRepo.findById(1)).thenReturn(Optional.of(fakeService));
        when(invoiceServiceDetailRepo.findByService_ServiceId(1)).thenReturn(fakeInvoiceServiceDetails);
        when(invoiceServiceDetailRepo.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        clinicService.deleteService(1);

        verify(invoiceServiceDetailRepo,times(1)).saveAll(invoiceServiceDetailListCaptor.capture());
        verify(serviceRepo,times(1)).delete(any(Service.class));
        
    }
    @Test
    void deleteService_Fail_ServiceNotFound() {
        when(serviceRepo.findById(1)).thenReturn(Optional.empty());

         NotFoundException exception = assertThrows(NotFoundException.class, ()->{
            clinicService.deleteService(1);
        });

        assertNotNull(exception);
        assertEquals(Message.serviceNotFound, exception.getMessage());
        verify(invoiceServiceDetailRepo,never()).saveAll(invoiceServiceDetailListCaptor.capture());
        verify(serviceRepo, never()).delete(any(Service.class));

    }
}

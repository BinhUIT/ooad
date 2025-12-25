package com.example.ooad.service.invoice.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.entity.InvoiceMedicineDetail;
import com.example.ooad.domain.entity.InvoiceServiceDetail;
import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.RefPaymentMethod;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EPaymentStatus;
import com.example.ooad.dto.request.invoice.InvoiceMedicineDetailRequest;
import com.example.ooad.dto.request.invoice.InvoiceSearchRequest;
import com.example.ooad.dto.request.invoice.InvoiceServiceDetailRequest;
import com.example.ooad.dto.request.invoice.UpdateInvoiceRequest;
import com.example.ooad.dto.response.invoice.AvailableMedicineResponse;
import com.example.ooad.dto.response.invoice.InvoiceDetailResponse;
import com.example.ooad.dto.response.invoice.InvoiceListResponse;
import com.example.ooad.dto.response.invoice.InvoiceMedicineDetailResponse;
import com.example.ooad.dto.response.invoice.InvoiceServiceDetailResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.InvoiceMedicineDetailRepository;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.repository.InvoiceServiceDetailRepository;
import com.example.ooad.repository.MedicineInventoryRepository;
import com.example.ooad.repository.MedicinePriceRepository;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.repository.RefPaymentMethodRepository;
import com.example.ooad.repository.ServiceRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.service.invoice.interfaces.InvoiceService;
import com.example.ooad.utils.Message;

@Service
public class InvoiceServiceImplementation implements InvoiceService {
    private final InvoiceRepository invoiceRepo;
    private final InvoiceMedicineDetailRepository medicineDetailRepo;
    private final InvoiceServiceDetailRepository serviceDetailRepo;
    private final MedicineRepository medicineRepo;
    private final ServiceRepository serviceRepo;
    private final MedicineInventoryRepository inventoryRepo;
    private final MedicinePriceRepository priceRepo;
    private final RefPaymentMethodRepository paymentMethodRepo;
    private final StaffRepository staffRepo;

    public InvoiceServiceImplementation(
            InvoiceRepository invoiceRepo,
            InvoiceMedicineDetailRepository medicineDetailRepo,
            InvoiceServiceDetailRepository serviceDetailRepo,
            MedicineRepository medicineRepo,
            ServiceRepository serviceRepo,
            MedicineInventoryRepository inventoryRepo,
            MedicinePriceRepository priceRepo,
            RefPaymentMethodRepository paymentMethodRepo,
            StaffRepository staffRepo) {
        this.invoiceRepo = invoiceRepo;
        this.medicineDetailRepo = medicineDetailRepo;
        this.serviceDetailRepo = serviceDetailRepo;
        this.medicineRepo = medicineRepo;
        this.serviceRepo = serviceRepo;
        this.inventoryRepo = inventoryRepo;
        this.priceRepo = priceRepo;
        this.paymentMethodRepo = paymentMethodRepo;
        this.staffRepo = staffRepo;
    }

    @Override
    public Invoice findInvoiceById(int invoiceId) {
        return invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException(Message.invoiceNotFound));
    }

    @Override
    public Page<InvoiceListResponse> searchInvoices(InvoiceSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        
        Date fromDate = request.getFromDate() != null ? Date.valueOf(request.getFromDate()) : null;
        Date toDate = request.getToDate() != null ? Date.valueOf(request.getToDate()) : null;
        
        Page<Invoice> invoices = invoiceRepo.searchInvoices(
                request.getPatientName(),
                request.getPatientPhone(),
                request.getPatientId(),
                request.getRecordId(),
                request.getPaymentStatus(),
                fromDate,
                toDate,
                pageable);
        
        return invoices.map(this::mapToListResponse);
    }

    @Override
    public InvoiceDetailResponse getInvoiceDetail(int invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);
        return mapToDetailResponse(invoice);
    }

    @Override
    @Transactional
    public Invoice updateInvoice(int invoiceId, UpdateInvoiceRequest request) {
        Invoice invoice = findInvoiceById(invoiceId);
        
        if (invoice.getPaymentStatus() == EPaymentStatus.PAID) {
            throw new BadRequestException("Cannot update a paid invoice");
        }
        
        if (request.getPaymentMethodId() != null) {
            RefPaymentMethod paymentMethod = paymentMethodRepo.findById(request.getPaymentMethodId())
                    .orElseThrow(() -> new NotFoundException("Payment method not found"));
            invoice.setPaymentMethod(paymentMethod);
        }
        
        return invoiceRepo.save(invoice);
    }

    @Override
    @Transactional
    public InvoiceDetailResponse updateMedicineDetails(int invoiceId, List<InvoiceMedicineDetailRequest> details) {
        Invoice invoice = findInvoiceById(invoiceId);
        
        if (invoice.getPaymentStatus() == EPaymentStatus.PAID) {
            throw new BadRequestException("Cannot update a paid invoice");
        }
        
        // Validate medicine availability before processing
        Date minExpiryDate = Date.valueOf(LocalDate.now().plusMonths(3));
        for (InvoiceMedicineDetailRequest detail : details) {
            int availableQty = inventoryRepo.getTotalAvailableQuantity(detail.getMedicineId(), minExpiryDate);
            if (availableQty < detail.getQuantity()) {
                Medicine medicine = medicineRepo.findById(detail.getMedicineId())
                        .orElseThrow(() -> new NotFoundException("Medicine not found: " + detail.getMedicineId()));
                throw new BadRequestException(
                        "Insufficient inventory for medicine: " + medicine.getMedicineName() + 
                        ". Available: " + availableQty + ", Requested: " + detail.getQuantity());
            }
        }
        
        // Collect IDs to keep
        List<Integer> keepIds = details.stream()
                .filter(d -> d.getDetailId() != null)
                .map(InvoiceMedicineDetailRequest::getDetailId)
                .collect(Collectors.toList());
        
        // Delete removed details
        if (keepIds.isEmpty()) {
            medicineDetailRepo.deleteByInvoiceId(invoiceId);
        } else {
            medicineDetailRepo.deleteByInvoiceIdAndDetailIdNotIn(invoiceId, keepIds);
        }
        
        // Update or create details
        for (InvoiceMedicineDetailRequest detailReq : details) {
            Medicine medicine = medicineRepo.findById(detailReq.getMedicineId())
                    .orElseThrow(() -> new NotFoundException("Medicine not found: " + detailReq.getMedicineId()));
            
            BigDecimal salePrice = detailReq.getSalePrice();
            if (salePrice == null) {
                // Get current price from medicine_price table
                salePrice = priceRepo.getCurrentPrice(detailReq.getMedicineId(), Date.valueOf(LocalDate.now()));
                if (salePrice == null) {
                    throw new BadRequestException("No price found for medicine: " + medicine.getMedicineName());
                }
            }
            
            BigDecimal amount = salePrice.multiply(BigDecimal.valueOf(detailReq.getQuantity()));
            
            if (detailReq.getDetailId() != null) {
                // Update existing
                InvoiceMedicineDetail existingDetail = medicineDetailRepo.findById(detailReq.getDetailId())
                        .orElseThrow(() -> new NotFoundException("Medicine detail not found"));
                existingDetail.setMedicine(medicine);
                existingDetail.setQuantity(detailReq.getQuantity());
                existingDetail.setSalePrice(salePrice);
                existingDetail.setAmount(amount);
                medicineDetailRepo.save(existingDetail);
            } else {
                // Create new
                InvoiceMedicineDetail newDetail = new InvoiceMedicineDetail();
                newDetail.setInvoice(invoice);
                newDetail.setMedicine(medicine);
                newDetail.setQuantity(detailReq.getQuantity());
                newDetail.setSalePrice(salePrice);
                newDetail.setAmount(amount);
                medicineDetailRepo.save(newDetail);
            }
        }
        
        // Recalculate totals
        recalculateInvoiceTotals(invoiceId);
        
        return getInvoiceDetail(invoiceId);
    }

    @Override
    @Transactional
    public InvoiceDetailResponse updateServiceDetails(int invoiceId, List<InvoiceServiceDetailRequest> details) {
        Invoice invoice = findInvoiceById(invoiceId);
        
        if (invoice.getPaymentStatus() == EPaymentStatus.PAID) {
            throw new BadRequestException("Cannot update a paid invoice");
        }
        
        // Collect IDs to keep
        List<Integer> keepIds = details.stream()
                .filter(d -> d.getDetailId() != null)
                .map(InvoiceServiceDetailRequest::getDetailId)
                .collect(Collectors.toList());
        
        // Delete removed details
        if (keepIds.isEmpty()) {
            serviceDetailRepo.deleteByInvoiceId(invoiceId);
        } else {
            serviceDetailRepo.deleteByInvoiceIdAndDetailIdNotIn(invoiceId, keepIds);
        }
        
        // Update or create details
        for (InvoiceServiceDetailRequest detailReq : details) {
            com.example.ooad.domain.entity.Service service = serviceRepo.findById(detailReq.getServiceId())
                    .orElseThrow(() -> new NotFoundException("Service not found: " + detailReq.getServiceId()));
            
            BigDecimal salePrice = detailReq.getSalePrice();
            if (salePrice == null) {
                salePrice = service.getUnitPrice();
            }
            
            BigDecimal amount = salePrice.multiply(BigDecimal.valueOf(detailReq.getQuantity()));
            
            if (detailReq.getDetailId() != null) {
                // Update existing
                InvoiceServiceDetail existingDetail = serviceDetailRepo.findById(detailReq.getDetailId())
                        .orElseThrow(() -> new NotFoundException("Service detail not found"));
                existingDetail.setService(service);
                existingDetail.setQuantity(detailReq.getQuantity());
                existingDetail.setSalePrice(salePrice);
                existingDetail.setAmount(amount);
                serviceDetailRepo.save(existingDetail);
            } else {
                // Create new
                InvoiceServiceDetail newDetail = new InvoiceServiceDetail();
                newDetail.setInvoice(invoice);
                newDetail.setService(service);
                newDetail.setQuantity(detailReq.getQuantity());
                newDetail.setSalePrice(salePrice);
                newDetail.setAmount(amount);
                serviceDetailRepo.save(newDetail);
            }
        }
        
        // Recalculate totals
        recalculateInvoiceTotals(invoiceId);
        
        return getInvoiceDetail(invoiceId);
    }

    @Override
    @Transactional
    public InvoiceDetailResponse addMedicineDetail(int invoiceId, InvoiceMedicineDetailRequest detail) {
        Invoice invoice = findInvoiceById(invoiceId);
        
        if (invoice.getPaymentStatus() == EPaymentStatus.PAID) {
            throw new BadRequestException("Cannot update a paid invoice");
        }
        
        // Validate medicine availability
        Date minExpiryDate = Date.valueOf(LocalDate.now().plusMonths(3));
        int availableQty = inventoryRepo.getTotalAvailableQuantity(detail.getMedicineId(), minExpiryDate);
        if (availableQty < detail.getQuantity()) {
            Medicine medicine = medicineRepo.findById(detail.getMedicineId())
                    .orElseThrow(() -> new NotFoundException("Medicine not found: " + detail.getMedicineId()));
            throw new BadRequestException(
                    "Insufficient inventory for medicine: " + medicine.getMedicineName() + 
                    ". Available: " + availableQty + ", Requested: " + detail.getQuantity());
        }
        
        Medicine medicine = medicineRepo.findById(detail.getMedicineId())
                .orElseThrow(() -> new NotFoundException("Medicine not found: " + detail.getMedicineId()));
        
        BigDecimal salePrice = detail.getSalePrice();
        if (salePrice == null) {
            salePrice = priceRepo.getCurrentPrice(detail.getMedicineId(), Date.valueOf(LocalDate.now()));
            if (salePrice == null) {
                throw new BadRequestException("No price found for medicine: " + medicine.getMedicineName());
            }
        }
        
        BigDecimal amount = salePrice.multiply(BigDecimal.valueOf(detail.getQuantity()));
        
        // Create new detail
        InvoiceMedicineDetail newDetail = new InvoiceMedicineDetail();
        newDetail.setInvoice(invoice);
        newDetail.setMedicine(medicine);
        newDetail.setQuantity(detail.getQuantity());
        newDetail.setSalePrice(salePrice);
        newDetail.setAmount(amount);
        medicineDetailRepo.save(newDetail);
        
        // Recalculate totals
        recalculateInvoiceTotals(invoiceId);
        
        return getInvoiceDetail(invoiceId);
    }

    @Override
    @Transactional
    public InvoiceDetailResponse addServiceDetail(int invoiceId, InvoiceServiceDetailRequest detail) {
        Invoice invoice = findInvoiceById(invoiceId);
        
        if (invoice.getPaymentStatus() == EPaymentStatus.PAID) {
            throw new BadRequestException("Cannot update a paid invoice");
        }
        
        com.example.ooad.domain.entity.Service service = serviceRepo.findById(detail.getServiceId())
                .orElseThrow(() -> new NotFoundException("Service not found: " + detail.getServiceId()));
        
        BigDecimal salePrice = detail.getSalePrice();
        if (salePrice == null) {
            salePrice = service.getUnitPrice();
        }
        
        BigDecimal amount = salePrice.multiply(BigDecimal.valueOf(detail.getQuantity()));
        
        // Create new detail
        InvoiceServiceDetail newDetail = new InvoiceServiceDetail();
        newDetail.setInvoice(invoice);
        newDetail.setService(service);
        newDetail.setQuantity(detail.getQuantity());
        newDetail.setSalePrice(salePrice);
        newDetail.setAmount(amount);
        serviceDetailRepo.save(newDetail);
        
        // Recalculate totals
        recalculateInvoiceTotals(invoiceId);
        
        return getInvoiceDetail(invoiceId);
    }

    @Override
    @Transactional
    public void deleteMedicineDetail(int invoiceId, int detailId) {
        Invoice invoice = findInvoiceById(invoiceId);
        
        if (invoice.getPaymentStatus() == EPaymentStatus.PAID) {
            throw new BadRequestException("Cannot update a paid invoice");
        }
        
        InvoiceMedicineDetail detail = medicineDetailRepo.findById(detailId)
                .orElseThrow(() -> new NotFoundException("Medicine detail not found"));
        
        if (detail.getInvoice().getInvoiceId() != invoiceId) {
            throw new BadRequestException("Detail does not belong to this invoice");
        }
        
        medicineDetailRepo.delete(detail);
        recalculateInvoiceTotals(invoiceId);
    }

    @Override
    @Transactional
    public void deleteServiceDetail(int invoiceId, int detailId) {
        Invoice invoice = findInvoiceById(invoiceId);
        
        if (invoice.getPaymentStatus() == EPaymentStatus.PAID) {
            throw new BadRequestException("Cannot update a paid invoice");
        }
        
        InvoiceServiceDetail detail = serviceDetailRepo.findById(detailId)
                .orElseThrow(() -> new NotFoundException("Service detail not found"));
        
        if (detail.getInvoice().getInvoiceId() != invoiceId) {
            throw new BadRequestException("Detail does not belong to this invoice");
        }
        
        serviceDetailRepo.delete(detail);
        recalculateInvoiceTotals(invoiceId);
    }

    @Override
    public List<AvailableMedicineResponse> getAvailableMedicines(int minMonthsBeforeExpiry) {
        Date minExpiryDate = Date.valueOf(LocalDate.now().plusMonths(minMonthsBeforeExpiry));
        Date currentDate = Date.valueOf(LocalDate.now());
        
        List<Medicine> allMedicines = medicineRepo.findAll();
        List<AvailableMedicineResponse> result = new ArrayList<>();
        
        for (Medicine medicine : allMedicines) {
            int availableQty = inventoryRepo.getTotalAvailableQuantity(medicine.getMedicineId(), minExpiryDate);
            
            if (availableQty > 0) {
                AvailableMedicineResponse response = new AvailableMedicineResponse();
                response.setMedicineId(medicine.getMedicineId());
                response.setMedicineName(medicine.getMedicineName());
                response.setUnit(medicine.getUnit() != null ? medicine.getUnit().name() : null);
                response.setConcentration(medicine.getConcentration());
                response.setForm(medicine.getForm());
                response.setManufacturer(medicine.getManufacturer());
                response.setAvailableQuantity(availableQty);
                response.setUnitPrice(priceRepo.getCurrentPrice(medicine.getMedicineId(), currentDate));
                response.setNearestExpiryDate(inventoryRepo.getNearestExpiryDate(medicine.getMedicineId(), minExpiryDate));
                result.add(response);
            }
        }
        
        return result;
    }

    @Override
    @Transactional
    public Invoice markAsPaid(int invoiceId, int paymentMethodId, int staffId) {
        Invoice invoice = findInvoiceById(invoiceId);
        
        if (invoice.getPaymentStatus() == EPaymentStatus.PAID) {
            throw new BadRequestException("Invoice is already paid");
        }
        
        RefPaymentMethod paymentMethod = paymentMethodRepo.findById(paymentMethodId)
                .orElseThrow(() -> new NotFoundException("Payment method not found"));
        
        Staff staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new NotFoundException("Staff not found"));
        
        invoice.setPaymentStatus(EPaymentStatus.PAID);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setIssueBy(staff);
        invoice.setInvoiceDate(Date.valueOf(LocalDate.now()));
        
        return invoiceRepo.save(invoice);
    }

    @Override
    @Transactional
    public void recalculateInvoiceTotals(int invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);
        
        // Calculate medicine fee
        List<InvoiceMedicineDetail> medicineDetails = medicineDetailRepo.findByInvoice_InvoiceId(invoiceId);
        BigDecimal medicineFee = medicineDetails.stream()
                .map(InvoiceMedicineDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate service fee
        List<InvoiceServiceDetail> serviceDetails = serviceDetailRepo.findByInvoice_InvoiceId(invoiceId);
        BigDecimal serviceFee = serviceDetails.stream()
                .map(InvoiceServiceDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        invoice.setMedicineFee(medicineFee);
        invoice.setServiceFee(serviceFee);
        invoice.setTotalAmount(invoice.getExaminationFee().add(medicineFee).add(serviceFee));
        
        invoiceRepo.save(invoice);
    }

    @Override
    public Page<InvoiceListResponse> getPatientInvoices(int patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Invoice> invoices = invoiceRepo.findByPatientIdPaginated(patientId, pageable);
        return invoices.map(this::mapToListResponse);
    }

    @Override
    public InvoiceDetailResponse getPatientInvoiceDetail(int patientId, int invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);
        
        if (invoice.getPatient().getPatientId() != patientId) {
            throw new BadRequestException("Invoice does not belong to this patient");
        }
        
        return mapToDetailResponse(invoice);
    }

    // Helper methods
    private InvoiceListResponse mapToListResponse(Invoice invoice) {
        InvoiceListResponse response = new InvoiceListResponse();
        response.setInvoiceId(invoice.getInvoiceId());
        response.setPatientId(invoice.getPatient().getPatientId());
        response.setPatientName(invoice.getPatient().getFullName());
        response.setPatientPhone(invoice.getPatient().getPhone());
        response.setRecordId(invoice.getRecord() != null ? invoice.getRecord().getRecordId() : null);
        response.setInvoiceDate(invoice.getInvoiceDate());
        response.setExaminationFee(invoice.getExaminationFee());
        response.setMedicineFee(invoice.getMedicineFee());
        response.setServiceFee(invoice.getServiceFee());
        response.setTotalAmount(invoice.getTotalAmount());
        response.setPaymentMethodName(invoice.getPaymentMethod() != null ? invoice.getPaymentMethod().getMethodName() : null);
        response.setPaymentStatus(invoice.getPaymentStatus());
        response.setIssuedByName(invoice.getIssueBy() != null ? invoice.getIssueBy().getFullName() : null);
        return response;
    }

    private InvoiceDetailResponse mapToDetailResponse(Invoice invoice) {
        InvoiceDetailResponse response = new InvoiceDetailResponse();
        response.setInvoiceId(invoice.getInvoiceId());
        response.setInvoiceDate(invoice.getInvoiceDate());
        response.setExaminationFee(invoice.getExaminationFee());
        response.setMedicineFee(invoice.getMedicineFee());
        response.setServiceFee(invoice.getServiceFee());
        response.setTotalAmount(invoice.getTotalAmount());
        response.setPaymentStatus(invoice.getPaymentStatus());
        
        // Patient info
        if (invoice.getPatient() != null) {
            InvoiceDetailResponse.PatientInfo patientInfo = new InvoiceDetailResponse.PatientInfo();
            patientInfo.setPatientId(invoice.getPatient().getPatientId());
            patientInfo.setFullName(invoice.getPatient().getFullName());
            patientInfo.setPhone(invoice.getPatient().getPhone());
            patientInfo.setAddress(invoice.getPatient().getAddress());
            patientInfo.setDateOfBirth(invoice.getPatient().getDateOfBirth());
            response.setPatient(patientInfo);
        }
        
        // Record info
        if (invoice.getRecord() != null) {
            InvoiceDetailResponse.RecordInfo recordInfo = new InvoiceDetailResponse.RecordInfo();
            recordInfo.setRecordId(invoice.getRecord().getRecordId());
            recordInfo.setExaminateDate(invoice.getRecord().getExaminateDate());
            recordInfo.setDiagnosis(invoice.getRecord().getDiagnosis());
            recordInfo.setSymptoms(invoice.getRecord().getSymptoms());
            recordInfo.setDoctorName(invoice.getRecord().getDoctorName());
            response.setRecord(recordInfo);
        }
        
        // Payment method info
        if (invoice.getPaymentMethod() != null) {
            InvoiceDetailResponse.PaymentMethodInfo pmInfo = new InvoiceDetailResponse.PaymentMethodInfo();
            pmInfo.setPaymentMethodId(invoice.getPaymentMethod().getPaymentMethodId());
            pmInfo.setMethodCode(invoice.getPaymentMethod().getMethodCode());
            pmInfo.setMethodName(invoice.getPaymentMethod().getMethodName());
            response.setPaymentMethod(pmInfo);
        }
        
        // Issued by info
        if (invoice.getIssueBy() != null) {
            InvoiceDetailResponse.StaffInfo staffInfo = new InvoiceDetailResponse.StaffInfo();
            staffInfo.setStaffId(invoice.getIssueBy().getStaffId());
            staffInfo.setFullName(invoice.getIssueBy().getFullName());
            response.setIssuedBy(staffInfo);
        }
        
        // Service details
        List<InvoiceServiceDetail> serviceDetails = serviceDetailRepo.findByInvoice_InvoiceId(invoice.getInvoiceId());
        response.setServiceDetails(serviceDetails.stream().map(this::mapToServiceDetailResponse).collect(Collectors.toList()));
        
        // Medicine details
        List<InvoiceMedicineDetail> medicineDetails = medicineDetailRepo.findByInvoice_InvoiceId(invoice.getInvoiceId());
        response.setMedicineDetails(medicineDetails.stream().map(this::mapToMedicineDetailResponse).collect(Collectors.toList()));
        
        return response;
    }

    private InvoiceServiceDetailResponse mapToServiceDetailResponse(InvoiceServiceDetail detail) {
        InvoiceServiceDetailResponse response = new InvoiceServiceDetailResponse();
        response.setDetailId(detail.getDetailId());
        response.setServiceId(detail.getService() != null ? detail.getService().getServiceId() : 0);
        response.setServiceName(detail.getService() != null ? detail.getService().getServiceName() : null);
        response.setQuantity(detail.getQuantity());
        response.setSalePrice(detail.getSalePrice());
        response.setAmount(detail.getAmount());
        return response;
    }

    private InvoiceMedicineDetailResponse mapToMedicineDetailResponse(InvoiceMedicineDetail detail) {
        InvoiceMedicineDetailResponse response = new InvoiceMedicineDetailResponse();
        response.setDetailId(detail.getDetailId());
        response.setMedicineId(detail.getMedicine() != null ? detail.getMedicine().getMedicineId() : 0);
        response.setMedicineName(detail.getMedicine() != null ? detail.getMedicine().getMedicineName() : null);
        response.setUnit(detail.getMedicine() != null && detail.getMedicine().getUnit() != null ? detail.getMedicine().getUnit().name() : null);
        response.setConcentration(detail.getMedicine() != null ? detail.getMedicine().getConcentration() : null);
        response.setQuantity(detail.getQuantity());
        response.setSalePrice(detail.getSalePrice());
        response.setAmount(detail.getAmount());
        return response;
    }
}

package com.example.ooad.service.prescription.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.compositekey.PrescriptionDetailKey;
import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.entity.InvoiceMedicineDetail;
import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Prescription;
import com.example.ooad.domain.entity.PrescriptionDetail;
import com.example.ooad.dto.request.PrescriptionDetailRequest;
import com.example.ooad.dto.request.PrescriptionRequest;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.InvoiceMedicineDetailRepository;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.repository.MedicinePriceRepository;
import com.example.ooad.repository.PrescriptionDetailRepository;
import com.example.ooad.repository.PrescriptionRepository;
import com.example.ooad.service.medical_record.interfaces.MedicalRecordService;
import com.example.ooad.service.patient.interfaces.PatientService;
import com.example.ooad.service.prescription.interfaces.PrescriptionService;
import com.example.ooad.utils.Message;

import jakarta.transaction.Transactional;

@Service
public class PrescriptionServiceImplementation implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepo;
    private final PrescriptionDetailRepository prescriptionDetailRepo;
    private final MedicalRecordService medicalRecordService;
    private final MedicineRepository medicineRepo;
    private final PatientService patientService;
    private final InvoiceRepository invoiceRepo;
    private final InvoiceMedicineDetailRepository invoiceMedicineDetailRepo;
    private final MedicinePriceRepository medicinePriceRepo;

    public PrescriptionServiceImplementation(PrescriptionRepository prescriptionRepo,
            PrescriptionDetailRepository prescriptionDetailRepo,
            MedicalRecordService medicalRecordService,
            MedicineRepository medicineRepo,
            PatientService patientService,
            InvoiceRepository invoiceRepo,
            InvoiceMedicineDetailRepository invoiceMedicineDetailRepo,
            MedicinePriceRepository medicinePriceRepo) {
        this.prescriptionRepo = prescriptionRepo;
        this.prescriptionDetailRepo = prescriptionDetailRepo;
        this.medicalRecordService = medicalRecordService;
        this.medicineRepo = medicineRepo;
        this.patientService = patientService;
        this.invoiceRepo = invoiceRepo;
        this.invoiceMedicineDetailRepo = invoiceMedicineDetailRepo;
        this.medicinePriceRepo = medicinePriceRepo;
    }

    // 3-param version (backward compatibility) - delegates to 4-param version
    @Override
    public Page<Prescription> getAllPrescription(int pageNumber, int pageSize, Optional<Date> prescriptionDate) {
        return getAllPrescription(pageNumber, pageSize, prescriptionDate, Optional.empty());
    }

    // 4-param version with patientName filter
    @Override
    public Page<Prescription> getAllPrescription(int pageNumber, int pageSize, Optional<Date> prescriptionDate,
            Optional<String> patientName) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Date pDate = prescriptionDate.orElse(null);
        String pName = patientName.orElse(null);
        return prescriptionRepo.findPrescriptions(pageable, pDate, pName);
    }

    @Override
    public Prescription getPrescriptionById(int prescriptionId) {
        return prescriptionRepo.findById(prescriptionId)
                .orElseThrow(() -> new NotFoundException(Message.prescriptionNotFound));
    }

    @Override
    public Prescription getPrescriptionByRecordId(int recordId) {
        // Get latest prescription when multiple exist for same record
        return prescriptionRepo.findLatestByRecordId(recordId)
                .orElse(null); // Return null if no prescription found for this record
    }

    // List version (no pagination)
    @Override
    public List<PrescriptionDetail> getPrescriptionDetailOfPrescription(int prescriptionId) {
        return prescriptionDetailRepo.findByPrescription_PrescriptionId(prescriptionId);
    }

    // Page version (with pagination)
    @Override
    public Page<PrescriptionDetail> getPrescriptionDetailOfPrescription(int pageNumber, int pageSize,
            int prescriptionId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return prescriptionDetailRepo.findByPrescription_PrescriptionId(pageable, prescriptionId);
    }

    @Override
    @Transactional
    public Prescription createPrescription(PrescriptionRequest request) {
        Prescription prescription = new Prescription();

        return fillInfoToPrescription(prescription, request, true);
    }

    @Override
    @Transactional
    public Prescription updatePrescription(PrescriptionRequest request, int prescriptionId) {
        Prescription prescription = this.getPrescriptionById(prescriptionId);
        return fillInfoToPrescription(prescription, request, false);
    }

    private Prescription fillInfoToPrescription(Prescription prescription, PrescriptionRequest request,
            boolean isCreate) {
        MedicalRecord record = medicalRecordService.findMedicalRecordById(request.getRecordId());

        // Check if invoice is paid - cannot update paid prescriptions
        Optional<Invoice> invoiceOpt = invoiceRepo.findByRecord_RecordId(record.getRecordId());
        if (invoiceOpt.isPresent()
                && invoiceOpt.get().getPaymentStatus() == com.example.ooad.domain.enums.EPaymentStatus.PAID) {
            throw new BadRequestException("Cannot update prescription - invoice has been paid");
        }

        clearPrescriptionDetail(prescription);
        prescription.setNotes(request.getNotes());
        prescription.setRecord(record);
        prescription = prescriptionRepo.save(prescription);
        List<PrescriptionDetail> prescriptionDetails = new ArrayList<>();
        List<Integer> listMedicineIds = request.getPrescriptionDetails().stream().map(item -> item.getMedicineId())
                .toList();
        List<Medicine> medicines = medicineRepo.findByMedicineId_In(listMedicineIds);

        for (PrescriptionDetailRequest detailRequest : request.getPrescriptionDetails()) {
            prescriptionDetails.add(fromRequestToPrescriptionDetail(prescription, detailRequest, medicines, isCreate));
        }
        prescriptionDetailRepo.saveAll(prescriptionDetails);

        // Sync invoice medicine details after saving prescription
        syncInvoiceMedicineDetails(prescription);

        return prescription;

    }

    private PrescriptionDetail fromRequestToPrescriptionDetail(Prescription prescription,
            PrescriptionDetailRequest detailRequest, List<Medicine> medicines, boolean isCreate) {
        if (!isCreate && prescription.getPrescriptionId() != detailRequest.getPrescriptionId()) {
            throw new BadRequestException(Message.invalidData);
        }
        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setDays(detailRequest.getDays());
        prescriptionDetail.setDosage(detailRequest.getDosage());
        prescriptionDetail.setQuantity(detailRequest.getQuantity());

        Medicine med = findByMedicineIdInList(medicines, detailRequest.getMedicineId());
        PrescriptionDetailKey key = new PrescriptionDetailKey(prescription.getPrescriptionId(), med.getMedicineId());
        prescriptionDetail.setPrescriptionDetailId(key);
        prescriptionDetail.setPrescription(prescription);
        prescriptionDetail.setMedicine(med);
        return prescriptionDetail;
    }

    private Medicine findByMedicineIdInList(List<Medicine> medicines, int medicineId) {
        for (Medicine med : medicines) {
            if (med.getMedicineId() == medicineId) {
                return med;
            }
        }
        throw new NotFoundException(Message.medicineNotFound);
    }

    private void clearPrescriptionDetail(Prescription prescription) {
        List<PrescriptionDetail> prescriptionDetails = prescriptionDetailRepo
                .findByPrescription_PrescriptionId(prescription.getPrescriptionId());
        prescriptionDetailRepo.deleteAll(prescriptionDetails);
    }

    @Override
    public List<MedicalRecord> getRecords() {
        return medicalRecordService.findAllRecords();
    }

    @Override
    public List<Medicine> getMedicines() {
        return medicineRepo.findAll();
    }

    @Override
    public Page<Prescription> getPrescriptionsOfPatient(Authentication auth, int pageNumber, int pageSize,
            Optional<Date> prescriptionDate) {
        Patient p = patientService.getPatientFromAuth(auth);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Date filterDate = prescriptionDate.orElse(null);
        return prescriptionRepo.findPrescriptionsOfPatient(pageable, filterDate, p.getPatientId());
    }

    private void syncInvoiceMedicineDetails(Prescription prescription) {
        MedicalRecord record = prescription.getRecord();
        if (record == null) {
            return;
        }

        Optional<Invoice> invoiceOpt = invoiceRepo.findByRecord_RecordId(record.getRecordId());
        if (!invoiceOpt.isPresent()) {
            return;
        }

        Invoice invoice = invoiceOpt.get();

        invoiceMedicineDetailRepo.deleteByInvoiceId(invoice.getInvoiceId());

        List<PrescriptionDetail> prescriptionDetails = prescriptionDetailRepo
                .findByPrescription_PrescriptionId(prescription.getPrescriptionId());

        for (PrescriptionDetail presDetail : prescriptionDetails) {
            Medicine medicine = presDetail.getMedicine();
            if (medicine == null)
                continue;

            BigDecimal salePrice = medicinePriceRepo.getCurrentPrice(
                    medicine.getMedicineId(),
                    Date.valueOf(LocalDate.now()));

            if (salePrice == null) {
                salePrice = BigDecimal.ZERO;
            }

            BigDecimal amount = salePrice.multiply(BigDecimal.valueOf(presDetail.getQuantity()));

            InvoiceMedicineDetail detail = new InvoiceMedicineDetail();
            detail.setInvoice(invoice);
            detail.setMedicine(medicine);
            detail.setQuantity(presDetail.getQuantity());
            detail.setSalePrice(salePrice);
            detail.setAmount(amount);
            invoiceMedicineDetailRepo.save(detail);
        }

        recalculateInvoice(invoice);
    }

    private void recalculateInvoice(Invoice invoice) {
        List<InvoiceMedicineDetail> medicineDetails = invoiceMedicineDetailRepo
                .findByInvoice_InvoiceId(invoice.getInvoiceId());
        BigDecimal medicineFee = medicineDetails.stream()
                .map(InvoiceMedicineDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        invoice.setMedicineFee(medicineFee);
        invoice.setTotalAmount(invoice.getExaminationFee().add(medicineFee).add(invoice.getServiceFee()));
        invoiceRepo.save(invoice);
    }
}

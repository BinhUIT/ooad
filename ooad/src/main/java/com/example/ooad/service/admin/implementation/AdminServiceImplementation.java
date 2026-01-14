package com.example.ooad.service.admin.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EAppointmentStatus;
import com.example.ooad.domain.enums.EPaymentStatus;
import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.AdminStatistic;
import com.example.ooad.dto.response.AppointmentChartData;
import com.example.ooad.dto.response.AppointmentStatistic;
import com.example.ooad.dto.response.PatientVisitDetailResponse;
import com.example.ooad.dto.response.PatientVisitReportData;
import com.example.ooad.dto.response.PatientVisitStatistic;
import com.example.ooad.dto.response.StaffScheduleResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.mapper.StaffScheduleMapper;
import com.example.ooad.repository.AppointmentRepository;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.repository.MedicalRecordRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.StaffScheduleRepository;
import com.example.ooad.service.admin.interfaces.AdminService;
import com.example.ooad.service.staff.implementation.StaffServiceImplementation;
import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.StaffScheduleValidator;

@Service
public class AdminServiceImplementation implements AdminService {
    private final StaffService staffService;
    private final StaffScheduleRepository staffScheduleRepo;
    private final StaffScheduleValidator staffScheduleValidator;
    private final AppointmentRepository appointmentRepo;
    private final InvoiceRepository invoiceRepo;
    private final MedicalRecordRepository medicalRecordRepo;
    private final PatientRepository patientRepo;

    public AdminServiceImplementation(StaffServiceImplementation staffService,
            StaffScheduleRepository staffScheduleRepo,
            StaffScheduleValidator staffScheduleValidator, AppointmentRepository appointmentRepo,
            InvoiceRepository invoiceRepo, MedicalRecordRepository medicalRecordRepo, PatientRepository patientRepo) {
        this.staffService = staffService;
        this.staffScheduleRepo = staffScheduleRepo;
        this.staffScheduleValidator = staffScheduleValidator;
        this.appointmentRepo = appointmentRepo;
        this.invoiceRepo = invoiceRepo;
        this.medicalRecordRepo = medicalRecordRepo;
        this.patientRepo = patientRepo;
    }

    @Override
    public StaffScheduleResponse createSchedule(CreateScheduleRequest request) {
        staffScheduleValidator.validateCreateScheduleRequest(request);
        Staff staff = staffService.findStaffById(request.getStaffId());
        StaffSchedule staffSchedule = StaffScheduleMapper.getScheduleFromRequestAndStaff(request, staff);
        try {
            staffSchedule = staffScheduleRepo.save(staffSchedule);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(Message.staffBusy);
        }
        return StaffScheduleMapper.getResponseFromStaffSchedule(staffSchedule);

    }

    @Override
    public List<AppointmentChartData> getAppointmentChart(int year) {
        String[] monthName = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        List<AppointmentChartData> result = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            AppointmentChartData chartData = new AppointmentChartData(monthName[i], 0, 0, 0, 0, 0);
            List<Appointment> appointments = appointmentRepo.getAppointmentsByMonthAndYear(year, i + 1);
            for (Appointment a : appointments) {
                if (a.getStatus() == EAppointmentStatus.SCHEDULED) {
                    chartData.setWaiting(chartData.getWaiting() + 1);
                    continue;
                }
                if (a.getStatus() == EAppointmentStatus.CANCELLED) {
                    chartData.setCancelled(chartData.getCancelled() + 1);
                    continue;
                }
                if (a.getStatus() == EAppointmentStatus.NOSHOW) {
                    chartData.setNotshown(chartData.getNotshown() + 1);
                    continue;
                }
                if (a.getStatus() == EAppointmentStatus.CONFIRMED) {
                    chartData.setConfirmed(chartData.getConfirmed() + 1);
                    continue;
                }
                if (a.getStatus() == EAppointmentStatus.COMPLETED) {
                    chartData.setCompleted(chartData.getCompleted() + 1);
                }
            }
            result.add(chartData);
        }
        return result;

    }

    @Override
    public AppointmentStatistic getAppointmentStatistic(int year) {
        AppointmentStatistic result = new AppointmentStatistic(0, 0, 0, 0, 0, 0);
        List<Appointment> appointments = appointmentRepo.getAppointmentsByYear(year);
        for (Appointment a : appointments) {
            if (a.getStatus() == EAppointmentStatus.SCHEDULED) {
                result.setScheduled(result.getScheduled() + 1);
                continue;
            }
            if (a.getStatus() == EAppointmentStatus.CANCELLED) {
                result.setCancelled(result.getCancelled() + 1);
                continue;
            }
            if (a.getStatus() == EAppointmentStatus.NOSHOW) {
                result.setNoshow(result.getNoshow() + 1);
                continue;
            }
            if (a.getStatus() == EAppointmentStatus.CONFIRMED) {
                result.setConfirmed(result.getConfirmed() + 1);
                continue;
            }
            if (a.getStatus() == EAppointmentStatus.COMPLETED) {
                result.setCompleted(result.getCompleted() + 1);
            }
        }
        result.setTotal(appointments.size());
        return result;
    }

    @Override
    public AdminStatistic getAdminStatistic() {
        Date currentDate = Date.valueOf(LocalDate.now());
        List<Invoice> invoices = invoiceRepo.findByInvoiceDateAndPaymentStatus(currentDate.getMonth(),
                currentDate.getYear(), EPaymentStatus.PAID);
        BigDecimal total = new BigDecimal(0L);
        for (Invoice i : invoices) {
            total = total.add(i.getTotalAmount());
        }

        List<StaffSchedule> staffSchedules = staffScheduleRepo.findByScheduleDate(currentDate);
        Set<Integer> staffIds = new HashSet<>();
        for (StaffSchedule s : staffSchedules) {
            if (s.getStaff() != null) {
                staffIds.add(s.getStaff().getStaffId());
            }
        }

        List<Appointment> todayAppointments = appointmentRepo.findByAppointmentDate(currentDate);

        return new AdminStatistic(todayAppointments.size(), staffIds.size(), total);

    }

    @Override
    public List<PatientVisitReportData> getPatientVisitReport(int year) {
        String[] monthName = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        List<PatientVisitReportData> result = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            int month = i + 1;
            PatientVisitReportData reportData = new PatientVisitReportData(monthName[i], 0, 0, 0);

            List<MedicalRecord> monthlyRecords = medicalRecordRepo.findByYearAndMonth(year, month);

            reportData.setTotalVisits(monthlyRecords.size());

            Set<Integer> newPatientIds = new HashSet<>();
            Set<Integer> returningPatientIds = new HashSet<>();

            for (MedicalRecord record : monthlyRecords) {
                int patientId = record.getPatientId();

                // Count total medical records for this patient in entire database
                long totalRecords = medicalRecordRepo.countByPatientId(patientId);

                if (totalRecords == 1) {
                    newPatientIds.add(patientId);
                } else {
                    returningPatientIds.add(patientId);
                }
            }

            reportData.setNewPatients(newPatientIds.size());
            reportData.setReturningPatients(returningPatientIds.size());

            result.add(reportData);
        }
        return result;
    }

    @Override
    public PatientVisitStatistic getPatientVisitStatistic(int year) {
        PatientVisitStatistic statistic = new PatientVisitStatistic(0, 0, 0);

        List<MedicalRecord> yearlyRecords = medicalRecordRepo.findByYear(year);

        statistic.setTotalVisits(yearlyRecords.size());

        Set<Integer> newPatientIds = new HashSet<>();
        Set<Integer> returningPatientIds = new HashSet<>();

        for (MedicalRecord record : yearlyRecords) {
            int patientId = record.getPatientId();

            // Count total medical records for this patient in entire database
            long totalRecords = medicalRecordRepo.countByPatientId(patientId);

            // New patient = only 1 medical record in entire system
            // Returning patient = more than 1 medical record
            if (totalRecords == 1) {
                newPatientIds.add(patientId);
            } else {
                returningPatientIds.add(patientId);
            }
        }

        statistic.setTotalNewPatients(newPatientIds.size());
        statistic.setTotalReturningPatients(returningPatientIds.size());

        return statistic;
    }

    @Override
    public List<PatientVisitDetailResponse> getPatientVisitDetails(int year) {
        List<PatientVisitDetailResponse> result = new ArrayList<>();
        Map<Integer, Integer> patientYearlyVisitCount = new HashMap<>();

        // Get medical records for this year
        List<MedicalRecord> yearlyRecords = medicalRecordRepo.findByYear(year);

        // Count visits for each patient in this year
        for (MedicalRecord record : yearlyRecords) {
            int patientId = record.getPatientId();
            patientYearlyVisitCount.put(patientId, patientYearlyVisitCount.getOrDefault(patientId, 0) + 1);
        }

        // Create response for each patient
        for (Map.Entry<Integer, Integer> entry : patientYearlyVisitCount.entrySet()) {
            int patientId = entry.getKey();
            int yearlyVisitCount = entry.getValue();

            Patient patient = patientRepo.findById(patientId).orElse(null);
            if (patient != null) {
                // Count total medical records for this patient in entire database
                long totalRecords = medicalRecordRepo.countByPatientId(patientId);

                // New patient = only 1 medical record in entire system
                boolean isNew = (totalRecords == 1);

                PatientVisitDetailResponse detail = new PatientVisitDetailResponse(
                        patient.getPatientId(),
                        patient.getFullName(),
                        patient.getFirstVisitDate(),
                        yearlyVisitCount,
                        isNew);
                result.add(detail);
            }
        }

        // Sort by visit count descending
        result.sort((a, b) -> Integer.compare(b.getTotalVisits(), a.getTotalVisits()));

        return result;
    }
}

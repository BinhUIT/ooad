package com.example.ooad.service.appointment.implementation;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EAppointmentStatus;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.domain.enums.EScheduleStatus;
import com.example.ooad.dto.request.AppointmentRequest;
import com.example.ooad.dto.request.BookAppointmentRequest;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.exception.UnauthorizedException;
import com.example.ooad.repository.AppointmentRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.StaffScheduleRepository;
import com.example.ooad.service.appointment.interfaces.AppointmentService;
import com.example.ooad.service.patient.interfaces.PatientService;
import com.example.ooad.utils.Message;

import jakarta.transaction.Transactional;
@Service
public class AppointmentServiceImplementation implements AppointmentService {
    private final AppointmentRepository appointmentRepo;
    
    private final StaffScheduleRepository staffScheduleRepo;
    private final PatientRepository patientRepo;
    private final PatientService patientService;
    public AppointmentServiceImplementation(AppointmentRepository appointmentRepo,
         StaffScheduleRepository staffScheduleRepo, PatientRepository patientRepo, PatientService patientService) {
        this.appointmentRepo = appointmentRepo;
        this.patientRepo = patientRepo;
        this.staffScheduleRepo = staffScheduleRepo;
        this.patientService= patientService;
    }

    @Override
    public Appointment findAppointmentById(int appointmentId) {
        return appointmentRepo.findById(appointmentId).orElseThrow(()-> new NotFoundException(Message.appointmentNotFound));
    }

    @Override
    @Transactional
    public Appointment bookAppointment(BookAppointmentRequest request, Patient patient) {
        StaffSchedule schedule = staffScheduleRepo.findById(request.getScheduleId()).orElseThrow(()->new NotFoundException(Message.scheduleNotFound));
        if(!checkSchedule(schedule)||schedule.getStatus()!=EScheduleStatus.AVAILABLE) {
            throw new BadRequestException(Message.cannotBookAppointment);
        }
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(schedule.getScheduleDate());
        appointment.setAppointmentTime(schedule.getStartTime());
        appointment.setPatient(patient);
        appointment.setStaff(schedule.getStaff());
        appointment.setStatus(EAppointmentStatus.SCHEDULED);
        schedule.setStatus(EScheduleStatus.BOOKED);
        return appointmentRepo.save(appointment);

    }
   
    @Override
    public Page<Appointment> getAppointmentHistory(int pageNumber, int pageSize,Patient patient, Optional<EAppointmentStatus> status, Optional<Date> appointmentDate) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if(status.isPresent()) {
            if(appointmentDate.isPresent()) {
                return appointmentRepo.findByPatient_PatientIdAndAppointmentDateAndStatus(pageable, patient.getPatientId(), status.get(), appointmentDate.get());
            }
            return appointmentRepo.findByPatient_PatientIdAndStatus(pageable, patient.getPatientId(), status.get());
        }
        if(appointmentDate.isPresent()) {
            return appointmentRepo.findByPatient_PatientIdAndAppointmentDate(pageable,patient.getPatientId(), appointmentDate.get());
        }
        return appointmentRepo.findByPatient_PatientId(pageable, patient.getPatientId());
    }

    @Override
    public Appointment patientGetAppointmentById(Patient patient, int appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(()->new NotFoundException(Message.appointmentNotFound));
        if(patient.getPatientId()!=appointment.getPatient().getPatientId()) {
            throw new UnauthorizedException(Message.noPermission);
        }
        return appointment;
    }

    @Override
    public List<StaffSchedule> getScheduleOfDoctor(int doctorId, Date selectedDate) {
        return staffScheduleRepo.findByStaff_StaffIdAndScheduleDateOrderByStartTimeAsc(doctorId,selectedDate);
    }

    @Override
    public Page<Appointment> getAppointmens(int pageNumber, int pageSize, Optional<String> patientName, Optional<EAppointmentStatus> status, Optional<Date> appointmentDate) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        String queryName = patientName.orElse(null);
        EAppointmentStatus queryStatus = status.orElse(null);
        Date queryDate= appointmentDate.orElse(null);
        return appointmentRepo.getAppointments(pageable, queryStatus, queryDate, queryName);
    }

    @Override
    public void endSession() {
        List<Appointment> appointments = appointmentRepo.findByStatus(EAppointmentStatus.SCHEDULED);
        for(Appointment a: appointments) {
            if(isAppointmentExpire(a)){
                a.setStatus(EAppointmentStatus.NOSHOW);
            }
        }
        appointmentRepo.saveAll(appointments);
    }
    private boolean isAppointmentExpire(Appointment appointment) {
        Date currentDate = Date.valueOf(LocalDate.now());
        if(!appointment.getAppointmentDate().before(currentDate)) {
            return false;
        }
        LocalTime current = LocalTime.now();
        LocalTime appointmentMaxTime = appointment.getAppointmentTime().plusHours(1);
        return appointmentMaxTime.isBefore(current);
    }

    @Override
    public Appointment receptionistBookAppointment(AppointmentRequest request) {
        Patient p = patientRepo.findById(request.getPatientId()).orElseThrow(()->new NotFoundException(Message.patientNotFound));
        return bookAppointment(request.getRequest(), p);
    }

    
    @Override
    @Transactional
    public Appointment changeAppointmentStatus(Authentication auth, int appointmentId,EAppointmentStatus status) {
        Appointment appointment = this.findAppointmentById(appointmentId);
        checkAuthority(auth, appointment);
        if(status==EAppointmentStatus.CANCELLED){
            freeSchedule(appointment);
        }
        appointment.setStatus(status);
        return appointmentRepo.save(appointment);
    
}
    private void freeSchedule(Appointment appointment) {
        StaffSchedule schedule = staffScheduleRepo.findByStaff_StaffIdAndStartTimeAndScheduleDate(appointment.getDoctorId(), appointment.getAppointmentTime(), appointment.getAppointmentDate());
        schedule.setStatus(EScheduleStatus.AVAILABLE);
        staffScheduleRepo.save(schedule);
    }
    private void checkAuthority(Authentication auth, Appointment appointment) {
        boolean isReceptionist = auth.getAuthorities().stream().anyMatch(grantedAuthority->{
            
            return grantedAuthority.getAuthority().equals(ERole.RECEPTIONIST.name());
        });
         if(!isReceptionist) {
            Patient p = patientService.getPatientFromAuth(auth);
            if(appointment.getPatient()==null||appointment.getPatient().getPatientId()!=p.getPatientId()){
                throw new BadRequestException(Message.noPermission);
            }
        }
    }
    private boolean checkSchedule(StaffSchedule schedule) {
        if(!schedule.getStaff().getPosition().equalsIgnoreCase("Doctor")) {
            return false;
        }
        
        Date currentDate = Date.valueOf(LocalDate.now());
        if(schedule.getScheduleDate().after(currentDate)) {
            return true;
        } 
        if(schedule.getScheduleDate().before(currentDate)) {
            return false;
        } 
        LocalTime currentTime=LocalTime.now();
        return !schedule.getStartTime().isBefore(currentTime);

    }

    @Override
    @Transactional
    public Appointment editAppointment(Authentication auth, int appointmentId, AppointmentRequest request) {
         Appointment appointment = this.findAppointmentById(appointmentId);
         if(appointment.getStatus()!=EAppointmentStatus.SCHEDULED){
            throw new BadRequestException(Message.cannotEdit);
         }
        checkAuthority(auth, appointment);
        Patient p= patientRepo.findById(request.getPatientId()).orElseThrow(()->new NotFoundException(Message.patientNotFound));
         StaffSchedule schedule = staffScheduleRepo.findById(request.getScheduleId()).orElseThrow(()->new NotFoundException(Message.scheduleNotFound));
        if(!checkSchedule(schedule)) {
            throw new BadRequestException(Message.cannotBookAppointment);
        }
        freeSchedule(appointment);
        appointment.setAppointmentDate(schedule.getScheduleDate());
        appointment.setPatient(p);
        appointment.setAppointmentTime(schedule.getStartTime());
        appointment.setStaff(schedule.getStaff());
        schedule.setStatus(EScheduleStatus.BOOKED);
        staffScheduleRepo.save(schedule);
        return appointmentRepo.save(appointment);
    }

    @Override
    public Integer getScheduleId(int appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        StaffSchedule staffSchedule = staffScheduleRepo.findByStaff_StaffIdAndStartTimeAndScheduleDate(appointment.getDoctorId(), appointment.getAppointmentTime(), appointment.getAppointmentDate());
        if(staffSchedule==null) return 0;
        return staffSchedule.getScheduleId();
    }
}

package com.example.ooad.service.reception.implementation;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.dto.request.CreateReceptionRequest;
import com.example.ooad.dto.request.UpdateReceptionRequest;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.ReceptionRepository;
import com.example.ooad.service.reception.interfaces.ReceptionService;
import com.example.ooad.utils.Message;

@Service
public class ReceptionServiceImplementation implements ReceptionService {
    private final ReceptionRepository receptionRepo;

    private final PatientRepository patientRepo;
    

    public ReceptionServiceImplementation(ReceptionRepository receptionRepo, PatientRepository patientRepo) {
        this.receptionRepo = receptionRepo;

        this.patientRepo = patientRepo;
    }

    @Override
    public Page<Reception> getListReceptions(int pageNumber, int pageSize, Optional<EReceptionStatus> status, Optional<Date> date, Optional<String> patientName) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        EReceptionStatus filterStatus = status.orElse(null);
        Date filterDate = date.orElse(null);
        String name = patientName.orElse(null);
        return receptionRepo.filterReception(pageable, filterStatus, filterDate, name);
    }

    @Override
    public Reception getReceptionById(int receptionId) {
        return receptionRepo.findById(receptionId).orElseThrow(() -> new NotFoundException(Message.receptionNotFound));
    }

    @Override
    public Reception editReception(UpdateReceptionRequest request) {
        Reception reception = this.getReceptionById(request.getReceptionId());
        if(reception.getStatus()==EReceptionStatus.DONE||reception.getStatus()==EReceptionStatus.CANCELLED) {
            throw new BadRequestException(Message.cannotEditReception);
        }
        Date currentDate = Date.valueOf(LocalDate.now());
        if (currentDate.after(reception.getReceptionDate())) {
            throw new BadRequestException(Message.cannotEditReception);
        }
        reception.setStatus(request.getNewStatus());
        return receptionRepo.save(reception);

    }

    @Override
    public Reception createReception(CreateReceptionRequest request, Staff receptionist) {
        Patient patient = patientRepo.findById(request.getPatientId())
                .orElseThrow(() -> new NotFoundException(Message.patientNotFound));
        if (receptionist.getAccount() == null || receptionist.getAccount().getRole() != ERole.RECEPTIONIST) {
            throw new BadRequestException(Message.cannotCreateReception);
        }
        Date currentDate = Date.valueOf(LocalDate.now());
        if (request.getReceptionDate().before(currentDate)) {
            throw new BadRequestException(Message.invalidReceptionDate);
        }
        Reception reception = new Reception();
        reception.setPatient(patient);
        reception.setReceptionDate(request.getReceptionDate());
        reception.setReceptionist(receptionist);
        reception.setStatus(EReceptionStatus.WAITING);
        return receptionRepo.save(reception);
    }
    @Override
    public void endSession() {
        List<Reception> receptions = receptionRepo.findByReceptionDateLessThanEqual(Date.valueOf(LocalDate.now()));
        for(Reception r:receptions) {
            if(r.getStatus()==EReceptionStatus.WAITING) {
                r.setStatus(EReceptionStatus.CANCELLED);
            }
            if(r.getStatus()==EReceptionStatus.IN_EXAMINATION) {
                r.setStatus(EReceptionStatus.DONE);
            }
        }
        receptionRepo.saveAll(receptions);
    }
   

}

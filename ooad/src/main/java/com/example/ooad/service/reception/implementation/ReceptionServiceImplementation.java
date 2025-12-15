package com.example.ooad.service.reception.implementation;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.dto.request.CreateReceptionRequest;
import com.example.ooad.dto.request.CreateReceptionRequest;
import com.example.ooad.dto.request.UpdateReceptionRequest;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.ReceptionRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.service.reception.interfaces.ReceptionService;
import com.example.ooad.utils.Message;

@Service
public class ReceptionServiceImplementation implements ReceptionService {
    private final ReceptionRepository receptionRepo;

    private final PatientRepository patientRepo;
    private final StaffRepository staffRepo;

    public ReceptionServiceImplementation(ReceptionRepository receptionRepo, PatientRepository patientRepo,
            StaffRepository staffRepo) {
        this.receptionRepo = receptionRepo;

        this.patientRepo = patientRepo;
        this.staffRepo = staffRepo;
    }

    @Override
    public Page<Reception> getListReceptions(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return receptionRepo.findAllByOrderByReceptionDateDesc(pageable);
    }

    @Override
    public Reception getReceptionById(int receptionId) {
        return receptionRepo.findById(receptionId).orElseThrow(() -> new NotFoundException(Message.receptionNotFound));
    }

    @Override
    public Reception editReception(UpdateReceptionRequest request) {
        Reception reception = this.getReceptionById(request.getReceptionId());
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

}

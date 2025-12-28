
package com.example.ooad.service.reception.interfaces;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.dto.request.CreateReceptionRequest;
import com.example.ooad.dto.request.UpdateReceptionRequest;

public interface ReceptionService {
    public Page<Reception> getListReceptions(int pageNumber, int pageSize, Optional<EReceptionStatus> status, Optional<Date> date, Optional<String> patientName);
    public Reception getReceptionById(int receptionId);
    public Reception editReception(UpdateReceptionRequest request);
    public Reception createReception(CreateReceptionRequest request, Staff receptionist);
    public void endSession();
}

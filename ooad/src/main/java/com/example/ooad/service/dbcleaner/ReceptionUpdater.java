package com.example.ooad.service.dbcleaner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.repository.ReceptionRepository;

@Component
public class ReceptionUpdater {

   
    private final ReceptionRepository receptionRepo;
    private static final long updateInterval=1000*60*5;
    public ReceptionUpdater(ReceptionRepository receptionRepo) {
        this.receptionRepo = receptionRepo;
    } 
    @Scheduled(fixedRate=updateInterval) 
    public void updateReceptionStatus() {
        List<Reception> receptions = receptionRepo.findByReceptionDateLessThan(Date.valueOf(LocalDate.now()));
        for(Reception r:receptions) {
            if(r.getStatus()==EReceptionStatus.WAITING) {
                r.setStatus(EReceptionStatus.CANCELLED);
            }
            if(r.getStatus()==EReceptionStatus.IN_EXAMINATION) {
                r.setStatus(EReceptionStatus.DONE);
            }
        }
        System.out.println("Call");
        receptionRepo.saveAll(receptions);
    }
    

}

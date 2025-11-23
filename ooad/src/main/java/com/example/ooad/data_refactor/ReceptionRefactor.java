package com.example.ooad.data_refactor;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.repository.ReceptionRepository;

//@Component
public class ReceptionRefactor {
    private final ReceptionRepository receptionRepo;

    public ReceptionRefactor(ReceptionRepository receptionRepo) {
        this.receptionRepo = receptionRepo;
        List<Reception> listReceptions = receptionRepo.findAll();
        Random rand = new Random();
        for(Reception reception : listReceptions) {
            int num= rand.nextInt(4);
            reception.setStatus(getReceptionStatusFromInt(num));
        }
        receptionRepo.saveAll(listReceptions);

    }
    private EReceptionStatus getReceptionStatusFromInt(int num) {
        switch(num){
            case 0 -> {
                return EReceptionStatus.DONE;
            }
            case 1 -> {
                return EReceptionStatus.IN_EXAMINATION;
            }
            case 2 -> {
                return EReceptionStatus.WAITING;
            }
            case 3 -> {
                return EReceptionStatus.CANCELLED;
            }
            default -> {
                return null;
            }
        }
    }
}

package com.example.ooad.domain.entity;

import java.sql.Date;

import com.example.ooad.domain.enums.EGender;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff extends Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int staffId;
    /**
     * @deprecated The `position` field is deprecated. Use `Account.role` (ERole)
     *             for
     *             authorization and role checks. This field remains in the database
     *             and entity for
     *             backward compatibility. TODO: remove this column and field after
     *             consumers are
     *             migrated and a DB migration is scheduled.
     */
    @Deprecated
    private String position;

    public Staff(String position, int staffId, String fullName, Date dateOfBirth, EGender gender, String phone,
            String email, String idCard, Account account) {
        super(fullName, dateOfBirth, gender, phone, email, idCard, account);
        this.position = position;
        this.staffId = staffId;
    }

    public Staff() {
        super();
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}

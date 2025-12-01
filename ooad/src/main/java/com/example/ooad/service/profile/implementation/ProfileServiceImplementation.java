package com.example.ooad.service.profile.implementation;

import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.dto.request.UpdateProfileRequest;
import com.example.ooad.dto.response.ProfileResponse;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.service.profile.interfaces.ProfileService;

@Service
public class ProfileServiceImplementation implements ProfileService {
    
    private final AccountRepository accountRepository;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;
    
    public ProfileServiceImplementation(
            AccountRepository accountRepository,
            PatientRepository patientRepository,
            StaffRepository staffRepository) {
        this.accountRepository = accountRepository;
        this.patientRepository = patientRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public ProfileResponse getMyProfile(String username) {
        // 1. Tìm Account từ username (lấy từ JWT Token - an toàn tuyệt đối)
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new NotFoundException("Không tìm thấy tài khoản");
        }
        
        // 2. Xây dựng response dựa trên role
        ProfileResponse response = new ProfileResponse();
        response.setUsername(account.getUsername());
        response.setRole(account.getRole());
        
        // 3. Lấy thông tin chi tiết từ Patient hoặc Staff tùy theo role
        if (account.getRole() == ERole.PATIENT) {
            Patient patient = patientRepository.findByAccountId(account.getAccountId())
                    .orElse(null);
            if (patient != null) {
                mapPatientToResponse(patient, response);
            }
        } else {
            // Staff (DOCTOR, RECEPTIONIST, WAREHOUSE_STAFF, ADMIN, etc.)
            Staff staff = staffRepository.findByAccountId(account.getAccountId())
                    .orElse(null);
            if (staff != null) {
                mapStaffToResponse(staff, response);
            }
        }
        
        return response;
    }

    @Override
    public ProfileResponse updateMyProfile(String username, UpdateProfileRequest request) {
        // 1. Tìm Account từ username (lấy từ JWT Token - an toàn tuyệt đối)
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new NotFoundException("Không tìm thấy tài khoản");
        }
        
        // 2. Cập nhật thông tin dựa trên role
        if (account.getRole() == ERole.PATIENT) {
            Patient patient = patientRepository.findByAccountId(account.getAccountId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin bệnh nhân"));
            
            updatePatientFromRequest(patient, request);
            patientRepository.save(patient);
        } else {
            // Staff (DOCTOR, RECEPTIONIST, WAREHOUSE_STAFF, ADMIN, etc.)
            Staff staff = staffRepository.findByAccountId(account.getAccountId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin nhân viên"));
            
            updateStaffFromRequest(staff, request);
            staffRepository.save(staff);
        }
        
        // 3. Trả về thông tin sau khi cập nhật
        return getMyProfile(username);
    }
    
    /**
     * Map thông tin từ Patient entity sang ProfileResponse
     */
    private void mapPatientToResponse(Patient patient, ProfileResponse response) {
        response.setFullName(patient.getFullName());
        response.setDateOfBirth(patient.getDateOfBirth());
        response.setGender(patient.getGender());
        response.setPhone(patient.getPhone());
        response.setEmail(patient.getEmail());
        response.setIdCard(patient.getIdCard());
        response.setAddress(patient.getAddress());
        response.setFirstVisitDate(patient.getFirstVisitDate());
    }
    
    /**
     * Map thông tin từ Staff entity sang ProfileResponse
     */
    private void mapStaffToResponse(Staff staff, ProfileResponse response) {
        response.setFullName(staff.getFullName());
        response.setDateOfBirth(staff.getDateOfBirth());
        response.setGender(staff.getGender());
        response.setPhone(staff.getPhone());
        response.setEmail(staff.getEmail());
        response.setIdCard(staff.getIdCard());
        response.setPosition(staff.getPosition());
    }
    
    /**
     * Cập nhật Patient từ request (chỉ cập nhật các trường được phép)
     */
    private void updatePatientFromRequest(Patient patient, UpdateProfileRequest request) {
        if (request.getFullName() != null) {
            patient.setFullName(request.getFullName());
        }
        if (request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            patient.setGender(request.getGender());
        }
        if (request.getPhone() != null) {
            patient.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            patient.setEmail(request.getEmail());
        }
        if (request.getIdCard() != null) {
            patient.setIdCard(request.getIdCard());
        }
        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }
    }
    
    /**
     * Cập nhật Staff từ request (chỉ cập nhật các trường được phép)
     * Lưu ý: Staff không được tự đổi position (chức vụ)
     */
    private void updateStaffFromRequest(Staff staff, UpdateProfileRequest request) {
        if (request.getFullName() != null) {
            staff.setFullName(request.getFullName());
        }
        if (request.getDateOfBirth() != null) {
            staff.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            staff.setGender(request.getGender());
        }
        if (request.getPhone() != null) {
            staff.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            staff.setEmail(request.getEmail());
        }
        if (request.getIdCard() != null) {
            staff.setIdCard(request.getIdCard());
        }
        // KHÔNG cho phép đổi position - đây là trường nhạy cảm
    }
}

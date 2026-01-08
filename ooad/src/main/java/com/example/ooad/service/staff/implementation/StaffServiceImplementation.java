package com.example.ooad.service.staff.implementation;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.ooad.service.email.interfaces.EmailService;
import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EScheduleStatus;
import com.example.ooad.domain.enums.EStatus;
import com.example.ooad.dto.request.CreateAccountDto;
import com.example.ooad.dto.request.StaffFilterRequest;
import com.example.ooad.dto.request.StaffRequest;
import com.example.ooad.dto.response.StaffResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.repository.StaffScheduleRepository;
import com.example.ooad.service.auth.interfaces.AuthService;
import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.DateTimeUtil;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.ActorValidator;

import jakarta.transaction.Transactional;

@Service
public class StaffServiceImplementation implements StaffService {
    private final StaffRepository staffRepo;
    private final StaffScheduleRepository staffScheduleRepo;
    private final AccountRepository accountRepo;
    private final ActorValidator actorValidator;
    private final AuthService authService;
    private final EmailService emailService;

    public StaffServiceImplementation(
            StaffRepository staffRepo,
            StaffScheduleRepository staffScheduleRepo,
            AccountRepository accountRepo,
            ActorValidator actorValidator,
            AuthService authService,
            EmailService emailService) {
        this.staffRepo = staffRepo;
        this.staffScheduleRepo = staffScheduleRepo;
        this.accountRepo = accountRepo;
        this.actorValidator = actorValidator;
        this.authService = authService;
        this.emailService = emailService;
    }

    @Override
    public Staff findStaffById(int staffId) {
        return staffRepo.findById(staffId).orElseThrow(() -> new NotFoundException(Message.staffNotFound));
    }

    @Override
    public boolean isStaffFree(Date scheduleDate, LocalTime startTime, LocalTime endTime, int staffId) {
        List<StaffSchedule> listSchedules = staffScheduleRepo
                .findByStaff_StaffIdAndScheduleDateAndStatusNot(staffId, scheduleDate, EScheduleStatus.CANCELLED);
        for (StaffSchedule staffSchedule : listSchedules) {
            if (DateTimeUtil.isTwoScheduleOverlap(startTime, endTime, staffSchedule.getStartTime(),
                    staffSchedule.getEndTime())) {
                return false;
            }
        }
        return true;
    }

    private void validateRequest(StaffRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (!actorValidator.validateEmail(request.getEmail())) {
            throw new BadRequestException(Message.invalidEmail);
        }
        if (!actorValidator.validatePhoneNumber(request.getPhone())) {
            throw new BadRequestException(Message.invalidPhone);
        }
        if (!actorValidator.validateIdCard(request.getIdCard())) {
            throw new BadRequestException(Message.invalidIdCard);
        }
    }

    @Override
    @Transactional
    public StaffResponse createStaff(StaffRequest request, BindingResult bindingResult) {
        validateRequest(request, bindingResult);

        List<Staff> existingStaffs = staffRepo.findAll();
        for (Staff s : existingStaffs) {
            if (s.getEmail().equalsIgnoreCase(request.getEmail())) {
                throw new ConflictException("Staff with email '" + request.getEmail() + "' already exists");
            }
            if (s.getIdCard().equals(request.getIdCard())) {
                throw new ConflictException("Staff with ID card '" + request.getIdCard() + "' already exists");
            }
        }

        Staff staff = new Staff();
        staff.setFullName(request.getFullName());
        staff.setDateOfBirth(request.getDateOfBirth());
        staff.setGender(request.getGender());
        staff.setEmail(request.getEmail());
        staff.setPhone(request.getPhone());
        staff.setIdCard(request.getIdCard());
        staff.setPosition(request.getPosition());
        staff.setAccount(null);
        staff = staffRepo.save(staff);

        // Send email with staff ID for account registration
        try {
            emailService.sendStaffRegistrationEmail(staff.getStaffId(), request.getEmail(), request.getFullName());
        } catch (Exception e) {
            System.err.println("Failed to send registration email: " + e.getMessage());
        }

        return new StaffResponse(staff);
    }

    @Override
    @Transactional
    public StaffResponse updateStaff(int id, StaffRequest request, BindingResult bindingResult) {
        validateRequest(request, bindingResult);

        Staff staff = staffRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Staff not found with id: " + id));

        // Check duplicate email/idCard (exclude current staff)
        List<Staff> existingStaffs = staffRepo.findAll();
        for (Staff s : existingStaffs) {
            if (s.getStaffId() != id) {
                if (s.getEmail().equalsIgnoreCase(request.getEmail())) {
                    throw new ConflictException("Staff with email '" + request.getEmail() + "' already exists");
                }
                if (s.getIdCard().equals(request.getIdCard())) {
                    throw new ConflictException("Staff with ID card '" + request.getIdCard() + "' already exists");
                }
            }
        }

        // Ensure username not used by other accounts (if staff has account)
        Account existingAccountWithUsername = accountRepo.findByUsername(request.getEmail());
        Account currentAccount = staff.getAccount();
        if (existingAccountWithUsername != null && currentAccount != null
                && existingAccountWithUsername.getAccountId() != currentAccount.getAccountId()) {
            throw new ConflictException("Account username '" + request.getEmail() + "' already exists");
        }

        // Update Staff fields
        staff.setFullName(request.getFullName());
        staff.setDateOfBirth(request.getDateOfBirth());
        staff.setGender(request.getGender());
        staff.setEmail(request.getEmail());
        staff.setPhone(request.getPhone());
        staff.setIdCard(request.getIdCard());
        staff.setPosition(request.getPosition());

        // Update Account only if staff has one
        if (currentAccount != null) {
            currentAccount.setUsername(request.getEmail());
            currentAccount.setRole(request.getRole());
            currentAccount.setStatus(request.getIsActive() ? EStatus.ACTIVE : EStatus.LOCKED);
            accountRepo.save(currentAccount);
        }

        staff = staffRepo.save(staff);

        return new StaffResponse(staff);
    }

    @Override
    public StaffResponse getStaffById(int id) {
        Staff staff = staffRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Staff not found with id: " + id));
        return new StaffResponse(staff);
    }

    @Override
    public Page<StaffResponse> searchStaffs(StaffFilterRequest filter) {
        Sort sort = Sort.by(
                filter.getSortType() != null && filter.getSortType().equalsIgnoreCase("DESC")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                filter.getSortBy() != null ? filter.getSortBy() : "staffId");

        Pageable pageable = PageRequest.of(
                filter.getPage() != null ? filter.getPage() : 0,
                filter.getSize() != null ? filter.getSize() : 10,
                sort);

        Specification<Staff> spec = (root, query, cb) -> cb.conjunction();

        // Filter by search keyword
        if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty()) {
            String keyword = "%" + filter.getSearch().trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("fullName")), keyword),
                    cb.like(cb.lower(root.get("email")), keyword),
                    cb.like(cb.lower(root.get("phone")), keyword),
                    cb.like(cb.lower(root.get("idCard")), keyword),
                    cb.like(cb.lower(root.get("position")), keyword)));
        }

        // Filter by role
        if (filter.getRole() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("account").get("role"), filter.getRole()));
        }

        // Filter by active status
        if (filter.getActive() != null) {
            EStatus status = filter.getActive() ? EStatus.ACTIVE : EStatus.LOCKED;
            spec = spec.and((root, query, cb) -> cb.equal(root.get("account").get("status"), status));
        }

        Page<Staff> page = staffRepo.findAll(spec, pageable);

        return page.map(StaffResponse::new);
    }

    @Override
    @Transactional
    public void deleteStaff(int id) {
        Staff staff = staffRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Staff not found with id: " + id));

        Account account = staff.getAccount();

        // Delete staff first (due to foreign key constraint)
        staffRepo.delete(staff);

        // Then delete account
        if (account != null) {
            accountRepo.delete(account);
        }
    }

    @Override
    public List<Staff> findStaffByRole(String role) {
        return staffRepo.findByPositionIgnoreCase(role);
    }

    @Override
    public Staff getStaffFromAuth(Authentication auth) {
        Account account = authService.getAccountFromAuth(auth);
        if (account == null) {
            throw new BadRequestException("Bad credential");
        }
        return staffRepo.findByAccountId(account.getAccountId())
                .orElseThrow(() -> new NotFoundException("Staff not found"));

    }
}

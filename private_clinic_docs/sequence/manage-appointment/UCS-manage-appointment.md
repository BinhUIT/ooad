# ĐẶC TẢ CÁC USE CASE - MANAGE APPOINTMENT

Tài liệu này mô tả các use case thuộc nhóm Quản lý Lịch hẹn.

Gồm 5 use case chính:

1. View and Filter Appointments (Xem và lọc lịch hẹn)
2. View Appointment Details (Xem chi tiết lịch hẹn)
3. Add New Appointment (Tạo lịch hẹn mới)
4. Edit Appointment (Chỉnh sửa lịch hẹn)
5. Delete/Cancel Appointment (Hủy lịch hẹn)

---

## UC_APT_01: View and Filter Appointments

### Tác nhân chính

- Receptionist, Doctor, Patient

### Luồng chính

1. Truy cập danh sách appointments
2. Hệ thống hiển thị danh sách với thông tin patient, doctor, date, time, status
3. Áp dụng filter (date, status, doctor)
4. Hiển thị kết quả

---

## UC_APT_02: View Appointment Details

### Tác nhân chính

- Receptionist, Doctor, Patient

### Luồng chính

1. Click vào appointment
2. Hệ thống hiển thị: thông tin appointment, patient info, doctor info

---

## UC_APT_03: Add New Appointment

### Tác nhân chính

- Receptionist, Patient

### Luồng chính

1. Click "Book Appointment"
2. Hệ thống hiển thị available time slots (từ StaffSchedule)
3. Chọn patient, doctor, date, time
4. Hệ thống kiểm tra:
   - Time slot available
   - Daily limit chưa đầy (max TBD patients/day)
5. Tạo Appointment
6. Gửi email xác nhận

### Luồng phụ

- Time slot not available: hiển thị lỗi
- Daily limit reached: hiển thị lỗi

---

## UC_APT_04: Edit Appointment

### Tác nhân chính

- Receptionist

### Luồng chính

1. Click "Edit" trên appointment
2. Hiển thị available time slots mới
3. Cập nhật thông tin (date, time, doctor, status)
4. Hệ thống kiểm tra new time slot available
5. Cập nhật Appointment

---

## UC_APT_05: Delete/Cancel Appointment

### Tác nhân chính

- Receptionist, Patient

### Luồng chính

1. Click "Cancel"
2. Confirm cancellation
3. Hệ thống update status to "Cancelled" hoặc delete
4. Gửi email thông báo

### Luồng phụ

- Appointment đã completed/in-progress: không cho phép cancel

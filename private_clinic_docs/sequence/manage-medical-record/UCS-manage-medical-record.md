# ĐẶC TẢ CÁC USE CASE - MANAGE MEDICAL RECORD

Tài liệu này mô tả các use case thuộc nhóm Quản lý Hồ sơ khám bệnh.

Gồm 3 use case chính:

1. View Medical Records (Xem danh sách hồ sơ khám)
2. Add Medical Record (Tạo hồ sơ khám mới)
3. Edit Medical Record (Chỉnh sửa hồ sơ khám)

---

## UC_MR_01: View Medical Records

### Tác nhân chính

- Doctor

### Luồng chính

1. Truy cập danh sách medical records
2. Hệ thống hiển thị records với patient info
3. Click vào record để xem chi tiết
4. Hiển thị: symptoms, diagnosis, prescription, services

---

## UC_MR_02: Add Medical Record

### Tác nhân chính

- Doctor

### Luồng chính

1. Chọn Reception để khám (từ danh sách receptions trong ngày)
2. Hệ thống hiển thị form khám bệnh với patient info
3. Doctor nhập:
   - Symptoms (triệu chứng)
   - Diagnosis (chẩn đoán)
   - Disease Type
   - Services cần thực hiện (X-ray, lab test, etc.)
4. Hệ thống:
   - Tạo MedicalRecord
   - Update Reception status to "In Examination" hoặc "Done"
   - Tạo Invoice với default values
5. Hiển thị thành công, có thể tiếp tục tạo Prescription

---

## UC_MR_03: Edit Medical Record

### Tác nhân chính

- Doctor

### Điều kiện

- Chỉ được sửa trong thời gian nhất định (vd: trong ngày)

### Luồng chính

1. Click "Edit" trên medical record
2. Cập nhật thông tin
3. Hệ thống lưu thay đổi

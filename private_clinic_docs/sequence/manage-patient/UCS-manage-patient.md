# ĐẶC TẢ CÁC USE CASE - PATIENT MANAGEMENT

Tài liệu này mô tả các use case thuộc nhóm quản lý Bệnh nhân (Patient Management) dành cho Nhân viên/Quản trị trong hệ thống Private Clinic Management System.

Gồm 5 use case chính:

1. View and Filter Patients (Xem và lọc bệnh nhân)
2. View Patient Details (Xem chi tiết bệnh nhân)
3. Add New Patient (Thêm bệnh nhân mới)
4. Edit Patient (Chỉnh sửa thông tin bệnh nhân)
5. Delete Patient (Xóa bệnh nhân)

---

## UC_PATIENT_01: View and Filter Patients (Xem và lọc bệnh nhân)

### Mô tả

Nhân viên xem danh sách bệnh nhân và lọc theo nhiều tiêu chí.

### Tác nhân chính

- Receptionist (Lễ tân)
- Doctor (Bác sĩ)
- Admin (Quản trị viên)

### Điều kiện tiên quyết

- Người dùng đã đăng nhập với vai trò Staff hoặc Admin

### Điều kiện hậu

- Hiển thị danh sách bệnh nhân theo tiêu chí lọc với phân trang

### Luồng sự kiện chính

| Bước | Staff                      | Hệ thống                                                                                                                                                                                                                                                                                                                                         |
| ---- | -------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 1    | Chọn "Quản lý bệnh nhân"   |                                                                                                                                                                                                                                                                                                                                                  |
| 2    |                            | Query danh sách bệnh nhân:<br>`SELECT p.*, COUNT(a.id) as appointment_count, COUNT(mr.id) as record_count`<br>`FROM Patient p`<br>`LEFT JOIN Appointment a ON p.patientID = a.patientID`<br>`LEFT JOIN MedicalRecord mr ON p.patientID = mr.patientID`<br>`GROUP BY p.patientID`<br>`ORDER BY p.patientID DESC`<br>`LIMIT :limit OFFSET :offset` |
| 3    |                            | Hiển thị danh sách với các cột:<br>- PatientID<br>- Full name<br>- Phone<br>- Email<br>- DOB<br>- Số lượng appointments<br>- Số lượng medical records<br>- Nút hành động                                                                                                                                                                         |
| 4    | Nhập tiêu chí lọc/tìm kiếm |                                                                                                                                                                                                                                                                                                                                                  |
| 5    | Click "Áp dụng"            |                                                                                                                                                                                                                                                                                                                                                  |
| 6    |                            | Query với điều kiện WHERE:<br>- Tên (LIKE '%keyword%')<br>- Số điện thoại (LIKE hoặc =)<br>- Email (LIKE hoặc =)<br>- Độ tuổi<br>- Giới tính                                                                                                                                                                                                     |
| 7    |                            | Hiển thị kết quả lọc                                                                                                                                                                                                                                                                                                                             |
| 8    | Xem kết quả                |                                                                                                                                                                                                                                                                                                                                                  |

### Luồng sự kiện phụ

**2a. Không có bệnh nhân:**

- 2a.1. Hiển thị: "Chưa có bệnh nhân trong hệ thống"

**6a. Không tìm thấy kết quả:**

- 6a.1. Hiển thị: "Không tìm thấy bệnh nhân phù hợp"
- 6a.2. Cho phép xóa bộ lọc

### Ràng buộc nghiệp vụ

- Phân trang: 20-50 records/page
- Index trên: phone_number, email, full_name
- Hỗ trợ export danh sách ra Excel/PDF

---

## UC_PATIENT_02: View Patient Details (Xem chi tiết bệnh nhân)

### Mô tả

Xem đầy đủ thông tin bệnh nhân bao gồm lịch sử khám, đơn thuốc, hóa đơn.

### Tác nhân chính

- Receptionist
- Doctor
- Pharmacist
- Admin

### Điều kiện tiên quyết

- Bệnh nhân tồn tại trong hệ thống

### Điều kiện hậu

- Hiển thị chi tiết bệnh nhân với các tab thông tin

### Luồng sự kiện chính

| Bước | Staff                               | Hệ thống                                                                                                                                                                                                                                                                                                                                                                          |
| ---- | ----------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1    | Click "Xem chi tiết" trên bệnh nhân |                                                                                                                                                                                                                                                                                                                                                                                   |
| 2    |                                     | Query thông tin bệnh nhân:<br>`SELECT * FROM Patient WHERE patientID = :id`                                                                                                                                                                                                                                                                                                       |
| 3    |                                     | Query danh sách appointments:<br>`SELECT a.*, s.full_name as doctor_name, ss.date, ss.start_time, ss.end_time`<br>`FROM Appointment a`<br>`JOIN Staff s ON a.doctorID = s.staffID`<br>`JOIN StaffSchedule ss ON a.scheduleID = ss.scheduleID`<br>`WHERE a.patientID = :id`<br>`ORDER BY ss.date DESC, ss.start_time DESC`                                                         |
| 4    |                                     | Query medical records:<br>`SELECT mr.*, s.full_name as doctor_name, dt.name as disease_name, p.total_cost`<br>`FROM MedicalRecord mr`<br>`JOIN Staff s ON mr.doctorID = s.staffID`<br>`JOIN DiseaseType dt ON mr.diseaseTypeID = dt.diseaseTypeID`<br>`LEFT JOIN Prescription p ON mr.recordID = p.recordID`<br>`WHERE mr.patientID = :id`<br>`ORDER BY mr.examination_date DESC` |
| 5    |                                     | Query invoices:<br>`SELECT i.*, mp.name as payment_method`<br>`FROM Invoice i`<br>`JOIN MethodPay mp ON i.methodPayID = mp.methodPayID`<br>`WHERE i.patientID = :id`<br>`ORDER BY i.invoice_date DESC`                                                                                                                                                                            |
| 6    |                                     | Hiển thị trang chi tiết với tabs:<br>- **Tab 1**: Thông tin cá nhân<br>- **Tab 2**: Danh sách appointments<br>- **Tab 3**: Medical records<br>- **Tab 4**: Invoices<br>- Nút: Chỉnh sửa, Xóa                                                                                                                                                                                      |
| 7    | Xem thông tin                       |                                                                                                                                                                                                                                                                                                                                                                                   |

### Luồng sự kiện phụ

**2a. Bệnh nhân không tồn tại:**

- 2a.1. Hiển thị: "Không tìm thấy bệnh nhân"
- 2a.2. Kết thúc use case

### Ràng buộc nghiệp vụ

- Chỉ hiển thị 10 records gần nhất cho mỗi tab
- Có nút "Xem tất cả" để xem đầy đủ
- Medical records và Prescriptions phải được bảo mật

---

## UC_PATIENT_03: Add New Patient (Thêm bệnh nhân mới)

### Mô tả

Thêm thông tin bệnh nhân mới vào hệ thống.

### Tác nhân chính

- Receptionist
- Admin

### Điều kiện tiên quyết

- Người dùng có quyền tạo bệnh nhân

### Điều kiện hậu

- Bệnh nhân mới được tạo trong hệ thống
- Có thể tạo account cho bệnh nhân (optional)

### Luồng sự kiện chính

| Bước | Staff                           | Hệ thống                                                                                                                                                                                                                                                                                                                 |
| ---- | ------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 1    | Click "Thêm bệnh nhân mới"      |                                                                                                                                                                                                                                                                                                                          |
| 2    |                                 | Hiển thị form với các trường:<br>- Full name (_)<br>- Phone (_)<br>- Email<br>- Date of Birth (_)<br>- Gender (_)<br>- Address<br>- Checkbox: Tạo tài khoản đăng nhập                                                                                                                                                    |
| 3    | Nhập thông tin bệnh nhân        |                                                                                                                                                                                                                                                                                                                          |
| 4    | Click "Lưu"                     |                                                                                                                                                                                                                                                                                                                          |
| 5    |                                 | Validate dữ liệu                                                                                                                                                                                                                                                                                                         |
| 6    |                                 | Kiểm tra trùng lặp:<br>`SELECT COUNT(*) FROM Patient WHERE phone = :phone OR email = :email`                                                                                                                                                                                                                             |
| 7    |                                 | Tạo patient:<br>`INSERT INTO Patient (accountID, full_name, phone, email, DOB, gender, address)`<br>`VALUES (NULL, ...) RETURNING patientID`                                                                                                                                                                             |
| 8    |                                 | Nếu checkbox "Tạo tài khoản" được chọn:<br>- Sinh username tự động (từ phone hoặc email)<br>- Sinh password ngẫu nhiên<br>- Tạo Account:<br>`INSERT INTO Account (username, password, role)`<br>`VALUES (:username, :hashed_password, 'PATIENT')`<br>- Cập nhật accountID cho Patient<br>- Gửi email thông tin đăng nhập |
| 9    |                                 | Hiển thị success và chuyển đến trang chi tiết bệnh nhân                                                                                                                                                                                                                                                                  |
| 10   | Xem thông tin bệnh nhân vừa tạo |                                                                                                                                                                                                                                                                                                                          |

### Luồng sự kiện phụ

**5a. Dữ liệu không hợp lệ:**

- 5a.1. Hiển thị lỗi chi tiết
- 5a.2. Quay lại bước 2

**6a. Phone hoặc email đã tồn tại:**

- 6a.1. Hiển thị: "Bệnh nhân với số điện thoại/email này đã tồn tại"
- 6a.2. Hỏi: "Bạn có muốn xem thông tin bệnh nhân này?"
- 6a.3. Quay lại bước 2

### Ràng buộc nghiệp vụ

- Phone và Email phải unique
- Phone: 10-11 chữ số
- Email: đúng format
- DOB: phải trong quá khứ
- Nếu tạo account, gửi thông tin qua email hoặc SMS

---

## UC_PATIENT_04: Edit Patient (Chỉnh sửa bệnh nhân)

### Mô tả

Cập nhật thông tin bệnh nhân đã có trong hệ thống.

### Tác nhân chính

- Receptionist
- Admin

### Điều kiện tiên quyết

- Bệnh nhân tồn tại

### Điều kiện hậu

- Thông tin bệnh nhân được cập nhật

### Luồng sự kiện chính

| Bước | Staff                            | Hệ thống                                                                                            |
| ---- | -------------------------------- | --------------------------------------------------------------------------------------------------- |
| 1    | Click "Chỉnh sửa" trên bệnh nhân |                                                                                                     |
| 2    |                                  | Query thông tin hiện tại:<br>`SELECT * FROM Patient WHERE patientID = :id`                          |
| 3    |                                  | Hiển thị form với dữ liệu hiện tại                                                                  |
| 4    | Cập nhật thông tin               |                                                                                                     |
| 5    | Click "Lưu"                      |                                                                                                     |
| 6    |                                  | Validate dữ liệu mới                                                                                |
| 7    |                                  | Cập nhật:<br>`UPDATE Patient SET full_name = :name, phone = :phone, ...`<br>`WHERE patientID = :id` |
| 8    |                                  | Hiển thị success                                                                                    |
| 9    | Xem thông tin đã cập nhật        |                                                                                                     |

### Luồng sự kiện phụ

**2a. Bệnh nhân không tồn tại:**

- 2a.1. Hiển thị lỗi
- 2a.2. Kết thúc use case

**6a. Dữ liệu không hợp lệ:**

- 6a.1. Hiển thị lỗi
- 6a.2. Quay lại bước 3

### Ràng buộc nghiệp vụ

- Không cho phép sửa patientID
- Phone và Email mới phải unique
- Ghi log mọi thay đổi

---

## UC_PATIENT_05: Delete Patient (Xóa bệnh nhân)

### Mô tả

Xóa bệnh nhân khỏi hệ thống.

### Tác nhân chính

- Admin

### Điều kiện tiên quyết

- Bệnh nhân tồn tại
- Bệnh nhân không có medical records

### Điều kiện hậu

- Bệnh nhân và dữ liệu liên quan được xóa

### Luồng sự kiện chính

| Bước | Admin                      | Hệ thống                                                                                                               |
| ---- | -------------------------- | ---------------------------------------------------------------------------------------------------------------------- |
| 1    | Click "Xóa" trên bệnh nhân |                                                                                                                        |
| 2    |                            | Hiển thị dialog xác nhận                                                                                               |
| 3    | Xác nhận xóa               |                                                                                                                        |
| 4    |                            | Kiểm tra medical records:<br>`SELECT COUNT(*) FROM MedicalRecord WHERE patientID = :id`                                |
| 5    |                            | Xóa appointments:<br>`DELETE FROM Appointment WHERE patientID = :id`                                                   |
| 6    |                            | Xóa account (nếu có):<br>`DELETE FROM Account WHERE accountID = (SELECT accountID FROM Patient WHERE patientID = :id)` |
| 7    |                            | Xóa patient:<br>`DELETE FROM Patient WHERE patientID = :id`                                                            |
| 8    |                            | Hiển thị success và refresh danh sách                                                                                  |

### Luồng sự kiện phụ

**4a. Bệnh nhân có medical records:**

- 4a.1. Hiển thị: "Không thể xóa bệnh nhân có hồ sơ khám bệnh"
- 4a.2. Đề xuất: "Bạn có thể ẩn bệnh nhân thay vì xóa"
- 4a.3. Kết thúc use case

### Ràng buộc nghiệp vụ

- Chỉ Admin mới có quyền xóa
- Không xóa nếu có medical records (bảo vệ dữ liệu y tế)
- Xóa cascade: Appointments, Account
- Ghi log vào audit trail

---

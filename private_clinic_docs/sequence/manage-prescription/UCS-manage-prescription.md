# ĐẶC TẢ CÁC USE CASE - MANAGE PRESCRIPTION

Tài liệu này mô tả các use case thuộc nhóm Quản lý Đơn thuốc.

Gồm 3 use case chính:

1. View Prescriptions (Xem danh sách đơn thuốc)
2. Add Prescription (Tạo đơn thuốc)
3. Dispense Medicine (Cấp phát thuốc)

---

## UC_PRESC_01: View Prescriptions

### Tác nhân chính

- Doctor, Pharmacy Staff

### Luồng chính

1. Truy cập danh sách prescriptions
2. Hệ thống hiển thị với patient info, doctor, date
3. Click vào prescription xem chi tiết medicines

---

## UC_PRESC_02: Add Prescription

### Tác nhân chính

- Doctor

### Điều kiện tiên quyết

- Đã có MedicalRecord

### Luồng chính

1. Từ Medical Record, click "Add Prescription"
2. Search và chọn medicines
3. Nhập cho mỗi medicine:
   - Quantity
   - Dosage (liều lượng)
   - Usage (cách dùng)
4. Hệ thống check medicine inventory
5. Nếu một số thuốc hết hàng:
   - Hiển thị warning
   - VẪN cho phép tạo prescription
   - Ghi chú thuốc nào unavailable
6. Tạo Prescription và PrescriptionDetails
7. Hiển thị thành công với warnings nếu có

### Ràng buộc

- Doctor tự do kê đơn, không bị chặn bởi inventory
- System chỉ alert để Doctor biết

---

## UC_PRESC_03: Dispense Medicine

### Tác nhân chính

- Pharmacy Staff

### Điều kiện tiên quyết

- Đã có Prescription
- Đã có Invoice

### Luồng chính

1. Pharmacy Staff tìm patient (by name/ID)
2. Hệ thống hiển thị invoices của patient
3. Chọn invoice và xem prescription
4. Staff chọn medicines để dispense (CHỈ những medicines còn hàng)
5. Hệ thống:
   - Check inventory
   - Apply FEFO (First-Expire-First-Out)
   - Update MedicineInventory (giảm quantity)
   - Add InvoiceMedicineDetail
   - Update Invoice total
6. In nhãn thuốc và hướng dẫn sử dụng
7. Thông báo cho patient về medicines không có sẵn (mua ngoài)

### Ràng buộc nghiệp vụ

- Chỉ dispense medicines có trong inventory
- Áp dụng FEFO để ưu tiên thuốc hết hạn sớm nhất
- Không dispense được thì patient phải mua ngoài

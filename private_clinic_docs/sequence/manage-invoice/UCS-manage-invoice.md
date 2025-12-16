# Use Case Summary: Manage Invoice

Tổng hợp các use case liên quan đến quản lý hóa đơn (Invoice Management).

## UC_INV_01: View and Filter Invoices

**Actors**: Receptionist, Pharmacy Staff

**Main Flow**:

1. Staff truy cập danh sách invoice
2. Hệ thống hiển thị danh sách invoice với thông tin cơ bản
3. Staff có thể lọc theo ngày, trạng thái thanh toán, bệnh nhân

## UC_INV_02: View Invoice Details

**Actors**: Receptionist, Pharmacy Staff, Patient

**Main Flow**:

1. User chọn một invoice từ danh sách
2. Hệ thống hiển thị chi tiết invoice bao gồm dịch vụ, thuốc, tổng tiền

## UC_INV_03: Add Service to Invoice

**Actors**: Receptionist

**Main Flow**:

1. Receptionist chọn invoice cần thêm dịch vụ
2. Chọn dịch vụ từ danh sách
3. Hệ thống cập nhật invoice với dịch vụ mới và tính lại tổng tiền

## UC_INV_04: Add Medicine to Invoice

**Actors**: Pharmacy Staff

**Main Flow**:

1. Pharmacy Staff chọn invoice
2. Chọn thuốc từ prescription
3. Hệ thống cập nhật invoice với thuốc đã cấp phát và tính lại tổng tiền

## UC_INV_05: Process Payment

**Actors**: Receptionist

**Main Flow**:

1. Receptionist chọn invoice cần thanh toán
2. Chọn phương thức thanh toán
3. Nhập số tiền thanh toán
4. Hệ thống cập nhật trạng thái invoice và tạo payment record

## UC_INV_06: Print Invoice

**Actors**: Receptionist, Patient

**Main Flow**:

1. User chọn invoice cần in
2. Hệ thống tạo PDF invoice
3. Hiển thị preview và cho phép in/tải xuống

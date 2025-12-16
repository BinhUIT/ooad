# UCS Patient Portal / Self-Service

## Overview

Module cung cấp các chức năng tự phục vụ cho bệnh nhân qua cổng thông tin trực tuyến.

## Use Cases

### UC_PORTAL_01: Book Appointment Online

- **Actor**: Patient (đã đăng nhập)
- **Description**: Bệnh nhân tự đặt lịch hẹn khám qua hệ thống online
- **Main Flow**:
  1. Patient truy cập màn hình đặt lịch
  2. Hệ thống hiển thị lịch làm việc của các bác sĩ
  3. Patient chọn bác sĩ, ngày, giờ khám
  4. Hệ thống kiểm tra slot còn trống
  5. Patient nhập lý do khám
  6. Hệ thống tạo appointment
- **Business Rules**:
  - Chỉ đặt lịch trong tương lai (>= ngày hiện tại)
  - Kiểm tra slot còn trống theo staff_schedule
  - Kiểm tra số lượng bệnh nhân tối đa/ngày
  - Trạng thái mặc định: "Scheduled"

### UC_PORTAL_02: View Medical Record History

- **Actor**: Patient (đã đăng nhập)
- **Description**: Bệnh nhân xem lịch sử hồ sơ khám bệnh của mình
- **Main Flow**:
  1. Patient truy cập lịch sử khám bệnh
  2. Hệ thống hiển thị danh sách các medical records
  3. Patient có thể xem chi tiết từng record
  4. Hệ thống hiển thị thông tin: ngày khám, bác sĩ, chẩn đoán, đơn thuốc
- **Business Rules**:
  - Chỉ xem được medical records của chính mình
  - Hiển thị cả đơn thuốc kèm theo

### UC_PORTAL_03: View Appointment History

- **Actor**: Patient (đã đăng nhập)
- **Description**: Bệnh nhân xem lịch sử các lần đặt hẹn
- **Main Flow**:
  1. Patient truy cập lịch sử đặt hẹn
  2. Hệ thống hiển thị danh sách appointments (cả quá khứ & tương lai)
  3. Patient có thể lọc theo trạng thái
  4. Patient có thể hủy appointment (nếu còn trong tương lai)
- **Business Rules**:
  - Chỉ xem được appointments của chính mình
  - Chỉ hủy được appointment trong tương lai (>= ngày hiện tại)

### UC_PORTAL_04: View Invoice History

- **Actor**: Patient (đã đăng nhập)
- **Description**: Bệnh nhân xem lịch sử hóa đơn thanh toán
- **Main Flow**:
  1. Patient truy cập lịch sử hóa đơn
  2. Hệ thống hiển thị danh sách invoices
  3. Patient có thể xem chi tiết từng invoice
  4. Hệ thống hiển thị: dịch vụ, thuốc, tổng tiền, trạng thái thanh toán
- **Business Rules**:
  - Chỉ xem được invoices của chính mình
  - Hiển thị cả chi tiết dịch vụ & thuốc

### UC_PORTAL_05: Patient Dashboard/Home

- **Actor**: Patient (đã đăng nhập)
- **Description**: Trang chủ bệnh nhân hiển thị tổng quan thông tin
- **Main Flow**:
  1. Patient đăng nhập vào hệ thống
  2. Hệ thống hiển thị dashboard với:
     - Appointment sắp tới
     - Medical record gần nhất
     - Invoice chưa thanh toán (nếu có)
     - Quick actions (đặt lịch mới, xem hồ sơ, v.v.)
  3. Patient có thể navigate đến các chức năng khác
- **Business Rules**:
  - Hiển thị thông tin cá nhân hóa
  - Quick access to common features

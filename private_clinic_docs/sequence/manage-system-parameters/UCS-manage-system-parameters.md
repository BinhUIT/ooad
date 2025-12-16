# UCS Manage System Parameters

## Overview

Module quản lý các tham số cấu hình hệ thống cho phòng khám (số lượng bệnh nhân tối đa mỗi ngày, thời gian khám, v.v.)

## Use Cases

### UC_PAR_01: View Parameters

- **Actor**: Admin
- **Description**: Xem danh sách toàn bộ các tham số hệ thống và giá trị hiện tại
- **Main Flow**:
  1. Admin truy cập màn hình quản lý tham số
  2. Hệ thống hiển thị danh sách các tham số (tên, mô tả, giá trị hiện tại, giá trị mặc định)
  3. Admin có thể tìm kiếm/lọc tham số

### UC_PAR_02: Edit Parameter Value

- **Actor**: Admin
- **Description**: Chỉnh sửa giá trị của tham số hệ thống
- **Main Flow**:
  1. Admin chọn tham số muốn chỉnh sửa
  2. Hệ thống hiển thị form với giá trị hiện tại
  3. Admin nhập giá trị mới
  4. Hệ thống xác thực (kiểu dữ liệu, phạm vi hợp lệ)
  5. Hệ thống lưu giá trị mới
- **Business Rules**:
  - Xác thực kiểu dữ liệu (số, text, boolean)
  - Kiểm tra phạm vi giá trị hợp lệ
  - Log lịch sử thay đổi

### UC_PAR_03: Reset Parameter to Default

- **Actor**: Admin
- **Description**: Khôi phục giá trị mặc định cho tham số
- **Main Flow**:
  1. Admin chọn tham số muốn reset
  2. Hệ thống hiển thị xác nhận với giá trị mặc định
  3. Admin xác nhận
  4. Hệ thống khôi phục giá trị mặc định
- **Business Rules**:
  - Yêu cầu xác nhận trước khi reset
  - Log lịch sử thay đổi

## System Parameters Examples

- MAX_PATIENTS_PER_DAY: Số lượng bệnh nhân tối đa mỗi ngày
- EXAMINATION_TIME_MINUTES: Thời gian khám mặc định (phút)
- MEDICINE_EXPIRY_WARNING_DAYS: Số ngày cảnh báo thuốc hết hạn
- AUTO_CANCEL_RECEPTION: Tự động hủy tiếp nhận cuối ngày (true/false)

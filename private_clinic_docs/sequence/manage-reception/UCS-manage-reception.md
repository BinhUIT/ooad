# Use Case Summary: Manage Reception

Tổng hợp các use case liên quan đến quản lý lễ tân (Reception Management).

## UC_RCP_01: View and Filter Receptions

**Actors**: Receptionist

**Main Flow**:

1. Receptionist truy cập danh sách reception
2. Hệ thống hiển thị danh sách reception với thông tin cơ bản
3. Receptionist có thể lọc theo ngày, trạng thái, bác sĩ, bệnh nhân

## UC_RCP_02: View Reception Details

**Actors**: Receptionist, Doctor

**Main Flow**:

1. User chọn một reception từ danh sách
2. Hệ thống hiển thị chi tiết reception bao gồm thông tin bệnh nhân, lịch hẹn, trạng thái

## UC_RCP_03: Add New Reception

**Actors**: Receptionist

**Precondition**: Bệnh nhân đã có appointment

**Main Flow**:

1. Receptionist chọn appointment để tạo reception
2. Nhập thông tin reception (triệu chứng, ghi chú)
3. Hệ thống tạo reception với trạng thái "Waiting"

## UC_RCP_04: Edit Reception Status

**Actors**: Receptionist

**Main Flow**:

1. Receptionist chọn reception cần cập nhật
2. Thay đổi trạng thái (Waiting → In Examination → Done)
3. Hệ thống cập nhật và thông báo cho bác sĩ (nếu "In Examination")

## UC_RCP_05: End Day/Close Receptions

**Actors**: Receptionist

**Main Flow**:

1. Receptionist chọn chức năng "End Day"
2. Hệ thống kiểm tra các reception còn pending
3. Xác nhận và đóng tất cả reception chưa hoàn thành với trạng thái "Cancelled"

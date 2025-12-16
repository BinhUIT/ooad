# Use Case Summary: Manage Staff

Tổng hợp các use case liên quan đến quản lý nhân viên (Staff Management).

## UC_STF_01: View and Filter Staff

**Actors**: Admin

**Main Flow**:

1. Admin truy cập danh sách nhân viên
2. Hệ thống hiển thị danh sách nhân viên với thông tin cơ bản
3. Admin có thể lọc theo vai trò, trạng thái

## UC_STF_02: View Staff Details

**Actors**: Admin

**Main Flow**:

1. Admin chọn một nhân viên từ danh sách
2. Hệ thống hiển thị chi tiết nhân viên bao gồm thông tin cá nhân, lịch làm việc

## UC_STF_03: Add New Staff

**Actors**: Admin

**Main Flow**:

1. Admin nhập thông tin nhân viên mới
2. Chọn vai trò (Doctor, Receptionist, Pharmacy Staff)
3. Hệ thống tạo account và staff record

## UC_STF_04: Edit Staff

**Actors**: Admin

**Main Flow**:

1. Admin chọn nhân viên cần chỉnh sửa
2. Cập nhật thông tin nhân viên
3. Hệ thống lưu thay đổi

## UC_STF_05: Delete Staff

**Actors**: Admin

**Precondition**: Nhân viên không có dữ liệu liên quan

**Main Flow**:

1. Admin chọn xóa nhân viên
2. Hệ thống kiểm tra ràng buộc
3. Xác nhận và xóa nhân viên

## UC_STF_06: Manage Staff Schedule

**Actors**: Admin

**Main Flow**:

1. Admin chọn nhân viên để quản lý lịch
2. Xem lịch hiện tại và thêm/sửa/xóa ca làm việc
3. Hệ thống cập nhật lịch làm việc

# Use Case Summary: Manage Service

Tổng hợp các use case liên quan đến quản lý dịch vụ khám (Service Management).

## UC_SVC_01: View and Filter Services

**Actors**: Admin, Receptionist

**Main Flow**:

1. User truy cập danh sách dịch vụ
2. Hệ thống hiển thị danh sách dịch vụ với giá
3. User có thể lọc theo loại dịch vụ, trạng thái

## UC_SVC_02: View Service Details

**Actors**: Admin

**Main Flow**:

1. Admin chọn một dịch vụ từ danh sách
2. Hệ thống hiển thị chi tiết dịch vụ

## UC_SVC_03: Add New Service

**Actors**: Admin

**Main Flow**:

1. Admin nhập thông tin dịch vụ mới (tên, giá, mô tả)
2. Hệ thống tạo dịch vụ mới

## UC_SVC_04: Edit Service

**Actors**: Admin

**Main Flow**:

1. Admin chọn dịch vụ cần chỉnh sửa
2. Cập nhật thông tin dịch vụ
3. Hệ thống lưu thay đổi

## UC_SVC_05: Delete Service

**Actors**: Admin

**Precondition**: Dịch vụ không được sử dụng trong invoice

**Main Flow**:

1. Admin chọn xóa dịch vụ
2. Hệ thống kiểm tra ràng buộc
3. Xác nhận và xóa dịch vụ

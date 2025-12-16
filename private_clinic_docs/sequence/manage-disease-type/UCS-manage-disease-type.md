# Use Case Summary: Manage Disease Type

Tổng hợp các use case liên quan đến quản lý loại bệnh (Disease Type Management).

## UC_DIS_01: View and Filter Disease Types

**Actors**: Admin, Doctor

**Main Flow**:

1. User truy cập danh sách loại bệnh
2. Hệ thống hiển thị danh sách loại bệnh
3. User có thể tìm kiếm theo tên

## UC_DIS_02: View Disease Type Details

**Actors**: Admin

**Main Flow**:

1. Admin chọn một loại bệnh từ danh sách
2. Hệ thống hiển thị chi tiết loại bệnh

## UC_DIS_03: Add New Disease Type

**Actors**: Admin

**Main Flow**:

1. Admin nhập thông tin loại bệnh mới (tên, mô tả)
2. Hệ thống tạo loại bệnh mới

## UC_DIS_04: Edit Disease Type

**Actors**: Admin

**Main Flow**:

1. Admin chọn loại bệnh cần chỉnh sửa
2. Cập nhật thông tin loại bệnh
3. Hệ thống lưu thay đổi

## UC_DIS_05: Delete Disease Type

**Actors**: Admin

**Precondition**: Loại bệnh không được sử dụng trong medical record

**Main Flow**:

1. Admin chọn xóa loại bệnh
2. Hệ thống kiểm tra ràng buộc
3. Xác nhận và xóa loại bệnh

# Use Case Summary: Manage Payment Method

Tổng hợp các use case liên quan đến quản lý phương thức thanh toán (Payment Method Management).

## UC_PAY_01: View Payment Methods

**Actors**: Admin, Receptionist

**Main Flow**:

1. User truy cập danh sách phương thức thanh toán
2. Hệ thống hiển thị danh sách các phương thức (Cash, Card, Transfer, etc.)

## UC_PAY_02: View Payment Method Details

**Actors**: Admin

**Main Flow**:

1. Admin chọn một phương thức từ danh sách
2. Hệ thống hiển thị chi tiết phương thức

## UC_PAY_03: Add New Payment Method

**Actors**: Admin

**Main Flow**:

1. Admin nhập thông tin phương thức mới (tên, mô tả)
2. Hệ thống tạo phương thức mới

## UC_PAY_04: Edit Payment Method

**Actors**: Admin

**Main Flow**:

1. Admin chọn phương thức cần chỉnh sửa
2. Cập nhật thông tin phương thức
3. Hệ thống lưu thay đổi

## UC_PAY_05: Delete Payment Method

**Actors**: Admin

**Precondition**: Phương thức không được sử dụng trong payment

**Main Flow**:

1. Admin chọn xóa phương thức
2. Hệ thống kiểm tra ràng buộc
3. Xác nhận và xóa phương thức

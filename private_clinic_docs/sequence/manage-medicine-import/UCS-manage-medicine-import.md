# Use Case Summary: Manage Medicine Import

Tổng hợp các use case liên quan đến quản lý nhập thuốc (Medicine Import Management).

## UC_IMP_01: View Medicine Imports

**Actors**: Warehouse Staff

**Main Flow**:

1. Staff truy cập danh sách phiếu nhập thuốc
2. Hệ thống hiển thị danh sách với thông tin cơ bản
3. Staff có thể lọc theo ngày, nhà cung cấp

## UC_IMP_02: View Import Details

**Actors**: Warehouse Staff

**Main Flow**:

1. Staff chọn một phiếu nhập từ danh sách
2. Hệ thống hiển thị chi tiết phiếu nhập bao gồm danh sách thuốc, số lượng, giá

## UC_IMP_03: Add New Import

**Actors**: Warehouse Staff

**Main Flow**:

1. Staff tạo phiếu nhập mới
2. Chọn thuốc, nhập số lượng, giá nhập, hạn sử dụng
3. Hệ thống tạo phiếu nhập và cập nhật inventory

## UC_IMP_04: Edit Import

**Actors**: Warehouse Staff

**Precondition**: Phiếu nhập chưa được duyệt

**Main Flow**:

1. Staff chọn phiếu nhập cần chỉnh sửa
2. Cập nhật thông tin phiếu nhập
3. Hệ thống lưu thay đổi

## UC_IMP_05: Delete Import

**Actors**: Warehouse Staff

**Precondition**: Phiếu nhập chưa được duyệt và chưa xuất thuốc

**Main Flow**:

1. Staff chọn xóa phiếu nhập
2. Hệ thống kiểm tra ràng buộc
3. Xác nhận và xóa phiếu nhập

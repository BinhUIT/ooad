# Use Case Summary: Manage Medicine Inventory

Tổng hợp các use case liên quan đến quản lý kho thuốc (Medicine Inventory Management).

## UC_MED_01: View Medicine Inventory

**Actors**: Warehouse Staff, Pharmacy Staff

**Main Flow**:

1. Staff truy cập danh sách thuốc trong kho
2. Hệ thống hiển thị danh sách thuốc với số lượng tồn kho, hạn sử dụng
3. Staff có thể lọc theo loại thuốc, trạng thái tồn kho

## UC_MED_02: View Medicine Details

**Actors**: Warehouse Staff

**Main Flow**:

1. Staff chọn một loại thuốc từ danh sách
2. Hệ thống hiển thị chi tiết thuốc bao gồm thông tin cơ bản, các lô hàng, lịch sử nhập/xuất

## UC_MED_03: Add New Medicine Type

**Actors**: Warehouse Staff

**Main Flow**:

1. Staff nhập thông tin loại thuốc mới
2. Hệ thống kiểm tra trùng lặp
3. Tạo loại thuốc mới trong hệ thống

## UC_MED_04: Edit Medicine Type

**Actors**: Warehouse Staff

**Main Flow**:

1. Staff chọn loại thuốc cần chỉnh sửa
2. Cập nhật thông tin thuốc
3. Hệ thống lưu thay đổi

## UC_MED_05: Delete Medicine Type

**Actors**: Warehouse Staff

**Precondition**: Không có tồn kho và không có trong prescription

**Main Flow**:

1. Staff chọn xóa loại thuốc
2. Hệ thống kiểm tra ràng buộc
3. Xác nhận và xóa loại thuốc

## UC_MED_06: Add Medicine Price

**Actors**: Warehouse Staff

**Main Flow**:

1. Staff chọn loại thuốc
2. Nhập giá mới với ngày hiệu lực
3. Hệ thống lưu thông tin giá

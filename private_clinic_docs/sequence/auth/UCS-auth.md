# ĐẶC TẢ CÁC USE CASE - AUTHENTICATION & ACCOUNT MANAGEMENT

Tài liệu này mô tả các use case thuộc nhóm Xác thực và Quản lý tài khoản người dùng (Authentication & Account Management) cho hệ thống Private Clinic Management System.

Gồm 4 use case chính:

1. Sign In (Đăng nhập)
2. Sign Up (Đăng ký)
3. Forgot Password (Quên mật khẩu)
4. Manage Profile (Quản lý hồ sơ cá nhân)

---

## UC_AUTH_01: Sign In (Đăng nhập)

### Mô tả

Người dùng đăng nhập vào hệ thống để sử dụng các chức năng theo vai trò của mình.

### Tác nhân chính

- Patient (Bệnh nhân)
- Doctor (Bác sĩ)
- Receptionist (Lễ tân)
- Pharmacist/Warehouse Staff (Nhân viên kho/Dược sĩ)
- Admin (Quản trị viên)

### Điều kiện tiên quyết

- Người dùng đã có tài khoản trong hệ thống
- Tài khoản chưa bị khóa (is_lock = false)

### Điều kiện hậu

- Người dùng được xác thực thành công
- JWT token được sinh và lưu trữ
- Chuyển hướng đến dashboard tương ứng với vai trò

### Luồng sự kiện chính

| Bước | User                      | Hệ thống                                                                                                                                                                                                           |
| ---- | ------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 1    | Truy cập trang đăng nhập  |                                                                                                                                                                                                                    |
| 2    |                           | Hiển thị form đăng nhập với username và password                                                                                                                                                                   |
| 3    | Nhập username và password |                                                                                                                                                                                                                    |
| 4    | Click "Sign In"           |                                                                                                                                                                                                                    |
| 5    |                           | Validate định dạng dữ liệu đầu vào                                                                                                                                                                                 |
| 6    |                           | Kiểm tra thông tin đăng nhập: `SELECT * FROM Account WHERE username = :username`                                                                                                                                   |
| 7    |                           | Verify password (compare hashed)                                                                                                                                                                                   |
| 8    |                           | Kiểm tra trạng thái tài khoản (is_lock = false)                                                                                                                                                                    |
| 9    |                           | Sinh JWT token chứa: accountID, username, role, email                                                                                                                                                              |
| 10   |                           | Trả về Success với token và thông tin user                                                                                                                                                                         |
| 11   |                           | Chuyển hướng đến dashboard theo role:<br>- Doctor → Doctor Dashboard<br>- Receptionist → Receptionist Dashboard<br>- Pharmacist → Pharmacy Dashboard<br>- Admin → Admin Dashboard<br>- Patient → Patient Dashboard |
| 12   | Xem dashboard tương ứng   |                                                                                                                                                                                                                    |

### Luồng sự kiện phụ

**6a. Username không tồn tại:**

- 6a.1. Trả về lỗi: "Invalid username or password"
- 6a.2. Hiển thị thông báo lỗi
- 6a.3. Quay lại bước 2

**7a. Password không đúng:**

- 7a.1. Trả về lỗi: "Invalid username or password"
- 7a.2. Tăng số lần đăng nhập thất bại
- 7a.3. Nếu thất bại > 5 lần trong 15 phút → khóa tài khoản tạm thời 30 phút
- 7a.4. Hiển thị thông báo lỗi
- 7a.5. Quay lại bước 2

**8a. Tài khoản bị khóa:**

- 8a.1. Trả về lỗi: "Account is locked. Please contact administrator"
- 8a.2. Hiển thị thông báo
- 8a.3. Kết thúc use case

### Ràng buộc nghiệp vụ

- JWT Token Expiry: Access Token (1 giờ), Refresh Token (7 ngày)
- Password phải được hash bằng bcrypt (cost ≥ 10)
- Giới hạn số lần đăng nhập thất bại để tránh brute force attack
- Ghi log mỗi lần đăng nhập thành công/thất bại

---

## UC_AUTH_02: Sign Up (Đăng ký)

### Mô tả

Bệnh nhân mới đăng ký tài khoản để sử dụng dịch vụ của phòng khám.

### Tác nhân chính

- Patient (chưa có tài khoản)

### Điều kiện tiên quyết

- Username và email chưa tồn tại trong hệ thống

### Điều kiện hậu

- Tạo mới Account và Patient record
- Người dùng được đăng nhập tự động với JWT token

### Luồng sự kiện chính

| Bước | User                            | Hệ thống                                                                                                                                                                                                            |
| ---- | ------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1    | Truy cập trang đăng ký          |                                                                                                                                                                                                                     |
| 2    |                                 | Hiển thị form đăng ký với các trường:<br>- Username (*bắt buộc)<br>- Password (*bắt buộc, ≥8 ký tự)<br>- Full name (*bắt buộc)<br>- Email (*bắt buộc)<br>- Phone number<br>- Date of Birth<br>- Gender<br>- Address |
| 3    | Nhập thông tin đăng ký          |                                                                                                                                                                                                                     |
| 4    | Click "Sign Up"                 |                                                                                                                                                                                                                     |
| 5    |                                 | Validate định dạng dữ liệu                                                                                                                                                                                          |
| 6    |                                 | Kiểm tra username và email chưa tồn tại:<br>`SELECT COUNT(*) FROM Account WHERE username = :username OR email = :email`                                                                                             |
| 7    |                                 | Hash password (bcrypt)                                                                                                                                                                                              |
| 8    |                                 | Tạo Account mới:<br>`INSERT INTO Account (username, password, role) VALUES (:username, :hashed_password, 'PATIENT')`                                                                                                |
| 9    |                                 | Tạo Patient record:<br>`INSERT INTO Patient (accountID, full_name, email, phone, DOB, gender, address) VALUES (...)`                                                                                                |
| 10   |                                 | Sinh JWT token                                                                                                                                                                                                      |
| 11   |                                 | Trả về Success và chuyển hướng đến Patient Dashboard                                                                                                                                                                |
| 12   | Được đăng nhập và xem dashboard |                                                                                                                                                                                                                     |

### Luồng sự kiện phụ

**5a. Dữ liệu không hợp lệ:**

- 5a.1. Hiển thị thông báo lỗi chi tiết
- 5a.2. Giữ nguyên dữ liệu đã nhập (trừ password)
- 5a.3. Quay lại bước 2

**6a. Username hoặc email đã tồn tại:**

- 6a.1. Trả về lỗi: "Username or email already exists"
- 6a.2. Hiển thị thông báo
- 6a.3. Quay lại bước 2

### Ràng buộc nghiệp vụ

- Username: Unique, không chứa khoảng trắng, 3-50 ký tự
- Email: Unique, validate format
- Password: ≥8 ký tự, chứa chữ hoa, chữ thường, số
- Phone: 10-11 chữ số
- Role mặc định: 'PATIENT'

---

## UC_AUTH_03: Forgot Password (Quên mật khẩu)

### Mô tả

Người dùng quên mật khẩu và yêu cầu đặt lại mật khẩu mới qua email.

### Tác nhân chính

- User (bất kỳ vai trò nào)

### Điều kiện tiên quyết

- Email đã được đăng ký trong hệ thống

### Điều kiện hậu

- Password được cập nhật thành công
- Người dùng có thể đăng nhập với mật khẩu mới

### Luồng sự kiện chính

| Bước | User                             | Hệ thống                                                                                                                        |
| ---- | -------------------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| 1    | Truy cập trang "Forgot Password" |                                                                                                                                 |
| 2    |                                  | Hiển thị form nhập email                                                                                                        |
| 3    | Nhập email                       |                                                                                                                                 |
| 4    | Click "Send Reset Link"          |                                                                                                                                 |
| 5    |                                  | Tìm account theo email                                                                                                          |
| 6    |                                  | Sinh reset token (random, expire 1 giờ)                                                                                         |
| 7    |                                  | Lưu token: `UPDATE Account SET reset_token = :token, reset_token_expire = :expire_time WHERE email = :email`                    |
| 8    |                                  | Gửi email chứa reset link với token                                                                                             |
| 9    |                                  | Hiển thị: "Check your email for reset instructions"                                                                             |
| 10   | Click reset link từ email        |                                                                                                                                 |
| 11   |                                  | Validate token và thời hạn                                                                                                      |
| 12   |                                  | Hiển thị form nhập mật khẩu mới                                                                                                 |
| 13   | Nhập mật khẩu mới (2 lần)        |                                                                                                                                 |
| 14   | Click "Reset Password"           |                                                                                                                                 |
| 15   |                                  | Validate mật khẩu mới                                                                                                           |
| 16   |                                  | Hash password mới                                                                                                               |
| 17   |                                  | Cập nhật password và xóa token:<br>`UPDATE Account SET password = :new_password, reset_token = NULL WHERE reset_token = :token` |
| 18   |                                  | Hiển thị success và chuyển đến trang đăng nhập                                                                                  |
| 19   | Đăng nhập với mật khẩu mới       |                                                                                                                                 |

### Luồng sự kiện phụ

**5a. Email không tồn tại:**

- 5a.1. Vẫn hiển thị success message (để bảo mật, không tiết lộ email có tồn tại hay không)
- 5a.2. Không gửi email
- 5a.3. Kết thúc use case

**11a. Token không hợp lệ hoặc hết hạn:**

- 11a.1. Hiển thị: "Reset link is invalid or expired"
- 11a.2. Cho phép yêu cầu reset mới
- 11a.3. Quay lại bước 1

### Ràng buộc nghiệp vụ

- Reset token expire sau 1 giờ
- Token chỉ sử dụng được 1 lần
- Gửi email qua SMTP service
- Password mới phải khác password cũ

---

## UC_AUTH_04: Manage Profile (Quản lý hồ sơ)

### Mô tả

Người dùng xem và cập nhật thông tin cá nhân của mình.

### Tác nhân chính

- Patient
- Doctor
- Receptionist
- Pharmacist
- Admin

### Điều kiện tiên quyết

- Người dùng đã đăng nhập

### Điều kiện hậu

- Thông tin cá nhân được cập nhật thành công

### Luồng sự kiện chính

| Bước | User                                       | Hệ thống                                                                                                                           |
| ---- | ------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------- |
| 1    | Click "Profile" từ menu                    |                                                                                                                                    |
| 2    |                                            | Lấy thông tin user từ JWT token                                                                                                    |
| 3    |                                            | Query thông tin chi tiết theo role:<br>- Patient: từ bảng Patient<br>- Staff: từ bảng Staff (Doctor/Receptionist/Pharmacist/Admin) |
| 4    |                                            | Hiển thị form profile với thông tin hiện tại                                                                                       |
| 5    | Cập nhật thông tin (phone, address, email) |                                                                                                                                    |
| 6    | Click "Update Profile"                     |                                                                                                                                    |
| 7    |                                            | Validate dữ liệu mới                                                                                                               |
| 8    |                                            | Cập nhật thông tin:<br>`UPDATE Patient/Staff SET phone = :phone, address = :address, email = :email WHERE id = :id`                |
| 9    |                                            | Hiển thị success message                                                                                                           |
| 10   |                                            | Refresh thông tin hiển thị                                                                                                         |

**Change Password:**

| Bước | User                    | Hệ thống                                                                                 |
| ---- | ----------------------- | ---------------------------------------------------------------------------------------- |
| 11   | Click "Change Password" |                                                                                          |
| 12   |                         | Hiển thị form với old_password, new_password, confirm_password                           |
| 13   | Nhập mật khẩu cũ và mới |                                                                                          |
| 14   | Click "Change Password" |                                                                                          |
| 15   |                         | Verify old password                                                                      |
| 16   |                         | Validate new password                                                                    |
| 17   |                         | Hash và cập nhật:<br>`UPDATE Account SET password = :new_password WHERE accountID = :id` |
| 18   |                         | Hiển thị success                                                                         |

### Luồng sự kiện phụ

**7a. Email mới đã tồn tại:**

- 7a.1. Hiển thị: "Email already in use"
- 7a.2. Quay lại bước 4

**15a. Old password không đúng:**

- 15a.1. Hiển thị: "Current password is incorrect"
- 15a.2. Quay lại bước 12

**16a. New password không hợp lệ:**

- 16a.1. Hiển thị lỗi validation
- 16a.2. Quay lại bước 12

### Ràng buộc nghiệp vụ

- Email phải unique trong toàn hệ thống
- Không cho phép thay đổi username
- New password phải khác old password
- Phone number phải đúng định dạng

---

# Authentication & Account Management Use Case Specifications

---

## 6.2.1. Use-case Specification: "Sign In"

### 6.2.1.1. Brief Description

Allows users to log into the system to perform available functions based on their assigned role.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The user selects the "Login" function on the system interface.
2. The system prompts the user to enter their email/username and password.
3. The user enters the login information and clicks "Login."
4. The system verifies the login information against the database.
5. If the information is correct, the system authenticates the user, generates a JWT token, and redirects them to the appropriate dashboard based on their role (Doctor Dashboard, Receptionist Dashboard, Pharmacy Dashboard, Admin Dashboard, or Patient Dashboard).

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. Incorrect Email/Username or Password

If the information is incorrect, the system displays the error message "Incorrect email or password" and asks the user to try again. The system increments the failed login attempt counter.

##### 6.2.1.2.2.2. Account Locked

If the account is locked (is_lock = true), the system displays the error message "Your account has been locked. Please contact administrator."

##### 6.2.1.2.2.3. Too Many Failed Attempts

If the user fails to login more than 5 times within 15 minutes, the system temporarily locks the account for 30 minutes and displays an appropriate message.

### 6.2.1.3. Special Requirements

- The password must be encrypted using bcrypt (cost ≥ 10) before being stored.
- JWT Token expiry: Access Token (1 hour), Refresh Token (7 days).
- All login attempts (successful and failed) must be logged for security auditing.
- Rate limiting must be implemented to prevent brute force attacks.

### 6.2.1.4. Pre-condition

- The user has an account in the system but has not logged in.
- The account is not locked (is_lock = false).

### 6.2.1.5. Post-condition

- If successful: The system grants access to system features based on user role, JWT token is generated and stored.
- If unsuccessful: The system prompts the user to try again with an error message.

### 6.2.1.6. Extension Points

- If the user does not have an account, they can register one by themselves (patients only).
- If the user forgot their password, they can use the "Forgot Password" function.
- Doctor, Receptionist, Pharmacist, and Admin accounts must be created by the admin (cannot be self-registered).

---

## 6.2.2. Use-case Specification: "Sign Up"

### 6.2.2.1. Brief Description

Allows new patients to register an account to use the clinic's services through the online portal.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The user selects the "Sign Up" or "Register" function on the system interface.
2. The system displays a registration form with required fields: Username, Password, Full name, Email, Phone number, Date of Birth, Gender, and Address.
3. The user enters the registration information and clicks "Sign Up."
4. The system validates the input data format and business rules.
5. The system checks if the username and email are unique in the database.
6. The system hashes the password and creates a new Account record with role 'PATIENT'.
7. The system creates a corresponding Patient record linked to the account.
8. The system generates a JWT token and automatically logs the user in.
9. The system redirects the user to the Patient Dashboard.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Invalid Data Format

If any input data is invalid (e.g., invalid email format, password too short), the system displays specific error messages for each field and retains the entered data (except password) for correction.

##### 6.2.2.2.2.2. Username or Email Already Exists

If the username or email already exists in the system, the system displays the error message "Username or email already exists" and asks the user to provide different credentials.

### 6.2.2.3. Special Requirements

- Username: Must be unique, no whitespace, 3-50 characters.
- Email: Must be unique and in valid email format.
- Password: Minimum 8 characters, must contain uppercase, lowercase, and numbers.
- Phone: Must be 10-11 digits.
- Password must be hashed using bcrypt before storage.

### 6.2.2.4. Pre-condition

- The user does not have an existing account in the system.
- The username and email are not already registered.

### 6.2.2.5. Post-condition

- If successful: A new Account and Patient record are created, user is automatically logged in with JWT token.
- If unsuccessful: The system displays error messages and allows the user to correct the input.

### 6.2.2.6. Extension Points

- Only patient accounts can be self-registered.
- Staff accounts (Doctor, Receptionist, Pharmacist, Admin) must be created by an administrator through the Staff Management module.

---

## 6.2.3. Use-case Specification: "Forgot Password"

### 6.2.3.1. Brief Description

Allows users who have forgotten their password to reset it through email verification.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The user selects the "Forgot Password" link on the login page.
2. The system displays a form requesting the user's registered email address.
3. The user enters their email and clicks "Send Reset Link."
4. The system searches for the account associated with the email.
5. The system generates a unique reset token with a 1-hour expiration time.
6. The system stores the token and sends an email containing a password reset link.
7. The system displays a confirmation message: "Check your email for reset instructions."
8. The user clicks the reset link from their email.
9. The system validates the token and its expiration.
10. The system displays a form to enter a new password (with confirmation).
11. The user enters and confirms the new password, then clicks "Reset Password."
12. The system validates the new password, hashes it, and updates the account.
13. The system invalidates the reset token and redirects to the login page with a success message.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Email Not Found

If the email is not registered in the system, the system still displays the same success message (for security reasons) but does not send any email.

##### 6.2.3.2.2.2. Invalid or Expired Token

If the reset link contains an invalid or expired token, the system displays the error message "Reset link is invalid or expired" and prompts the user to request a new reset link.

##### 6.2.3.2.2.3. Password Validation Failed

If the new password does not meet requirements, the system displays specific error messages and allows the user to re-enter.

### 6.2.3.3. Special Requirements

- Reset token must be cryptographically random and expire after 1 hour.
- For security, the system should not reveal whether an email exists in the database.
- New password must meet the same requirements as registration (min 8 chars, uppercase, lowercase, numbers).
- Old reset tokens must be invalidated when a new one is requested.

### 6.2.3.4. Pre-condition

- The user has a registered account with a valid email address.

### 6.2.3.5. Post-condition

- If successful: The password is updated, user can log in with the new password.
- If unsuccessful: The system displays appropriate error messages and guides the user.

### 6.2.3.6. Extension Points

- User can request a new reset link if the previous one expired.
- After successful reset, user is redirected to login page to authenticate with new credentials.

---

## 6.2.4. Use-case Specification: "Manage Profile"

### 6.2.4.1. Brief Description

Allows authenticated users to view and update their personal profile information.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The user selects the "Profile" or "My Account" option from the navigation menu.
2. The system retrieves and displays the user's current profile information (name, email, phone, address, etc.).
3. The user clicks "Edit Profile" to modify their information.
4. The system displays an editable form with the current values pre-populated.
5. The user modifies the desired fields and clicks "Save Changes."
6. The system validates the updated information.
7. The system updates the user's profile in the database.
8. The system displays a success message: "Profile updated successfully."

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. Validation Error

If any updated field contains invalid data, the system displays specific error messages and retains the form for correction.

##### 6.2.4.2.2.2. Email Already In Use

If the user tries to change their email to one that is already registered, the system displays "This email is already in use" and prevents the update.

##### 6.2.4.2.2.3. Change Password

If the user wants to change their password, they must provide the current password for verification before setting a new one.

### 6.2.4.3. Special Requirements

- Users cannot change their username or role.
- Email changes may require re-verification depending on security settings.
- Password changes require current password verification.
- Profile changes should be logged for audit purposes.

### 6.2.4.4. Pre-condition

- The user is authenticated and logged into the system.

### 6.2.4.5. Post-condition

- If successful: The user's profile information is updated in the database.
- If unsuccessful: The system displays error messages and retains the original data.

### 6.2.4.6. Extension Points

- Users can upload or change their profile picture.
- Users can enable/disable notification preferences.
- Password change functionality is available within the profile management
- 11a.2. Cho phép yêu cầu reset mới
- 11a.3. Quay lại bước 1

### Ràng buộc nghiệp vụ

# Manage Staff Use Case Specifications

---

## 6.2.1. Use-case Specification: "View and Filter Staff"

### 6.2.1.1. Brief Description

Allows administrators to view a list of all staff members and filter by role or status.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The admin selects the "Staff Management" option from the navigation menu.
2. The system retrieves and displays a paginated list of staff members with basic information (name, role, email, phone, status).
3. The admin applies filters such as role (Doctor, Receptionist, Pharmacy Staff) or status (Active, Inactive).
4. The system filters the list according to the selected criteria.
5. The system displays the filtered results.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Staff Found

If no staff members match the filter criteria, the system displays "No staff members found" and allows the admin to modify filters.

##### 6.2.1.2.2.2. System Error

If the system fails to retrieve staff data, it displays an error message and provides a retry option.

### 6.2.1.3. Special Requirements

- The list should be sortable by name, role, or hire date.
- Search functionality should support partial matching on name and email.
- Staff count per role should be displayed for quick overview.

### 6.2.1.4. Pre-condition

- The user is authenticated as Admin.

### 6.2.1.5. Post-condition

- If successful: The staff list is displayed according to the applied filters.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- Clicking on a staff member opens View Staff Details.
- Admin can access Add New Staff, Edit, or Delete functions from this view.

---

## 6.2.2. Use-case Specification: "View Staff Details"

### 6.2.2.1. Brief Description

Allows administrators to view complete details of a staff member including personal information and work schedule.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The admin selects a staff member from the list.
2. The system retrieves the complete staff record from the database.
3. The system displays detailed information including: personal details (name, email, phone, address), role, hire date, account status, and current work schedule.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Staff Not Found

If the staff record has been deleted, the system displays "Staff member not found" and returns to the list.

### 6.2.2.3. Special Requirements

- Display work schedule in a calendar format when applicable.
- Show appointment statistics for doctors (total patients, today's appointments).
- Include account status and last login information.

### 6.2.2.4. Pre-condition

- The user is authenticated as Admin.
- The staff member exists in the system.

### 6.2.2.5. Post-condition

- If successful: Complete staff details are displayed.
- If unsuccessful: An error message is shown and admin returns to the list.

### 6.2.2.6. Extension Points

- Admin can Edit Staff, Delete Staff, or Manage Staff Schedule from the details view.
- Admin can lock/unlock the staff member's account.

---

## 6.2.3. Use-case Specification: "Add New Staff"

### 6.2.3.1. Brief Description

Allows administrators to add new staff members to the system and create their accounts.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The admin selects "Add New Staff" from the staff management screen.
2. The system displays a form with fields for personal information (name, email, phone, address, date of birth, gender).
3. The admin selects the staff role (Doctor, Receptionist, Pharmacy Staff).
4. The admin enters all required information.
5. The admin clicks "Save" to create the staff member.
6. The system validates all input data.
7. The system checks for duplicate email addresses.
8. The system creates a new Account with the appropriate role.
9. The system creates the Staff record linked to the account.
10. The system generates a temporary password and sends login credentials via email.
11. The system displays a success message.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Email Already Exists

If the email is already registered, the system displays "An account with this email already exists" and prevents creation.

##### 6.2.3.2.2.2. Validation Error

If required fields are missing or invalid, the system highlights the errors and prevents submission.

### 6.2.3.3. Special Requirements

- Email must be unique across all accounts.
- Temporary password should be randomly generated and meet security requirements.
- New staff should be prompted to change password on first login.

### 6.2.3.4. Pre-condition

- The user is authenticated as Admin.
- The email is not already registered in the system.

### 6.2.3.5. Post-condition

- If successful: A new Account and Staff record are created, credentials are sent to the staff member.
- If unsuccessful: No records are created and error messages guide the admin.

### 6.2.3.6. Extension Points

- After creation, admin can immediately set up the staff member's work schedule.
- For doctors, additional specialization information can be added.

---

## 6.2.4. Use-case Specification: "Edit Staff"

### 6.2.4.1. Brief Description

Allows administrators to modify the information of an existing staff member.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The admin selects a staff member and clicks "Edit."
2. The system displays a form pre-populated with current staff information.
3. The admin modifies the desired fields (name, phone, address, role, status).
4. The admin clicks "Save" to update the staff record.
5. The system validates the updated data.
6. The system checks for conflicts (e.g., email uniqueness if changed).
7. The system updates the staff record.
8. The system displays a success message.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. Email Conflict

If the new email conflicts with another existing account, the system displays an error and prevents the update.

##### 6.2.4.2.2.2. Role Change Warning

If the admin changes the staff role, the system warns about potential schedule and access implications.

### 6.2.4.3. Special Requirements

- Role changes should be carefully handled as they affect permissions.
- Email changes may require re-verification.
- All modifications should be logged for audit purposes.

### 6.2.4.4. Pre-condition

- The user is authenticated as Admin.
- The staff member exists in the system.

### 6.2.4.5. Post-condition

- If successful: The staff information is updated in the database.
- If unsuccessful: The original data remains unchanged with an appropriate message.

### 6.2.4.6. Extension Points

- Admin can lock/unlock the staff account during editing.
- Admin can reset the staff member's password.

---

## 6.2.5. Use-case Specification: "Delete Staff"

### 6.2.5.1. Brief Description

Allows administrators to remove a staff member from the system, provided they have no associated data.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The admin selects a staff member and clicks "Delete."
2. The system checks if the staff member has any associated data (appointments, medical records, etc.).
3. If no associated data exists, the system displays a confirmation dialog.
4. The admin confirms the deletion.
5. The system deletes the Staff record and associated Account.
6. The system displays a success message and refreshes the list.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. Staff Has Associated Data

If the staff member has associated records, the system displays "Cannot delete: This staff member has X associated records" and suggests deactivation instead.

##### 6.2.5.2.2.2. Deletion Cancelled

If the admin cancels the confirmation, the staff record remains unchanged.

### 6.2.5.3. Special Requirements

- Referential integrity must be maintained; staff with associated data cannot be hard-deleted.
- Consider implementing soft delete (deactivation) for staff with history.
- Deletion should be logged for audit purposes.

### 6.2.5.4. Pre-condition

- The user is authenticated as Admin.
- The staff member exists in the system.
- The staff member has no associated appointments, medical records, or other data.

### 6.2.5.5. Post-condition

- If successful: The staff member and their account are removed from the system.
- If unsuccessful: The staff record remains with an explanatory message.

### 6.2.5.6. Extension Points

- If deletion is blocked, admin can deactivate the account instead.
- Admin can view associated records before deciding on action.

---

## 6.2.6. Use-case Specification: "Manage Staff Schedule"

### 6.2.6.1. Brief Description

Allows administrators to view and modify work schedules for staff members.

### 6.2.6.2. Event Flow

#### 6.2.6.2.1. Main Event Flow

1. The admin selects a staff member and clicks "Manage Schedule."
2. The system displays the current schedule in a calendar view.
3. The admin can add new work shifts by selecting a date and time slot.
4. The admin can modify existing shifts by clicking and editing.
5. The admin can delete shifts by selecting and removing them.
6. The system validates that shifts don't overlap.
7. The system saves all schedule changes.
8. The system displays a success message.

#### 6.2.6.2.2. Alternative Event Flows

##### 6.2.6.2.2.1. Schedule Conflict

If a new shift overlaps with an existing one, the system displays "Schedule conflict detected" and prevents the addition.

##### 6.2.6.2.2.2. Shift Has Appointments

If the admin tries to delete a shift that has scheduled appointments, the system warns about affected appointments.

### 6.2.6.3. Special Requirements

- Schedule should support recurring shifts (e.g., every Monday 9 AM - 5 PM).
- Changes to schedules should notify affected staff via email.
- Schedule must be visible to the appointment booking system.

### 6.2.6.4. Pre-condition

- The user is authenticated as Admin.
- The staff member exists in the system.

### 6.2.6.5. Post-condition

- If successful: The staff schedule is updated and available for appointment booking.
- If unsuccessful: The schedule remains unchanged with appropriate error messages.

### 6.2.6.6. Extension Points

- Staff members can view their own schedule (read-only).
- Schedule changes can trigger notifications to patients with affected appointments.

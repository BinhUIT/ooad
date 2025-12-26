# Manage Patient Use Case Specifications

---

## 6.2.1. c

### 6.2.1.1. Brief Description

Allows staff to view a list of all patients and filter by various criteria such as name, phone, email, age, or gender.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The staff member selects the "Patient Management" option from the navigation menu.
2. The system retrieves and displays a paginated list of patients with PatientID, full name, phone, email, date of birth, appointment count, and medical record count.
3. The staff applies filters or enters search keywords (name, phone, email, age range, gender).
4. The staff clicks "Apply" or presses Enter to execute the search.
5. The system filters the list according to the selected criteria.
6. The system displays the filtered results with pagination.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Patients in System

If no patients exist in the system, the system displays "No patients in the system yet."

##### 6.2.1.2.2.2. No Matching Results

If no patients match the filter criteria, the system displays "No patients found matching your criteria" and allows the user to clear filters.

### 6.2.1.3. Special Requirements

- Pagination should display 20-50 records per page.
- Database indexes on phone_number, email, and full_name for performance.
- Support export to Excel/PDF formats.

### 6.2.1.4. Pre-condition

- The user is authenticated as Staff (Receptionist, Doctor, or Admin).

### 6.2.1.5. Post-condition

- If successful: The patient list is displayed according to the applied filters with pagination.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- Clicking on a patient row opens View Patient Details.
- Staff can access Add New Patient function from this view.

---

## 6.2.2. Use-case Specification: "View Patient Details"

### 6.2.2.1. Brief Description

Allows staff to view complete patient information including personal data, appointment history, medical records, and invoices.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The staff selects a patient and clicks "View Details."
2. The system retrieves the patient record and associated data.
3. The system displays a detailed view with multiple tabs:
   - Tab 1: Personal Information (name, phone, email, DOB, gender, address)
   - Tab 2: Appointment History (showing last 10 with "View All" option)
   - Tab 3: Medical Records (showing last 10 with doctor name, diagnosis, disease type)
   - Tab 4: Invoices (showing last 10 with date, amount, payment method, status)
4. The staff navigates between tabs to view different information.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Patient Not Found

If the patient has been deleted, the system displays "Patient not found" and returns to the list.

##### 6.2.2.2.2.2. No Records in Tab

If a specific tab has no records (e.g., no appointments), the system displays "No records found" in that tab section.

### 6.2.2.3. Special Requirements

- Display only 10 most recent records per tab for performance.
- Provide "View All" option for complete history.
- Medical records and prescriptions must be handled with privacy considerations.

### 6.2.2.4. Pre-condition

- The user is authenticated as Staff (Receptionist, Doctor, Pharmacist, or Admin).
- The patient exists in the system.

### 6.2.2.5. Post-condition

- If successful: Complete patient details are displayed with associated history.
- If unsuccessful: An error message is shown and user is returned to the list.

### 6.2.2.6. Extension Points

- Staff can Edit Patient or Delete Patient from the details view.
- Staff can create new appointment for the patient.
- Staff can create new reception for the patient.

---

## 6.2.3. Use-case Specification: "Add New Patient"

### 6.2.3.1. Brief Description

Allows receptionist or admin to register a new patient in the system with the option to create a login account.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The staff selects "Add New Patient" from the patient management screen.
2. The system displays a form with required fields (marked with _): Full Name_, Phone*, Date of Birth*, Gender\*, and optional fields: Email, Address, plus a checkbox "Create login account."
3. The staff enters the patient information.
4. The staff clicks "Save" to create the patient record.
5. The system validates the input data (format validation).
6. The system checks for duplicate phone number or email.
7. The system creates the patient record.
8. If "Create login account" is checked:
   - The system generates a username (from phone or email)
   - The system generates a random password
   - The system creates an Account record with role "PATIENT"
   - The system sends login credentials via email or SMS
9. The system displays a success message and navigates to the patient details page.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Validation Error

If required fields are empty or data format is invalid, the system displays specific error messages and highlights the problematic fields.

##### 6.2.3.2.2.2. Duplicate Phone or Email

If the phone number or email already exists, the system displays "A patient with this phone number/email already exists. Would you like to view their information?" with options to view existing patient or correct the data.

### 6.2.3.3. Special Requirements

- Phone and email must be unique across all patients.
- Phone number format: 10-11 digits.
- Email must follow standard email format.
- Date of birth must be in the past.
- If account is created, credentials must be sent securely.

### 6.2.3.4. Pre-condition

- The user is authenticated as Receptionist or Admin.
- The phone/email is not already registered.

### 6.2.3.5. Post-condition

- If successful: A new patient record is created, optionally with a login account.
- If unsuccessful: No record is created and error messages guide the staff.

### 6.2.3.6. Extension Points

- After creation, staff can immediately create an appointment or reception for the patient.
- The patient can log in to the patient portal if account was created.

---

## 6.2.4. Use-case Specification: "Edit Patient"

### 6.2.4.1. Brief Description

Allows receptionist or admin to update the information of an existing patient.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The staff selects a patient and clicks "Edit."
2. The system retrieves the current patient information.
3. The system displays a form pre-populated with existing data.
4. The staff modifies the patient information (name, phone, email, DOB, gender, address).
5. The staff clicks "Save" to update the record.
6. The system validates the new data.
7. The system checks that new phone/email doesn't conflict with other patients.
8. The system updates the patient record.
9. The system logs the changes for audit purposes.
10. The system displays a success message.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. Patient Not Found

If the patient has been deleted during editing, the system displays "Patient not found" and returns to the list.

##### 6.2.4.2.2.2. Validation Error

If the new data is invalid, the system displays error messages and keeps the form open for correction.

##### 6.2.4.2.2.3. Duplicate Phone/Email

If the new phone or email conflicts with another patient, the system displays "This phone number/email is already in use by another patient."

### 6.2.4.3. Special Requirements

- PatientID cannot be modified.
- Phone and email must remain unique.
- All changes must be logged for audit trail.

### 6.2.4.4. Pre-condition

- The user is authenticated as Receptionist or Admin.
- The patient exists in the system.

### 6.2.4.5. Post-condition

- If successful: The patient information is updated in the database.
- If unsuccessful: The original data remains unchanged with an appropriate message.

### 6.2.4.6. Extension Points

- Staff can manage patient's account (reset password, deactivate) from this view.

---

## 6.2.5. Use-case Specification: "Delete Patient"

### 6.2.5.1. Brief Description

Allows admin to remove a patient from the system, provided the patient has no medical records.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The admin selects a patient and clicks "Delete."
2. The system displays a confirmation dialog with patient information.
3. The admin confirms the deletion.
4. The system checks if the patient has any medical records.
5. If no medical records exist, the system deletes associated appointments.
6. The system deletes the associated account (if exists).
7. The system deletes the patient record.
8. The system logs the deletion for audit trail.
9. The system displays a success message and refreshes the list.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. Has Medical Records

If the patient has medical records, the system displays "Cannot delete patient with medical records. Medical data must be preserved." and suggests deactivating the patient instead.

##### 6.2.5.2.2.2. Deletion Cancelled

If the admin cancels the confirmation, the patient record remains unchanged.

### 6.2.5.3. Special Requirements

- Only Admin role can delete patients.
- Patients with medical records cannot be deleted (medical data preservation requirement).
- Deletion cascades to: Appointments and Account.
- All deletions must be logged in audit trail.
- Consider implementing soft delete (deactivation) instead of hard delete.

### 6.2.5.4. Pre-condition

- The user is authenticated as Admin.
- The patient exists in the system.
- The patient has no medical records.

### 6.2.5.5. Post-condition

- If successful: The patient and related data (appointments, account) are removed from the system.
- If unsuccessful: The patient record remains with an explanatory message.

### 6.2.5.6. Extension Points

- If deletion is blocked, admin can deactivate the patient instead.
- Admin can view the medical records that are blocking deletion.

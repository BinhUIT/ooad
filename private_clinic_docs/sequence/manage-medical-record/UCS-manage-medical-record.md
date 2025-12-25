# Manage Medical Record Use Case Specifications

---

## 6.2.1. Use-case Specification: "View Medical Records"

### 6.2.1.1. Brief Description

Allows doctors to view a list of medical records and access detailed information including symptoms, diagnosis, prescriptions, and services.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The doctor selects the "Medical Records" option from the navigation menu.
2. The system retrieves and displays a paginated list of medical records with patient information.
3. The doctor can filter records by patient name, date range, or disease type.
4. The doctor clicks on a record to view details.
5. The system displays complete information: patient demographics, symptoms, diagnosis, disease type, prescribed services, and associated prescriptions.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Records Found

If no medical records match the filter criteria, the system displays "No medical records found" and allows the doctor to modify filters.

##### 6.2.1.2.2.2. Access Denied

If a doctor tries to access records they don't have permission to view, the system denies access with an appropriate message.

### 6.2.1.3. Special Requirements

- Medical records must be secured and only accessible to authorized personnel.
- Records should display the most recent entries first by default.
- Patient privacy must be maintained according to healthcare regulations.

### 6.2.1.4. Pre-condition

- The user is authenticated as Doctor.
- At least one medical record exists in the system.

### 6.2.1.5. Post-condition

- If successful: Medical records are displayed according to the applied filters.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- From the detail view, doctor can Edit Medical Record or Add Prescription.
- Doctor can view linked invoices and prescriptions.

---

## 6.2.2. Use-case Specification: "Add Medical Record"

### 6.2.2.1. Brief Description

Allows doctors to create a new medical record for a patient during or after an examination.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The doctor selects a reception from the daily reception list to begin examination.
2. The system displays the examination form with patient information pre-populated.
3. The doctor enters medical information: symptoms, diagnosis, and selects the disease type.
4. The doctor selects any required services (X-ray, lab tests, etc.).
5. The doctor clicks "Save" to create the medical record.
6. The system validates all required fields.
7. The system creates the MedicalRecord entry in the database.
8. The system updates the Reception status to "In Examination" or "Done."
9. The system automatically creates an Invoice with default values.
10. The system displays a success message and offers to Add Prescription.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Validation Error

If required fields are missing or invalid, the system highlights the errors and prevents submission.

##### 6.2.2.2.2.2. Reception Already Processed

If the reception already has a medical record, the system notifies the doctor and offers to view the existing record.

### 6.2.2.3. Special Requirements

- All medical records must be linked to a valid reception.
- Timestamps must be automatically recorded for audit purposes.
- Invoice creation should be automatic to ensure billing integrity.

### 6.2.2.4. Pre-condition

- The user is authenticated as Doctor.
- A valid reception exists for the patient.
- The reception status is "Waiting" or "In Examination."

### 6.2.2.5. Post-condition

- If successful: A new medical record is created, reception status is updated, and an invoice is generated.
- If unsuccessful: No records are created and error messages guide the doctor.

### 6.2.2.6. Extension Points

- After creation, doctor can immediately proceed to Add Prescription.
- Doctor can add additional services to the generated invoice.

---

## 6.2.3. Use-case Specification: "Edit Medical Record"

### 6.2.3.1. Brief Description

Allows doctors to modify an existing medical record within a specified time window.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The doctor opens a medical record and clicks "Edit."
2. The system verifies that the record is within the editable time window.
3. The system displays an editable form with current values pre-populated.
4. The doctor modifies the symptoms, diagnosis, disease type, or services.
5. The doctor clicks "Save" to update the record.
6. The system validates the updated information.
7. The system updates the medical record and logs the modification.
8. The system displays a success message.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Edit Window Expired

If the medical record is outside the editable time window (e.g., created more than 24 hours ago), the system displays "This record can no longer be edited" and prevents modification.

##### 6.2.3.2.2.2. Concurrent Edit

If another user is editing the same record, the system warns about potential conflicts and may block the edit.

### 6.2.3.3. Special Requirements

- Edits must only be allowed within a configurable time window (e.g., same day or 24 hours).
- All modifications must be logged with timestamp and user information.
- Original data should be preserved in an audit trail.

### 6.2.3.4. Pre-condition

- The user is authenticated as Doctor.
- The medical record exists and is within the editable time window.
- The doctor has permission to edit the record (typically the creating doctor).

### 6.2.3.5. Post-condition

- If successful: The medical record is updated with changes logged.
- If unsuccessful: The original record remains unchanged with an appropriate message.

### 6.2.3.6. Extension Points

- After editing, doctor can update the associated prescription if needed.
- Changes to services may trigger invoice updates.

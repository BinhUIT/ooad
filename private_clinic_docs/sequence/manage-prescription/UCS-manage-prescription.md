# Manage Prescription Use Case Specifications

---

## 6.2.1. Use-case Specification: "View Prescriptions"

### 6.2.1.1. Brief Description

Allows doctors and pharmacy staff to view a list of prescriptions and access detailed medicine information.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The user selects the "Prescriptions" option from the navigation menu.
2. The system retrieves and displays a list of prescriptions with basic information (patient name, doctor name, date, status).
3. The user can filter prescriptions by date, patient, or dispensing status.
4. The user clicks on a prescription to view details.
5. The system displays complete prescription information including all medicines with dosage and usage instructions.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Prescriptions Found

If no prescriptions match the filter criteria, the system displays "No prescriptions found" and allows the user to modify filters.

##### 6.2.1.2.2.2. Access Denied

If a user tries to access prescriptions they don't have permission to view, the system denies access with an appropriate message.

### 6.2.1.3. Special Requirements

- Pharmacy staff should see pending prescriptions prioritized.
- Show dispensing status for each prescription (Pending, Partially Dispensed, Fully Dispensed).
- Include inventory warnings for medicines that are low in stock.

### 6.2.1.4. Pre-condition

- The user is authenticated as Doctor or Pharmacy Staff.

### 6.2.1.5. Post-condition

- If successful: The prescription list is displayed according to the applied filters.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- Doctors can access Add Prescription from this view.
- Pharmacy staff can access Dispense Medicine from prescription details.

---

## 6.2.2. Use-case Specification: "Add Prescription"

### 6.2.2.1. Brief Description

Allows doctors to create a new prescription for a patient after completing a medical examination.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The doctor opens a medical record and selects "Add Prescription."
2. The system displays a prescription form with patient information.
3. The doctor searches and selects medicines from the medicine catalog.
4. For each medicine, the doctor enters: quantity, dosage, and usage instructions.
5. The system checks inventory availability and displays warnings for unavailable medicines.
6. The doctor reviews the prescription and clicks "Save."
7. The system creates the Prescription and PrescriptionDetails records.
8. The system displays a success message with inventory warnings if applicable.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Medicine Out of Stock

If a selected medicine is out of stock, the system displays a warning but allows the prescription to be created. The medicine is marked as "unavailable" for the pharmacy staff.

##### 6.2.2.2.2.2. No Medical Record

If no medical record exists for the visit, the system prompts the doctor to create one first.

### 6.2.2.3. Special Requirements

- Doctors can prescribe medicines regardless of inventory status.
- System should only alert doctors about unavailable medicines, not block prescription.
- Dosage and usage instructions should have common templates for quick selection.

### 6.2.2.4. Pre-condition

- The user is authenticated as Doctor.
- A valid medical record exists for the patient.

### 6.2.2.5. Post-condition

- If successful: A new prescription is created with all medicine details, linked to the medical record.
- If unsuccessful: No prescription is created and error messages guide the doctor.

### 6.2.2.6. Extension Points

- After creation, the prescription is available for Dispense Medicine by pharmacy staff.
- Prescription can be printed for the patient.

---

## 6.2.3. Use-case Specification: "Dispense Medicine"

### 6.2.3.1. Brief Description

Allows pharmacy staff to dispense prescribed medicines to patients, updating inventory and invoice accordingly.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The pharmacy staff searches for the patient by name or ID.
2. The system displays the patient's invoices with associated prescriptions.
3. The pharmacy staff selects an invoice and views the prescription.
4. The pharmacy staff selects medicines to dispense (only medicines available in inventory).
5. The system applies FEFO (First-Expire-First-Out) logic to select stock batches.
6. The system validates inventory quantities.
7. The pharmacy staff confirms the dispensing.
8. The system reduces inventory quantities for each dispensed medicine.
9. The system adds InvoiceMedicineDetail records and updates the invoice total.
10. The system prints medicine labels and usage instructions.
11. The system notifies about any medicines that could not be dispensed.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Insufficient Stock

If the requested quantity exceeds available stock, the system offers partial dispensing with the available quantity.

##### 6.2.3.2.2.2. Medicine Expired

If all available batches of a medicine are expired, the system prevents dispensing and alerts the staff.

##### 6.2.3.2.2.3. Medicine Not In Stock

If a prescribed medicine has no inventory at all, the system marks it as "patient must purchase externally" and continues with other medicines.

### 6.2.3.3. Special Requirements

- FEFO logic must be strictly applied to minimize expired medicine waste.
- Batch/lot numbers must be recorded for traceability.
- Real-time inventory updates to prevent overselling.
- Generate medicine labels with patient name, medicine name, dosage, and usage instructions.

### 6.2.3.4. Pre-condition

- The user is authenticated as Pharmacy Staff.
- A valid prescription exists for the patient.
- An invoice exists for the patient's visit.

### 6.2.3.5. Post-condition

- If successful: Medicines are dispensed, inventory is reduced, invoice is updated, labels are printed.
- If unsuccessful: No changes are made to inventory or invoice with appropriate error messages.

### 6.2.3.6. Extension Points

- After dispensing, patient receives a list of any medicines they need to purchase externally.
- Generate receipt for dispensed medicines.
- Update prescription status to reflect partial or full dispensing.

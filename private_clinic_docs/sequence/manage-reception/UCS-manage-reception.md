# Manage Reception Use Case Specifications

---

## 6.2.1. Use-case Specification: "View and Filter Receptions"

### 6.2.1.1. Brief Description

Allows receptionists to view a list of all patient receptions and filter by date, status, doctor, or patient.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The receptionist selects the "Receptions" option from the navigation menu.
2. The system retrieves and displays a list of receptions for the current day with basic information (patient name, doctor, time, status).
3. The receptionist applies filters such as date range, status, doctor, or patient name.
4. The system validates the filter criteria and retrieves matching receptions.
5. The system displays the filtered results sorted by appointment time.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Receptions Found

If no receptions match the filter criteria, the system displays "No receptions found" and allows the receptionist to modify filters.

##### 6.2.1.2.2.2. System Error

If the system fails to retrieve reception data, it displays an error message and provides a retry option.

### 6.2.1.3. Special Requirements

- Default view should show today's receptions.
- Status should be color-coded for quick identification (Waiting, In Examination, Done, Cancelled).
- Real-time updates when reception statuses change.

### 6.2.1.4. Pre-condition

- The user is authenticated as Receptionist.

### 6.2.1.5. Post-condition

- If successful: The reception list is displayed according to the applied filters.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- Clicking on a reception opens View Reception Details.
- Receptionist can access Add New Reception or Edit Reception Status from this view.

---

## 6.2.2. Use-case Specification: "View Reception Details"

### 6.2.2.1. Brief Description

Allows staff to view complete details of a reception including patient information, appointment details, and current status.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The user selects a reception from the list.
2. The system retrieves the complete reception record.
3. The system displays detailed information including: patient demographics, appointment details, symptoms/notes, current status, assigned doctor, and timeline of status changes.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Reception Not Found

If the reception has been deleted, the system displays "Reception not found" and returns to the list.

### 6.2.2.3. Special Requirements

- Display a timeline of all status changes with timestamps.
- Show linked medical record and invoice if available.
- Include patient contact information for easy reference.

### 6.2.2.4. Pre-condition

- The user is authenticated as Receptionist or Doctor.
- The reception exists in the system.

### 6.2.2.5. Post-condition

- If successful: Complete reception details are displayed.
- If unsuccessful: An error message is shown and user is returned to the list.

### 6.2.2.6. Extension Points

- Receptionist can Edit Reception Status from the details view.
- Doctor can access the medical record creation from this view.

---

## 6.2.3. Use-case Specification: "Add New Reception"

### 6.2.3.1. Brief Description

Allows receptionists to create a new reception record when a patient arrives for their appointment.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The receptionist selects an appointment from the day's appointment list.
2. The system displays a reception form with patient and appointment information pre-populated.
3. The receptionist enters additional information such as initial symptoms and notes.
4. The receptionist clicks "Create Reception."
5. The system validates the input data.
6. The system creates a new reception record with status "Waiting."
7. The system updates the appointment status.
8. The system displays a success message.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Reception Already Exists

If a reception already exists for this appointment, the system displays "A reception already exists for this appointment" and offers to view it.

##### 6.2.3.2.2.2. Walk-in Patient

If the patient is a walk-in (no appointment), the receptionist can create a reception by first creating an appointment or selecting an existing patient.

### 6.2.3.3. Special Requirements

- Reception must be linked to a valid appointment.
- Initial status is always "Waiting."
- Timestamp of creation should be recorded automatically.

### 6.2.3.4. Pre-condition

- The user is authenticated as Receptionist.
- A valid appointment exists for the patient.
- No reception exists for this appointment yet.

### 6.2.3.5. Post-condition

- If successful: A new reception is created with status "Waiting" and the patient is added to the doctor's queue.
- If unsuccessful: No reception is created and error messages guide the receptionist.

### 6.2.3.6. Extension Points

- After creation, receptionist can view the reception queue for each doctor.
- Reception creation can trigger a notification to the assigned doctor.

---

## 6.2.4. Use-case Specification: "Edit Reception Status"

### 6.2.4.1. Brief Description

Allows receptionists to update the status of a reception as the patient progresses through the clinic visit.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The receptionist selects a reception and clicks "Update Status."
2. The system displays the current status and available next statuses.
3. The receptionist selects the new status (Waiting → In Examination → Done).
4. The receptionist confirms the status change.
5. The system validates the status transition.
6. The system updates the reception status with timestamp.
7. If status is "In Examination," the system notifies the assigned doctor.
8. The system displays a success message.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. Invalid Status Transition

If the receptionist attempts an invalid status transition (e.g., Waiting → Done), the system displays "Invalid status change" and explains the valid transitions.

##### 6.2.4.2.2.2. Status Already Final

If the reception is already "Done" or "Cancelled," the system prevents further status changes.

### 6.2.4.3. Special Requirements

- Status transitions must follow a defined workflow.
- All status changes must be logged with timestamps.
- Status changes should trigger appropriate notifications.

### 6.2.4.4. Pre-condition

- The user is authenticated as Receptionist.
- The reception exists and is not in a final status.

### 6.2.4.5. Post-condition

- If successful: The reception status is updated, notifications are sent, and audit trail is updated.
- If unsuccessful: The status remains unchanged with an appropriate message.

### 6.2.4.6. Extension Points

- Status change to "In Examination" can display on doctor's dashboard.
- Status change to "Done" can trigger invoice generation.

---

## 6.2.5. Use-case Specification: "End Day/Close Receptions"

### 6.2.5.1. Brief Description

Allows receptionists to close all pending receptions at the end of the day, marking incomplete visits as cancelled.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The receptionist selects the "End Day" or "Close Receptions" function.
2. The system checks for all receptions still in "Waiting" or "In Examination" status.
3. The system displays a list of pending receptions with a warning message.
4. The receptionist reviews the list and confirms the end-of-day closure.
5. The system updates all pending receptions to "Cancelled" status with a reason "End of day closure."
6. The system logs the closure action with timestamp and user information.
7. The system displays a summary of closed receptions.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. No Pending Receptions

If all receptions are already completed, the system displays "All receptions are already complete" and no action is needed.

##### 6.2.5.2.2.2. Closure Cancelled

If the receptionist cancels the confirmation, all receptions remain unchanged.

### 6.2.5.3. Special Requirements

- This action should require confirmation to prevent accidental closure.
- Affected patients should be notified if possible.
- A report of cancelled receptions should be generated for management.

### 6.2.5.4. Pre-condition

- The user is authenticated as Receptionist.
- At least one pending reception exists.

### 6.2.5.5. Post-condition

- If successful: All pending receptions are marked as "Cancelled" and logged.
- If unsuccessful: Receptions remain unchanged.

### 6.2.5.6. Extension Points

- Option to reschedule appointments for cancelled receptions.
- Generate end-of-day report for management review.

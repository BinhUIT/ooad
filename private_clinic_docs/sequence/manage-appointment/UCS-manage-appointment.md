# Manage Appointment Use Case Specifications

## 6.2.1. Use-case Specification: View and Filter Appointments

### 6.2.1.1. Brief Description

Receptionists, doctors, and patients browse the clinic's appointment roster, applying filters such as date range, doctor, or status so they can focus on the slots most relevant to their needs.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The user selects the "View Appointments" option from the dashboard or menu.
2. The system displays an initial paginated list showing patient name, doctor, date and time, and current status.
3. The user applies filters (e.g., date range, doctor, status, clinic location) and submits the request.
4. The system validates the filters, retrieves matching appointments, sorts them by date/time, and refreshes the list with up-to-date records.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. Filters return no results

The system shows a friendly message ("No appointments match the selected filters"), retains the current filter selection, and leaves the list empty so the user can adjust inputs.

##### 6.2.1.2.2.2. Data retrieval fails

If the system cannot load appointments (e.g., database timeout), it displays an error banner and allows the user to retry or contact support.

### 6.2.1.3. Special Requirements

- Results must reflect changes made in the last minute to avoid stale scheduling information.
- Server-side filtering must support date ranges, doctor IDs, and status codes with efficient pagination.
- The displayed list should never surface more than 50 rows per page by default.

### 6.2.1.4. Pre-condition

The logged-in user is a receptionist, doctor, or patient with permission to view appointment data, and at least one appointment exists in the system.

### 6.2.1.5. Post-condition

The appointment list is refreshed according to the filters; the user may proceed to select an appointment for more actions or close the list.

### 6.2.1.6. Extension Points

- Selecting an appointment launches the View Appointment Details use case.
- Inline actions can branch to Edit Appointment or Delete/Cancel Appointment if permissions allow.

---

## 6.2.2. Use-case Specification: View Appointment Details

### 6.2.2.1. Brief Description

Authorized staff or patients inspect a selected appointment's comprehensive information so they can verify patient/doctor data, status, and any linked services.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The user clicks or taps an appointment entry from the list.
2. The system loads the appointment record along with patient demographics, doctor details, room, services requested, and status notes.
3. The system displays a detailed view or modal with contact information and relevant links (e.g., linked medical record or invoices).

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Appointment has been removed

If the record was deleted or cancelled since the list loaded, the system alerts the user that the appointment no longer exists and refreshes the list.

##### 6.2.2.2.2.2. Access denied

If the user lacks permission for the requested appointment (e.g., a patient viewing another patient's slot), the system shows an authorization error and stays on the list view.

### 6.2.2.3. Special Requirements

- The detail view must include contact information and allow for quick navigation to the patient profile or doctor schedule.
- Data must be read-only unless the user initiates an edit or cancellation workflow.

### 6.2.2.4. Pre-condition

An appointment is selected from the list and the user remains authenticated with the appropriate role.

### 6.2.2.5. Post-condition

The detailed information is presented; the user can close the view or trigger extensions (edit, cancel, invoice).

### 6.2.2.6. Extension Points

- "Edit" launches the Edit Appointment use case.
- "Cancel" navigates to the Delete/Cancel Appointment use case.
- "Print" or "Generate Invoice" links out to billing flows.

---

## 6.2.3. Use-case Specification: Add New Appointment

### 6.2.3.1. Brief Description

Receptionists or patients schedule a new appointment by selecting an available slot, patient, and doctor while the system enforces capacity and availability constraints.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The user activates the "Book Appointment" or "Add New Appointment" control.
2. The system displays a form requesting patient selection (existing or new), doctor, reason, preferred date/time, and optional notes.
3. The user chooses a time slot, doctor, and any required services, then submits the form.
4. The system verifies the slot against the doctor's schedule and daily patient limit for that doctor/clinic, ensuring no conflict.
5. Upon successful validation, the system persists the appointment with status "Scheduled" and dispatches a confirmation notification (email/SMS) to the patient and doctor.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Time slot unavailable

If the selected slot conflicts with an existing booking or blocked time, the system highlights the conflict, suggests the next available slots, and keeps the form data for correction.

##### 6.2.3.2.2.2. Daily limit reached

If the doctor or clinic has met the configurable daily limit, the system prevents saving and displays a message advising to choose another day or doctor.

### 6.2.3.3. Special Requirements

- Appointment creation should check both general availability and any leave/holidays on the doctor schedule.
- Notifications must be queued for delivery within 30 seconds of creation.
- Patient and doctor time zones must be aligned to prevent off-by-one-day errors.

### 6.2.3.4. Pre-condition

The user is authenticated and has permission to schedule appointments; the patient record exists (or is created on-the-fly); a doctor schedule is published.

### 6.2.3.5. Post-condition

A new appointment record exists with status "Scheduled," relevant staff/patient notified, and availability grids updated.

### 6.2.3.6. Extension Points

- After creation, the system can auto-create an initial medical record or pre-authorize services in the Add Medical Record workflow.
- Invoice creation or patient intake steps might be triggered from the confirmation screen.

---

## 6.2.4. Use-case Specification: Edit Appointment

### 6.2.4.1. Brief Description

Receptionists adjust existing appointments to update date/time, assigned doctor, or status while verifying new slot availability.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The user opens an appointment from the list or detail view and chooses "Edit."
2. The system pre-populates the edit form with current details and refreshes available time slots for the selected doctor or clinic.
3. The user modifies the appointment data (date, time, doctor, status, notes) and saves changes.
4. The system validates the new slot, ensures it does not collide with other commitments, and updates the appointment record.
5. A notification is sent to affected parties about the change.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. New slot conflict

If the new date/time conflicts with another booking or clinic closure, the system warns the user and keeps the edited form for correction.

##### 6.2.4.2.2.2. Editing prohibited

If the appointment is already marked as completed or in progress, the system blocks the edit and explains that only future or scheduled appointments can be modified.

### 6.2.4.3. Special Requirements

- Changes must be logged with timestamps and user identity for audit purposes.
- The edit form must respect the same validation rules as creation (slot available, daily limits).

### 6.2.4.4. Pre-condition

The appointment exists with status "Scheduled" or similar, and the user has rights to modify the slot.

### 6.2.4.5. Post-condition

The appointment reflects the updated input; schedules and notifications are updated accordingly.

### 6.2.4.6. Extension Points

- After editing, the user may proceed to Generate Invoice if services changed, or re-open View Appointment Details.

---

## 6.2.5. Use-case Specification: Delete/Cancel Appointment

### 6.2.5.1. Brief Description

Receptionists or patients cancel appointments that no longer need to occur, triggering status updates and cancellation notices.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The user selects an appointment and chooses "Cancel" or "Delete."
2. The system prompts for confirmation and an optional cancellation reason.
3. Upon confirmation, the system updates the appointment status to "Cancelled," frees the slot in the schedule, and notifies the patient and doctor.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. Appointment already started or completed

If the appointment is marked as in progress or done, the system rejects the cancellation and explains that only scheduled appointments can be cancelled.

##### 6.2.5.2.2.2. Cancellation aborted

If the user dismisses the confirmation, the appointment remains unchanged and the list returns to its previous state.

### 6.2.5.3. Special Requirements

- Cancellation must capture a reason and time of action for reporting.
- Notifications should be dispatched immediately so staff can update their workload.

### 6.2.5.4. Pre-condition

The appointment exists, is scheduled (not yet started), and the user has permission to cancel it.

### 6.2.5.5. Post-condition

The appointment status becomes "Cancelled," availability is restored, and notifications are sent; dependent workflows (medical records, invoices) are aware of the change.

### 6.2.5.6. Extension Points

- After cancellation, provide a direct link to book a new appointment or reactivate a future slot.
- Trigger downstream billing adjustments if payment was already processed.

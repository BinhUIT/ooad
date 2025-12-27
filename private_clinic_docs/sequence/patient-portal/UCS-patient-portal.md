# Patient Portal Use Case Specifications

---

## 6.2.1. Use-case Specification: "Book Appointment Online"

### 6.2.1.1. Brief Description

Allows logged-in patients to schedule new appointments with doctors through the online patient portal.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The patient selects the "Book Appointment" option from the patient portal dashboard.
2. The system displays available doctors with their specializations and schedules.
3. The patient selects a preferred doctor.
4. The system displays the doctor's available time slots based on their schedule.
5. The patient selects a date and time slot.
6. The system verifies the slot is still available.
7. The patient enters the reason for the visit and any additional notes.
8. The patient confirms the appointment booking.
9. The system creates the appointment with status "Scheduled."
10. The system sends a confirmation email/SMS to the patient.
11. The system displays a booking confirmation with appointment details.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. Slot No Longer Available

If the selected time slot was booked by another patient during the booking process, the system displays "This time slot is no longer available" and prompts to select another slot.

##### 6.2.1.2.2.2. Daily Limit Reached

If the doctor or clinic has reached the maximum number of patients for the selected day, the system displays "No more appointments available for this day" and suggests alternative dates.

##### 6.2.1.2.2.3. Past Date Selected

If the patient tries to book for a past date, the system prevents the action and displays "Please select a future date."

### 6.2.1.3. Special Requirements

- Appointments can only be booked for future dates.
- Real-time availability checking to prevent double-booking.
- Respect doctor's working schedule and clinic operating hours.
- Maximum patients per day limit must be enforced.

### 6.2.1.4. Pre-condition

- The user is authenticated as Patient.
- At least one doctor has an available schedule.

### 6.2.1.5. Post-condition

- If successful: A new appointment is created with status "Scheduled," confirmation is sent to patient.
- If unsuccessful: No appointment is created and error message guides the patient.

### 6.2.1.6. Extension Points

- Patient can cancel the appointment before the scheduled date.
- Patient receives reminder notifications before the appointment.

---

## 6.2.2. Use-case Specification: "View Medical Record History"

### 6.2.2.1. Brief Description

Allows logged-in patients to view their own medical record history including diagnoses and prescriptions.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The patient selects the "Medical History" option from the patient portal.
2. The system retrieves all medical records belonging to the patient.
3. The system displays a list of medical records with examination date, doctor name, and diagnosis summary.
4. The patient selects a record to view details.
5. The system displays complete information: examination date, doctor, symptoms, diagnosis, disease type, services performed, and associated prescription.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. No Medical Records

If the patient has no medical records, the system displays "You don't have any medical records yet."

##### 6.2.2.2.2.2. Access Restricted

If certain sensitive information is restricted, the system displays only authorized information.

### 6.2.2.3. Special Requirements

- Patients can only view their own medical records.
- Records should be displayed in reverse chronological order (newest first).
- Prescription details including dosage and usage instructions must be visible.

### 6.2.2.4. Pre-condition

- The user is authenticated as Patient.

### 6.2.2.5. Post-condition

- If successful: The patient's medical record history is displayed.
- If unsuccessful: An error message is shown.

### 6.2.2.6. Extension Points

- Patient can download or print medical records.
- Patient can view prescription details and usage instructions.

---

## 6.2.3. Use-case Specification: "View Appointment History"

### 6.2.3.1. Brief Description

Allows logged-in patients to view their appointment history including past and upcoming appointments with the ability to cancel future appointments.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The patient selects the "My Appointments" option from the patient portal.
2. The system retrieves all appointments belonging to the patient.
3. The system displays appointments in two sections: Upcoming and Past.
4. The patient can filter appointments by status (Scheduled, Completed, Cancelled).
5. For upcoming appointments, the patient sees a "Cancel" option.
6. The patient selects an appointment to view details.
7. The system displays appointment details including date, time, doctor, and status.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. No Appointments

If the patient has no appointments, the system displays "You don't have any appointments" with a link to book a new one.

##### 6.2.3.2.2.2. Cancel Appointment

If the patient chooses to cancel a future appointment:

1. The system displays a confirmation dialog.
2. The patient confirms cancellation.
3. The system updates the appointment status to "Cancelled."
4. The system sends a cancellation notification.

### 6.2.3.3. Special Requirements

- Only future appointments can be cancelled by patients.
- Cancellation may have restrictions (e.g., not allowed within 24 hours of appointment).
- Patients can only view and manage their own appointments.

### 6.2.3.4. Pre-condition

- The user is authenticated as Patient.

### 6.2.3.5. Post-condition

- If successful: The patient's appointment history is displayed.
- If unsuccessful: An error message is shown.

### 6.2.3.6. Extension Points

- Patient can reschedule an appointment (cancel and book new).
- Patient can add the appointment to their calendar.

---

## 6.2.4. Use-case Specification: "View Invoice History"

### 6.2.4.1. Brief Description

Allows logged-in patients to view their invoice history including services, medicines, and payment status.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The patient selects the "My Invoices" or "Billing History" option from the patient portal.
2. The system retrieves all invoices belonging to the patient.
3. The system displays a list of invoices with date, total amount, and payment status.
4. The patient selects an invoice to view details.
5. The system displays complete invoice information: services rendered with prices, medicines dispensed with prices, subtotals, any discounts, total amount, and payment history.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. No Invoices

If the patient has no invoices, the system displays "You don't have any invoices."

##### 6.2.4.2.2.2. Unpaid Invoice Alert

If there are unpaid invoices, the system highlights them and may display a reminder message.

### 6.2.4.3. Special Requirements

- Patients can only view their own invoices.
- Payment status should be clearly visible (Paid, Unpaid, Partial).
- Detailed breakdown of all charges must be provided.

### 6.2.4.4. Pre-condition

- The user is authenticated as Patient.

### 6.2.4.5. Post-condition

- If successful: The patient's invoice history is displayed.
- If unsuccessful: An error message is shown.

### 6.2.4.6. Extension Points

- Patient can download or print invoices.
- Patient may be able to make online payments for unpaid invoices (if feature is enabled).

---

## 6.2.5. Use-case Specification: "Patient Dashboard/Home"

### 6.2.5.1. Brief Description

Displays a personalized dashboard for logged-in patients with a summary of their appointments, medical records, and invoices.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The patient logs into the system.
2. The system retrieves summary data for the patient.
3. The system displays the patient dashboard with:
   - Upcoming appointments (next 1-3)
   - Recent medical record summary
   - Unpaid invoices (if any)
   - Quick action buttons (Book Appointment, View History, etc.)
4. The patient can navigate to detailed views by clicking on dashboard items.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. No Upcoming Appointments

If there are no upcoming appointments, the dashboard shows "No upcoming appointments" with a prominent "Book Now" button.

##### 6.2.5.2.2.2. No Recent Records

If there are no recent medical records, that section is minimized or shows a welcome message for new patients.

### 6.2.5.3. Special Requirements

- Dashboard should load quickly with essential data.
- Information should be personalized to the logged-in patient.
- Quick actions should provide easy access to common features.
- Unpaid invoices should be highlighted for attention.

### 6.2.5.4. Pre-condition

- The user is authenticated as Patient.

### 6.2.5.5. Post-condition

- If successful: The personalized dashboard is displayed with summary information.
- If unsuccessful: An error message is shown or default dashboard is displayed.

### 6.2.5.6. Extension Points

- Dashboard widgets can be customized by the patient.
- Notifications and alerts are displayed prominently.
- Quick links to patient profile management.

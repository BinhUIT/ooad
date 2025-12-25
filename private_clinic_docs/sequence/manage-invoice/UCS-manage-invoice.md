# Manage Invoice Use Case Specifications

---

## 6.2.1. Use-case Specification: "View and Filter Invoices"

### 6.2.1.1. Brief Description

Allows staff members to view a list of all invoices and apply filters to find specific invoices based on various criteria.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The staff member selects the "Invoices" or "Invoice Management" option from the navigation menu.
2. The system retrieves and displays a paginated list of invoices with basic information (invoice ID, patient name, date, total amount, payment status).
3. The staff member applies filters such as date range, payment status, or patient name.
4. The system validates the filter criteria and retrieves matching invoices.
5. The system displays the filtered results sorted by date (newest first).

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Invoices Found

If no invoices match the filter criteria, the system displays a message "No invoices found matching your criteria" and allows the user to modify filters.

##### 6.2.1.2.2.2. System Error

If the system fails to retrieve invoices, it displays an error message and provides a retry option.

### 6.2.1.3. Special Requirements

- The list must be paginated with a configurable number of items per page (default: 20).
- Filters must support date range, payment status (Paid, Unpaid, Partial), and patient search.
- The system should display real-time totals for filtered results.

### 6.2.1.4. Pre-condition

- The user is authenticated as Receptionist or Pharmacy Staff.
- At least one invoice exists in the system.

### 6.2.1.5. Post-condition

- If successful: The invoice list is displayed according to the applied filters.
- If unsuccessful: An error message is shown and the user can retry.

### 6.2.1.6. Extension Points

- Clicking on an invoice opens the View Invoice Details use case.
- Export functionality allows generating PDF/Excel reports of filtered invoices.

---

## 6.2.2. Use-case Specification: "View Invoice Details"

### 6.2.2.1. Brief Description

Allows users to view complete details of a specific invoice including services, medicines, and payment information.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The user selects an invoice from the invoice list.
2. The system retrieves the complete invoice record from the database.
3. The system displays detailed information including: patient details, services rendered with prices, medicines dispensed with quantities and prices, subtotals, discounts (if any), total amount, payment status, and payment history.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Invoice Not Found

If the invoice has been deleted or does not exist, the system displays "Invoice not found" and returns to the invoice list.

##### 6.2.2.2.2.2. Access Denied

If a patient tries to view another patient's invoice, the system denies access and displays an authorization error.

### 6.2.2.3. Special Requirements

- Invoice details must show itemized breakdown of all charges.
- Payment history should display all partial payments with timestamps.
- The system should calculate and display any outstanding balance.

### 6.2.2.4. Pre-condition

- The user is authenticated as Receptionist, Pharmacy Staff, or the Patient who owns the invoice.
- The invoice exists in the system.

### 6.2.2.5. Post-condition

- If successful: Complete invoice details are displayed to the user.
- If unsuccessful: An appropriate error message is shown.

### 6.2.2.6. Extension Points

- Users can proceed to Process Payment if there is an outstanding balance.
- Users can Print Invoice from the details view.
- Staff can Add Service or Add Medicine to modify the invoice.

---

## 6.2.3. Use-case Specification: "Add Service to Invoice"

### 6.2.3.1. Brief Description

Allows receptionists to add medical services to an existing invoice and automatically recalculate the total amount.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The receptionist opens an invoice and selects "Add Service."
2. The system displays a list of available services with their current prices.
3. The receptionist searches or browses to find the desired service.
4. The receptionist selects a service and specifies the quantity if applicable.
5. The system validates the selection and adds the service to the invoice.
6. The system recalculates the invoice total and updates the display.
7. The system displays a success message confirming the service was added.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Service Already Added

If the service is already on the invoice, the system prompts whether to increase the quantity or cancel the operation.

##### 6.2.3.2.2.2. Invoice Already Paid

If the invoice is fully paid, the system prevents modification and displays "Cannot modify a paid invoice."

### 6.2.3.3. Special Requirements

- Services must be added with their current effective price.
- The system must maintain an audit trail of invoice modifications.
- Price changes after adding a service should not affect the invoice.

### 6.2.3.4. Pre-condition

- The user is authenticated as Receptionist.
- The invoice exists and is not fully paid.
- At least one service is available in the system.

### 6.2.3.5. Post-condition

- If successful: The service is added to the invoice and totals are recalculated.
- If unsuccessful: The invoice remains unchanged and an error message is displayed.

### 6.2.3.6. Extension Points

- After adding services, the receptionist can proceed to Process Payment.
- Multiple services can be added in sequence without closing the dialog.

---

## 6.2.4. Use-case Specification: "Add Medicine to Invoice"

### 6.2.4.1. Brief Description

Allows pharmacy staff to add dispensed medicines from a prescription to the invoice and automatically recalculate the total.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The pharmacy staff opens an invoice and selects "Add Medicine."
2. The system displays the associated prescription with medicine details.
3. The pharmacy staff selects medicines to dispense from the prescription.
4. The system checks inventory availability for selected medicines.
5. The system applies FEFO (First-Expire-First-Out) logic to select stock.
6. The system adds the medicines to the invoice with current prices.
7. The system updates inventory quantities and recalculates the invoice total.
8. The system displays a success message with a summary of added items.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. Insufficient Stock

If the requested quantity exceeds available stock, the system displays a warning and allows partial dispensing or cancellation.

##### 6.2.4.2.2.2. Medicine Expired

If all available stock for a medicine is expired, the system prevents dispensing and notifies the staff.

### 6.2.4.3. Special Requirements

- FEFO logic must be applied to prioritize medicines expiring soonest.
- Inventory must be updated in real-time to prevent overselling.
- Batch/lot numbers should be recorded for traceability.

### 6.2.4.4. Pre-condition

- The user is authenticated as Pharmacy Staff.
- The invoice exists with an associated prescription.
- The invoice is not fully paid.

### 6.2.4.5. Post-condition

- If successful: Medicines are added to invoice, inventory is reduced, totals are updated.
- If unsuccessful: No changes are made and appropriate error messages are shown.

### 6.2.4.6. Extension Points

- Staff can print medicine labels and usage instructions after dispensing.
- If some medicines are unavailable, staff can note them for external purchase.

---

## 6.2.5. Use-case Specification: "Process Payment"

### 6.2.5.1. Brief Description

Allows receptionists to process payments for invoices using various payment methods and update the payment status accordingly.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The receptionist opens an invoice and selects "Process Payment."
2. The system displays the outstanding balance and available payment methods.
3. The receptionist selects a payment method (Cash, Card, Bank Transfer, etc.).
4. The receptionist enters the payment amount.
5. The system validates the payment amount against the outstanding balance.
6. The system creates a payment record and updates the invoice status.
7. If fully paid, the system marks the invoice as "Paid." If partially paid, status becomes "Partial."
8. The system displays a payment confirmation with receipt details.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. Overpayment

If the payment amount exceeds the balance, the system displays the change amount to be returned and processes the exact balance.

##### 6.2.5.2.2.2. Payment Failed

If payment processing fails (e.g., card declined), the system displays an error message and allows retry with a different method.

### 6.2.5.3. Special Requirements

- Multiple partial payments must be supported for a single invoice.
- All payment transactions must be logged with timestamps.
- Cash payments should calculate and display change amounts.

### 6.2.5.4. Pre-condition

- The user is authenticated as Receptionist.
- The invoice has an outstanding balance greater than zero.
- At least one payment method is configured in the system.

### 6.2.5.5. Post-condition

- If successful: Payment is recorded, invoice status is updated, receipt is generated.
- If unsuccessful: No payment is recorded and an error message is displayed.

### 6.2.5.6. Extension Points

- After payment, the system can automatically print a receipt.
- For card payments, integration with payment gateway may be triggered.

---

## 6.2.6. Use-case Specification: "Print Invoice"

### 6.2.6.1. Brief Description

Allows users to generate and print a formatted PDF version of an invoice for record-keeping or patient receipt.

### 6.2.6.2. Event Flow

#### 6.2.6.2.1. Main Event Flow

1. The user opens an invoice and selects "Print Invoice."
2. The system generates a PDF document with clinic header, invoice details, itemized charges, totals, and payment status.
3. The system displays a print preview to the user.
4. The user confirms to print or download the PDF.
5. The system sends the document to the printer or initiates download.

#### 6.2.6.2.2. Alternative Event Flows

##### 6.2.6.2.2.1. PDF Generation Failed

If the system fails to generate the PDF, it displays an error message and offers to retry.

##### 6.2.6.2.2.2. Printer Unavailable

If no printer is available, the system offers only the download option.

### 6.2.6.3. Special Requirements

- The PDF must include clinic logo, address, and contact information.
- Invoice must be formatted for standard paper sizes (A4, Letter).
- The document should include a unique invoice number and barcode/QR code.

### 6.2.6.4. Pre-condition

- The user is authenticated as Receptionist or the Patient who owns the invoice.
- The invoice exists in the system.

### 6.2.6.5. Post-condition

- If successful: A PDF invoice is generated and printed/downloaded.
- If unsuccessful: An error message is displayed and user can retry.

### 6.2.6.6. Extension Points

- Option to email the invoice PDF directly to the patient.
- Option to save invoice to patient's document history.

# Manage Payment Method Use Case Specifications

---

## 6.2.1. Use-case Specification: "View Payment Methods"

### 6.2.1.1. Brief Description

Allows administrators and receptionists to view a list of all available payment methods in the system.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The user selects the "Payment Methods" option from the settings or administration menu.
2. The system retrieves and displays a list of all payment methods with their names, descriptions, and status (Active/Inactive).
3. The user reviews the available payment methods.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Payment Methods

If no payment methods exist (unlikely scenario), the system displays "No payment methods configured" and prompts admin to add one.

##### 6.2.1.2.2.2. System Error

If the system fails to retrieve payment methods, it displays an error message and provides a retry option.

### 6.2.1.3. Special Requirements

- Active payment methods should be highlighted or listed first.
- The list should show usage statistics (how many payments processed with each method).

### 6.2.1.4. Pre-condition

- The user is authenticated as Admin or Receptionist.

### 6.2.1.5. Post-condition

- If successful: The payment method list is displayed.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- Admin users can access Add, Edit, or Delete functions from this view.
- Receptionists can only view the list (read-only).

---

## 6.2.2. Use-case Specification: "View Payment Method Details"

### 6.2.2.1. Brief Description

Allows administrators to view complete details of a specific payment method including usage history.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The admin selects a payment method from the list.
2. The system retrieves the complete payment method record.
3. The system displays detailed information including: method ID, name, description, status, creation date, and usage statistics.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Payment Method Not Found

If the payment method has been deleted, the system displays "Payment method not found" and returns to the list.

### 6.2.2.3. Special Requirements

- Display total amount processed through this payment method.
- Show transaction count for the current month and all-time.

### 6.2.2.4. Pre-condition

- The user is authenticated as Admin.
- The payment method exists in the system.

### 6.2.2.5. Post-condition

- If successful: Complete payment method details are displayed.
- If unsuccessful: An error message is shown and user is returned to the list.

### 6.2.2.6. Extension Points

- Admin can Edit or Delete the payment method from the details view.

---

## 6.2.3. Use-case Specification: "Add New Payment Method"

### 6.2.3.1. Brief Description

Allows administrators to add new payment methods to the system for use during invoice payment processing.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The admin selects "Add New Payment Method" from the payment method management screen.
2. The system displays a form with fields for name and description.
3. The admin enters the payment method information.
4. The admin clicks "Save" to create the payment method.
5. The system validates the input data.
6. The system checks for duplicate payment method names.
7. The system creates the new payment method record with status "Active."
8. The system displays a success message.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Duplicate Name

If a payment method with the same name already exists, the system displays "A payment method with this name already exists" and allows correction.

##### 6.2.3.2.2.2. Validation Error

If required fields are empty, the system highlights the errors and prevents submission.

### 6.2.3.3. Special Requirements

- Payment method name must be unique.
- New payment methods should be active by default.
- Common methods (Cash, Card, Bank Transfer) should be pre-suggested.

### 6.2.3.4. Pre-condition

- The user is authenticated as Admin.
- The payment method name is not already in use.

### 6.2.3.5. Post-condition

- If successful: A new payment method is created and available for use in payment processing.
- If unsuccessful: No record is created and error messages guide the admin.

### 6.2.3.6. Extension Points

- After creation, admin can immediately add another payment method.
- The new payment method becomes available in the payment processing dropdown.

---

## 6.2.4. Use-case Specification: "Edit Payment Method"

### 6.2.4.1. Brief Description

Allows administrators to modify the details of an existing payment method.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The admin selects a payment method and clicks "Edit."
2. The system displays a form pre-populated with current payment method information.
3. The admin modifies the name, description, or status.
4. The admin clicks "Save" to update the payment method.
5. The system validates the updated data.
6. The system updates the payment method record.
7. The system displays a success message.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. Name Conflict

If the new name conflicts with another existing payment method, the system displays an error and prevents the update.

##### 6.2.4.2.2.2. Deactivation Warning

If the admin is deactivating a payment method, the system warns that it will no longer be available for new payments.

### 6.2.4.3. Special Requirements

- Deactivating a payment method should not affect existing payment records.
- Changes should be logged for audit purposes.

### 6.2.4.4. Pre-condition

- The user is authenticated as Admin.
- The payment method exists in the system.

### 6.2.4.5. Post-condition

- If successful: The payment method information is updated in the database.
- If unsuccessful: The original data remains unchanged with an appropriate message.

### 6.2.4.6. Extension Points

- Admin can toggle active/inactive status quickly from the list view.

---

## 6.2.5. Use-case Specification: "Delete Payment Method"

### 6.2.5.1. Brief Description

Allows administrators to remove a payment method from the system, provided it has not been used in any payments.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The admin selects a payment method and clicks "Delete."
2. The system checks if the payment method has been used in any payment records.
3. If not used, the system displays a confirmation dialog.
4. The admin confirms the deletion.
5. The system deletes the payment method record.
6. The system displays a success message and refreshes the list.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. Payment Method In Use

If the payment method has been used in payments, the system displays "Cannot delete: This payment method has been used in X payments" and suggests deactivation instead.

##### 6.2.5.2.2.2. Deletion Cancelled

If the admin cancels the confirmation, the payment method remains unchanged.

### 6.2.5.3. Special Requirements

- Referential integrity must be maintained; payment methods with payment references cannot be deleted.
- Consider implementing soft delete (deactivation) to preserve payment history integrity.

### 6.2.5.4. Pre-condition

- The user is authenticated as Admin.
- The payment method exists in the system.
- The payment method has not been used in any payment records.

### 6.2.5.5. Post-condition

- If successful: The payment method is removed from the system.
- If unsuccessful: The payment method remains with an explanatory message.

### 6.2.5.6. Extension Points

- If deletion is blocked, admin can deactivate the payment method instead.
- Admin can view which invoices used this payment method.

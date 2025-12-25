# Manage Service Use Case Specifications

---

## 6.2.1. Use-case Specification: "View and Filter Services"

### 6.2.1.1. Brief Description

Allows administrators and receptionists to view a list of all medical services offered by the clinic and filter by type or status.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The user selects the "Services" or "Service Management" option from the navigation menu.
2. The system retrieves and displays a list of all services with their names, prices, and status.
3. The user applies filters such as service type or active/inactive status.
4. The system filters the list according to the selected criteria.
5. The system displays the filtered results.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Services Found

If no services match the filter criteria, the system displays "No services found" and allows the user to modify filters.

##### 6.2.1.2.2.2. Empty Service List

If no services exist in the system, the system displays "No services have been added yet" with an option to add new ones (for Admin only).

### 6.2.1.3. Special Requirements

- Services should display current effective price.
- Filter options should include service categories if applicable.
- The list should be sortable by name or price.

### 6.2.1.4. Pre-condition

- The user is authenticated as Admin or Receptionist.

### 6.2.1.5. Post-condition

- If successful: The service list is displayed according to the applied filters.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- Clicking on a service opens View Service Details.
- Admin users can access Add, Edit, or Delete functions from this view.

---

## 6.2.2. Use-case Specification: "View Service Details"

### 6.2.2.1. Brief Description

Allows administrators to view complete details of a specific service including pricing history.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The admin selects a service from the list.
2. The system retrieves the complete service record.
3. The system displays detailed information including: service ID, name, description, current price, price history, status, and usage statistics.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Service Not Found

If the service has been deleted, the system displays "Service not found" and returns to the list.

### 6.2.2.3. Special Requirements

- Display price history with effective dates.
- Show usage statistics (how many times the service has been used).
- Include creation and modification timestamps.

### 6.2.2.4. Pre-condition

- The user is authenticated as Admin.
- The service exists in the system.

### 6.2.2.5. Post-condition

- If successful: Complete service details are displayed.
- If unsuccessful: An error message is shown and user is returned to the list.

### 6.2.2.6. Extension Points

- Admin can Edit or Delete the service from the details view.

---

## 6.2.3. Use-case Specification: "Add New Service"

### 6.2.3.1. Brief Description

Allows administrators to add new medical services to the system with pricing information.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The admin selects "Add New Service" from the service management screen.
2. The system displays a form with fields for service name, description, and price.
3. The admin enters the service information.
4. The admin clicks "Save" to create the service.
5. The system validates the input data.
6. The system checks for duplicate service names.
7. The system creates the new service record with the price effective from today.
8. The system displays a success message.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Duplicate Name

If a service with the same name already exists, the system displays "A service with this name already exists" and allows correction.

##### 6.2.3.2.2.2. Invalid Price

If the price is not a valid positive number, the system displays "Please enter a valid price" and prevents submission.

### 6.2.3.3. Special Requirements

- Service name must be unique in the system.
- Price must be a positive number with up to 2 decimal places.
- New services should be active by default.

### 6.2.3.4. Pre-condition

- The user is authenticated as Admin.
- The service name is not already in use.

### 6.2.3.5. Post-condition

- If successful: A new service is created and available for use in invoices.
- If unsuccessful: No record is created and error messages guide the admin.

### 6.2.3.6. Extension Points

- After creation, admin can immediately add another service.
- The new service becomes available for selection in medical records.

---

## 6.2.4. Use-case Specification: "Edit Service"

### 6.2.4.1. Brief Description

Allows administrators to modify the details of an existing service including price updates.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The admin selects a service and clicks "Edit."
2. The system displays a form pre-populated with current service information.
3. The admin modifies the name, description, price, or status.
4. The admin clicks "Save" to update the service.
5. The system validates the updated data.
6. If the price changed, the system creates a new price history entry.
7. The system updates the service record.
8. The system displays a success message.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. Name Conflict

If the new name conflicts with another existing service, the system displays an error and prevents the update.

##### 6.2.4.2.2.2. Price Change Confirmation

If the price is being changed, the system asks for confirmation as this affects future invoices.

### 6.2.4.3. Special Requirements

- Price changes should be tracked in a history table with effective dates.
- Existing invoices should not be affected by price changes.
- Changes should be logged for audit purposes.

### 6.2.4.4. Pre-condition

- The user is authenticated as Admin.
- The service exists in the system.

### 6.2.4.5. Post-condition

- If successful: The service information is updated, price history is maintained.
- If unsuccessful: The original data remains unchanged with an appropriate message.

### 6.2.4.6. Extension Points

- Admin can deactivate a service instead of deleting it.

---

## 6.2.5. Use-case Specification: "Delete Service"

### 6.2.5.1. Brief Description

Allows administrators to remove a service from the system, provided it is not referenced by any invoices.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The admin selects a service and clicks "Delete."
2. The system checks if the service is referenced by any invoices.
3. If not referenced, the system displays a confirmation dialog.
4. The admin confirms the deletion.
5. The system deletes the service record and its price history.
6. The system displays a success message and refreshes the list.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. Service In Use

If the service is referenced by invoices, the system displays "Cannot delete: This service is used in X invoices" and suggests deactivation instead.

##### 6.2.5.2.2.2. Deletion Cancelled

If the admin cancels the confirmation, the service remains unchanged.

### 6.2.5.3. Special Requirements

- Referential integrity must be maintained; services with invoice references cannot be deleted.
- Consider implementing soft delete (deactivation) to preserve history.
- Deletion should be logged for audit purposes.

### 6.2.5.4. Pre-condition

- The user is authenticated as Admin.
- The service exists in the system.
- The service is not referenced by any invoices.

### 6.2.5.5. Post-condition

- If successful: The service is removed from the system.
- If unsuccessful: The service remains with an explanatory message.

### 6.2.5.6. Extension Points

- If deletion is blocked, admin can deactivate the service instead.
- Admin can view which invoices reference this service.

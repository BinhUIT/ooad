# Manage Medicine Import Use Case Specifications

---

## 6.2.1. Use-case Specification: "View Medicine Imports"

### 6.2.1.1. Brief Description

Allows warehouse staff to view a list of all medicine import records and filter by date or supplier.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The warehouse staff selects the "Medicine Imports" option from the navigation menu.
2. The system retrieves and displays a paginated list of import records with basic information (import ID, date, supplier, total value, status).
3. The staff applies filters such as date range or supplier name.
4. The system validates the filter criteria and retrieves matching imports.
5. The system displays the filtered results sorted by date (newest first).

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Imports Found

If no import records match the filter criteria, the system displays "No import records found" and allows the staff to modify filters.

##### 6.2.1.2.2.2. System Error

If the system fails to retrieve import data, it displays an error message and provides a retry option.

### 6.2.1.3. Special Requirements

- Display total import value for filtered results.
- Show import status (Draft, Approved, Completed).
- Support date range filtering for reporting purposes.

### 6.2.1.4. Pre-condition

- The user is authenticated as Warehouse Staff.

### 6.2.1.5. Post-condition

- If successful: The import record list is displayed according to the applied filters.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- Clicking on an import record opens View Import Details.
- Staff can access Add New Import function from this view.

---

## 6.2.2. Use-case Specification: "View Import Details"

### 6.2.2.1. Brief Description

Allows warehouse staff to view complete details of a medicine import including all items, quantities, and prices.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The staff selects an import record from the list.
2. The system retrieves the complete import record.
3. The system displays detailed information including: import ID, date, supplier details, list of medicines with quantities, unit prices, expiry dates, batch numbers, and total value.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Import Not Found

If the import record has been deleted, the system displays "Import record not found" and returns to the list.

### 6.2.2.3. Special Requirements

- Display itemized list of all imported medicines.
- Show batch/lot numbers for traceability.
- Include expiry date for each item.

### 6.2.2.4. Pre-condition

- The user is authenticated as Warehouse Staff.
- The import record exists in the system.

### 6.2.2.5. Post-condition

- If successful: Complete import details are displayed.
- If unsuccessful: An error message is shown and user is returned to the list.

### 6.2.2.6. Extension Points

- Staff can Edit or Delete the import if it's not yet approved.
- Staff can print import receipt from the details view.

---

## 6.2.3. Use-case Specification: "Add New Import"

### 6.2.3.1. Brief Description

Allows warehouse staff to create a new medicine import record and update inventory accordingly.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The warehouse staff selects "Add New Import" from the import management screen.
2. The system displays a form with fields for supplier information and import date.
3. The staff enters supplier details and selects the import date.
4. The staff adds medicines to the import by selecting from the medicine catalog.
5. For each medicine, the staff enters: quantity, unit price, expiry date, and batch number.
6. The system calculates line totals and import total automatically.
7. The staff reviews and clicks "Save" to create the import.
8. The system validates all data including expiry dates.
9. The system creates the import record and import detail records.
10. The system updates medicine inventory with the imported quantities.
11. The system displays a success message.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Medicine Not Found

If the staff needs to import a new medicine not in the catalog, the system offers to create a new medicine type first.

##### 6.2.3.2.2.2. Invalid Expiry Date

If an expiry date is in the past or too soon (e.g., within 30 days), the system displays a warning and asks for confirmation.

### 6.2.3.3. Special Requirements

- Expiry date must be in the future with a minimum threshold.
- Batch numbers must be recorded for traceability.
- Import total should be calculated automatically from line items.

### 6.2.3.4. Pre-condition

- The user is authenticated as Warehouse Staff.
- At least one medicine type exists in the system.

### 6.2.3.5. Post-condition

- If successful: A new import record is created, inventory is updated with new stock.
- If unsuccessful: No changes are made and error messages guide the staff.

### 6.2.3.6. Extension Points

- After creation, staff can print the import receipt.
- New inventory batches become available for dispensing following FEFO rules.

---

## 6.2.4. Use-case Specification: "Edit Import"

### 6.2.4.1. Brief Description

Allows warehouse staff to modify an existing import record before it is approved or medicines are dispensed.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The staff selects an import record and clicks "Edit."
2. The system verifies that the import is eligible for editing.
3. The system displays a form pre-populated with current import information.
4. The staff modifies import details (add/remove medicines, change quantities, prices, or dates).
5. The staff clicks "Save" to update the import.
6. The system validates the updated data.
7. The system updates the import record and adjusts inventory accordingly.
8. The system displays a success message.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. Import Already Approved

If the import has been approved, the system displays "Cannot edit: This import has been approved" and prevents editing.

##### 6.2.4.2.2.2. Medicines Already Dispensed

If any medicines from this import have been dispensed, the system prevents editing those specific items.

### 6.2.4.3. Special Requirements

- Changes must be logged with before/after values for audit.
- Inventory adjustments must be atomic to prevent inconsistencies.

### 6.2.4.4. Pre-condition

- The user is authenticated as Warehouse Staff.
- The import record exists and has not been approved.

### 6.2.4.5. Post-condition

- If successful: The import record is updated and inventory is adjusted.
- If unsuccessful: The original data remains unchanged with an appropriate message.

### 6.2.4.6. Extension Points

- Staff can submit the import for approval after editing.

---

## 6.2.5. Use-case Specification: "Delete Import"

### 6.2.5.1. Brief Description

Allows warehouse staff to delete an import record that has not been approved and has no dispensed medicines.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The staff selects an import record and clicks "Delete."
2. The system checks if the import is eligible for deletion (not approved, no medicines dispensed).
3. If eligible, the system displays a confirmation dialog.
4. The staff confirms the deletion.
5. The system removes the inventory quantities added by this import.
6. The system deletes the import record and all related detail records.
7. The system displays a success message and refreshes the list.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. Import Already Approved

If the import has been approved, the system displays "Cannot delete: This import has been approved" and prevents deletion.

##### 6.2.5.2.2.2. Medicines Dispensed

If any medicines from this import have been dispensed to patients, the system displays "Cannot delete: Some medicines have been dispensed" and prevents deletion.

##### 6.2.5.2.2.3. Deletion Cancelled

If the staff cancels the confirmation, the import record remains unchanged.

### 6.2.5.3. Special Requirements

- Inventory must be reversed when import is deleted.
- Deletion should be logged for audit purposes.
- Consider implementing soft delete to preserve history.

### 6.2.5.4. Pre-condition

- The user is authenticated as Warehouse Staff.
- The import record exists, has not been approved, and has no dispensed medicines.

### 6.2.5.5. Post-condition

- If successful: The import record is deleted and inventory is reversed.
- If unsuccessful: The import record remains with an explanatory message.

### 6.2.5.6. Extension Points

- Staff can view detailed dispensing history to understand why deletion is blocked.

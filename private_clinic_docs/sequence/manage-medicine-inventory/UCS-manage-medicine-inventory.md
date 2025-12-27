# Manage Medicine Inventory Use Case Specifications

---

## 6.2.1. Use-case Specification: "View Medicine Inventory"

### 6.2.1.1. Brief Description

Allows warehouse and pharmacy staff to view a list of all medicines in inventory with stock quantities and expiry information.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The staff member selects the "Medicine Inventory" option from the navigation menu.
2. The system retrieves and displays a list of all medicine types with current stock quantity, unit, and nearest expiry date.
3. The staff applies filters such as medicine type, stock status (In Stock, Low Stock, Out of Stock), or expiry status.
4. The system filters the list according to the selected criteria.
5. The system displays the filtered results with visual indicators for low stock and near-expiry items.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Medicines Found

If no medicines match the filter criteria, the system displays "No medicines found" and allows the user to modify filters.

##### 6.2.1.2.2.2. System Alerts

If there are medicines with critical low stock or nearing expiry, the system displays alert notifications.

### 6.2.1.3. Special Requirements

- Low stock threshold should be configurable per medicine type.
- Expiry warnings should appear for medicines expiring within the configured warning period.
- Color-coded status indicators (green for adequate stock, yellow for low, red for out of stock).

### 6.2.1.4. Pre-condition

- The user is authenticated as Warehouse Staff or Pharmacy Staff.

### 6.2.1.5. Post-condition

- If successful: The medicine inventory list is displayed according to the applied filters.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- Clicking on a medicine opens View Medicine Details.
- Warehouse staff can access Add New Medicine Type function from this view.

---

## 6.2.2. Use-case Specification: "View Medicine Details"

### 6.2.2.1. Brief Description

Allows warehouse staff to view complete details of a medicine including batch information and import/export history.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The staff selects a medicine from the inventory list.
2. The system retrieves the complete medicine record.
3. The system displays detailed information including: medicine ID, name, unit, description, current price, stock batches with quantities and expiry dates, and transaction history (imports and dispenses).

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Medicine Not Found

If the medicine has been deleted, the system displays "Medicine not found" and returns to the list.

### 6.2.2.3. Special Requirements

- Display all stock batches sorted by expiry date (FEFO order).
- Show price history with effective dates.
- Include import and dispense transaction history.

### 6.2.2.4. Pre-condition

- The user is authenticated as Warehouse Staff.
- The medicine exists in the system.

### 6.2.2.5. Post-condition

- If successful: Complete medicine details are displayed with batch and history information.
- If unsuccessful: An error message is shown and user is returned to the list.

### 6.2.2.6. Extension Points

- Staff can Edit Medicine Type, Add Price, or Delete Medicine from the details view.
- Staff can view related import records.

---

## 6.2.3. Use-case Specification: "Add New Medicine Type"

### 6.2.3.1. Brief Description

Allows warehouse staff to add a new medicine type to the system catalog.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The warehouse staff selects "Add New Medicine Type" from the inventory management screen.
2. The system displays a form with fields for medicine name, unit, description, initial price, and low stock threshold.
3. The staff enters the medicine information.
4. The staff clicks "Save" to create the medicine type.
5. The system validates the input data.
6. The system checks for duplicate medicine names.
7. The system creates the new medicine type record with initial price.
8. The system displays a success message.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Duplicate Name

If a medicine with the same name already exists, the system displays "A medicine with this name already exists" and allows correction.

##### 6.2.3.2.2.2. Validation Error

If required fields are empty or invalid, the system highlights the errors and prevents submission.

### 6.2.3.3. Special Requirements

- Medicine name must be unique in the system.
- Initial price must be a positive number.
- Unit should be selected from a predefined list (tablets, capsules, ml, mg, etc.).

### 6.2.3.4. Pre-condition

- The user is authenticated as Warehouse Staff.
- The medicine name is not already in use.

### 6.2.3.5. Post-condition

- If successful: A new medicine type is created with initial price and zero stock.
- If unsuccessful: No record is created and error messages guide the staff.

### 6.2.3.6. Extension Points

- After creation, staff can immediately import stock for the new medicine.
- The new medicine becomes available for prescription by doctors.

---

## 6.2.4. Use-case Specification: "Edit Medicine Type"

### 6.2.4.1. Brief Description

Allows warehouse staff to modify the details of an existing medicine type.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The staff selects a medicine type and clicks "Edit."
2. The system displays a form pre-populated with current medicine information.
3. The staff modifies the name, unit, description, or low stock threshold.
4. The staff clicks "Save" to update the medicine type.
5. The system validates the updated data.
6. The system checks that the new name doesn't conflict with existing medicines.
7. The system updates the medicine type record.
8. The system displays a success message.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. Name Conflict

If the new name conflicts with another existing medicine, the system displays an error and prevents the update.

##### 6.2.4.2.2.2. Unit Change Warning

If the unit is being changed, the system warns about potential inconsistencies with existing inventory.

### 6.2.4.3. Special Requirements

- Price changes should use the Add Medicine Price function for proper history tracking.
- Changes should be logged for audit purposes.

### 6.2.4.4. Pre-condition

- The user is authenticated as Warehouse Staff.
- The medicine type exists in the system.

### 6.2.4.5. Post-condition

- If successful: The medicine type information is updated in the database.
- If unsuccessful: The original data remains unchanged with an appropriate message.

### 6.2.4.6. Extension Points

- Staff can proceed to Add Medicine Price for price updates.

---

## 6.2.5. Use-case Specification: "Delete Medicine Type"

### 6.2.5.1. Brief Description

Allows warehouse staff to remove a medicine type from the system, provided it has no inventory and is not referenced in prescriptions.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The staff selects a medicine type and clicks "Delete."
2. The system checks if the medicine has any inventory stock.
3. The system checks if the medicine is referenced in any prescriptions.
4. If no inventory and no references exist, the system displays a confirmation dialog.
5. The staff confirms the deletion.
6. The system deletes the medicine type and related price history records.
7. The system displays a success message and refreshes the list.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. Has Inventory

If the medicine has inventory stock, the system displays "Cannot delete: This medicine has X units in inventory" and prevents deletion.

##### 6.2.5.2.2.2. Referenced In Prescriptions

If the medicine is referenced in prescriptions, the system displays "Cannot delete: This medicine is used in X prescriptions" and suggests deactivation instead.

##### 6.2.5.2.2.3. Deletion Cancelled

If the staff cancels the confirmation, the medicine type remains unchanged.

### 6.2.5.3. Special Requirements

- Referential integrity must be maintained.
- Consider implementing soft delete (deactivation) to preserve history.
- Deletion should be logged for audit purposes.

### 6.2.5.4. Pre-condition

- The user is authenticated as Warehouse Staff.
- The medicine type exists in the system.
- The medicine has no inventory stock.
- The medicine is not referenced in any prescriptions.

### 6.2.5.5. Post-condition

- If successful: The medicine type is removed from the system.
- If unsuccessful: The medicine type remains with an explanatory message.

### 6.2.5.6. Extension Points

- If deletion is blocked, staff can deactivate the medicine type instead.
- Staff can view which prescriptions reference this medicine.

---

## 6.2.6. Use-case Specification: "Add Medicine Price"

### 6.2.6.1. Brief Description

Allows warehouse staff to add a new price for a medicine with an effective date, maintaining price history.

### 6.2.6.2. Event Flow

#### 6.2.6.2.1. Main Event Flow

1. The staff selects a medicine type and clicks "Add Price" or "Update Price."
2. The system displays a form with the current price and fields for new price and effective date.
3. The staff enters the new price and effective date.
4. The staff clicks "Save" to create the price record.
5. The system validates the price and date.
6. The system creates a new price history record.
7. If the effective date is today or in the past, the current price is updated immediately.
8. The system displays a success message.

#### 6.2.6.2.2. Alternative Event Flows

##### 6.2.6.2.2.1. Invalid Price

If the price is not a valid positive number, the system displays "Please enter a valid price" and prevents submission.

##### 6.2.6.2.2.2. Past Date Warning

If the effective date is in the past, the system warns that this will affect historical records and asks for confirmation.

### 6.2.6.3. Special Requirements

- Price history must be maintained for audit and reporting.
- Future effective dates should be supported for planned price changes.
- Existing invoices should not be affected by price changes.

### 6.2.6.4. Pre-condition

- The user is authenticated as Warehouse Staff.
- The medicine type exists in the system.

### 6.2.6.5. Post-condition

- If successful: A new price record is created, current price is updated if applicable.
- If unsuccessful: No price record is created and error messages guide the staff.

### 6.2.6.6. Extension Points

- Staff can view complete price history from the medicine details view.
- System can generate reports on price changes over time.

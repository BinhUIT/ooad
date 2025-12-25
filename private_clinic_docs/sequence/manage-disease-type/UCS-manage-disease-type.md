# Manage Disease Type Use Case Specifications

---

## 6.2.1. Use-case Specification: "View and Filter Disease Types"

### 6.2.1.1. Brief Description

Allows administrators and doctors to view a list of all disease types in the system and search by name.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The user selects the "Disease Types" or "Disease Management" option from the navigation menu.
2. The system retrieves and displays a list of all disease types with their names and descriptions.
3. The user enters a search keyword in the search field.
4. The system filters the list to show only disease types matching the search criteria.
5. The system displays the filtered results.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Results Found

If no disease types match the search criteria, the system displays "No disease types found" and allows the user to modify the search.

##### 6.2.1.2.2.2. Empty Database

If no disease types exist in the system, the system displays "No disease types have been added yet" with an option to add new ones (for Admin only).

### 6.2.1.3. Special Requirements

- Search must be case-insensitive and support partial matching.
- The list should be sortable by name alphabetically.
- Results should be paginated for large datasets.

### 6.2.1.4. Pre-condition

- The user is authenticated as Admin or Doctor.

### 6.2.1.5. Post-condition

- If successful: The disease type list is displayed according to search criteria.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- Clicking on a disease type opens View Disease Type Details.
- Admin users can access Add, Edit, or Delete functions from this view.

---

## 6.2.2. Use-case Specification: "View Disease Type Details"

### 6.2.2.1. Brief Description

Allows administrators to view complete details of a specific disease type including its usage statistics.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The admin selects a disease type from the list.
2. The system retrieves the complete disease type record.
3. The system displays detailed information including: disease type ID, name, description, creation date, last modified date, and count of associated medical records.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Disease Type Not Found

If the disease type has been deleted, the system displays "Disease type not found" and returns to the list.

### 6.2.2.3. Special Requirements

- The detail view should show usage statistics (how many medical records reference this disease type).
- Display creation and modification timestamps for audit purposes.

### 6.2.2.4. Pre-condition

- The user is authenticated as Admin.
- The disease type exists in the system.

### 6.2.2.5. Post-condition

- If successful: Complete disease type details are displayed.
- If unsuccessful: An error message is shown and user is returned to the list.

### 6.2.2.6. Extension Points

- Admin can Edit or Delete the disease type from the details view.

---

## 6.2.3. Use-case Specification: "Add New Disease Type"

### 6.2.3.1. Brief Description

Allows administrators to add new disease types to the system for use in medical record diagnoses.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The admin selects "Add New Disease Type" from the disease type management screen.
2. The system displays a form with fields for name and description.
3. The admin enters the disease type information.
4. The admin clicks "Save" to create the disease type.
5. The system validates the input data.
6. The system checks for duplicate disease type names.
7. The system creates the new disease type record.
8. The system displays a success message and returns to the disease type list.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Duplicate Name

If a disease type with the same name already exists, the system displays "A disease type with this name already exists" and allows correction.

##### 6.2.3.2.2.2. Validation Error

If required fields are empty or invalid, the system highlights the errors and prevents submission.

### 6.2.3.3. Special Requirements

- Disease type name must be unique in the system.
- Name should have a minimum length of 2 characters and maximum of 100 characters.
- Description is optional but recommended.

### 6.2.3.4. Pre-condition

- The user is authenticated as Admin.
- The disease type name is not already in use.

### 6.2.3.5. Post-condition

- If successful: A new disease type is created and available for use in medical records.
- If unsuccessful: No record is created and error messages guide the user.

### 6.2.3.6. Extension Points

- After creation, admin can immediately add another disease type.
- The new disease type becomes available in the diagnosis dropdown for doctors.

---

## 6.2.4. Use-case Specification: "Edit Disease Type"

### 6.2.4.1. Brief Description

Allows administrators to modify the details of an existing disease type.

### 6.2.4.2. Event Flow

#### 6.2.4.2.1. Main Event Flow

1. The admin selects a disease type and clicks "Edit."
2. The system displays a form pre-populated with the current disease type information.
3. The admin modifies the name and/or description.
4. The admin clicks "Save" to update the disease type.
5. The system validates the updated data.
6. The system checks that the new name doesn't conflict with existing disease types.
7. The system updates the disease type record.
8. The system displays a success message.

#### 6.2.4.2.2. Alternative Event Flows

##### 6.2.4.2.2.1. Name Conflict

If the new name conflicts with another existing disease type, the system displays an error and prevents the update.

##### 6.2.4.2.2.2. No Changes Made

If the user saves without making changes, the system simply returns to the previous view without updating.

### 6.2.4.3. Special Requirements

- Changes should be logged with timestamp and user information for audit.
- Editing a disease type should not affect existing medical records referencing it.

### 6.2.4.4. Pre-condition

- The user is authenticated as Admin.
- The disease type exists in the system.

### 6.2.4.5. Post-condition

- If successful: The disease type information is updated in the database.
- If unsuccessful: The original data remains unchanged and an error message is displayed.

### 6.2.4.6. Extension Points

- After editing, admin can proceed to view details or return to the list.

---

## 6.2.5. Use-case Specification: "Delete Disease Type"

### 6.2.5.1. Brief Description

Allows administrators to remove a disease type from the system, provided it is not referenced by any medical records.

### 6.2.5.2. Event Flow

#### 6.2.5.2.1. Main Event Flow

1. The admin selects a disease type and clicks "Delete."
2. The system checks if the disease type is referenced by any medical records.
3. If not referenced, the system displays a confirmation dialog.
4. The admin confirms the deletion.
5. The system deletes the disease type record.
6. The system displays a success message and refreshes the list.

#### 6.2.5.2.2. Alternative Event Flows

##### 6.2.5.2.2.1. Disease Type In Use

If the disease type is referenced by medical records, the system displays "Cannot delete: This disease type is used in X medical records" and prevents deletion.

##### 6.2.5.2.2.2. Deletion Cancelled

If the admin cancels the confirmation, the disease type remains unchanged.

### 6.2.5.3. Special Requirements

- Referential integrity must be maintained; disease types with references cannot be deleted.
- Consider implementing soft delete (marking as inactive) instead of hard delete.
- Deletion should be logged for audit purposes.

### 6.2.5.4. Pre-condition

- The user is authenticated as Admin.
- The disease type exists in the system.
- The disease type is not referenced by any medical records.

### 6.2.5.5. Post-condition

- If successful: The disease type is removed from the system and no longer available for selection.
- If unsuccessful: The disease type remains in the system with an explanatory message.

### 6.2.5.6. Extension Points

- If deletion is blocked, admin can view which medical records reference this disease type.
- Alternative: Admin can deactivate instead of delete to preserve referential integrity.

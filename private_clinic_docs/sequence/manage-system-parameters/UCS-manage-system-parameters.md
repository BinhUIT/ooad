# Manage System Parameters Use Case Specifications

---

## 6.2.1. Use-case Specification: "View Parameters"

### 6.2.1.1. Brief Description

Allows administrators to view a list of all system configuration parameters with their current values.

### 6.2.1.2. Event Flow

#### 6.2.1.2.1. Main Event Flow

1. The admin selects the "System Parameters" or "System Configuration" option from the administration menu.
2. The system retrieves and displays a list of all system parameters with their names, descriptions, current values, and default values.
3. The admin can search or filter parameters by name or category.
4. The system displays the filtered/searched results.

#### 6.2.1.2.2. Alternative Event Flows

##### 6.2.1.2.2.1. No Parameters Found

If no parameters match the search criteria, the system displays "No parameters found" and allows the admin to modify the search.

##### 6.2.1.2.2.2. System Error

If the system fails to retrieve parameters, it displays an error message and provides a retry option.

### 6.2.1.3. Special Requirements

- Parameters should be grouped by category for easier navigation.
- Display both current and default values for comparison.
- Highlight parameters that have been modified from their default values.

### 6.2.1.4. Pre-condition

- The user is authenticated as Admin.

### 6.2.1.5. Post-condition

- If successful: The system parameter list is displayed.
- If unsuccessful: An error message is shown.

### 6.2.1.6. Extension Points

- Clicking on a parameter opens the Edit Parameter Value form.
- Admin can access Reset Parameter to Default from this view.

---

## 6.2.2. Use-case Specification: "Edit Parameter Value"

### 6.2.2.1. Brief Description

Allows administrators to modify the value of a system configuration parameter.

### 6.2.2.2. Event Flow

#### 6.2.2.2.1. Main Event Flow

1. The admin selects a parameter and clicks "Edit."
2. The system displays a form with the parameter name, description, current value, default value, and an input field for the new value.
3. The admin enters a new value for the parameter.
4. The admin clicks "Save" to update the parameter.
5. The system validates the new value against the parameter's data type and valid range.
6. The system updates the parameter value.
7. The system logs the change with timestamp, old value, new value, and user information.
8. The system displays a success message.

#### 6.2.2.2.2. Alternative Event Flows

##### 6.2.2.2.2.1. Invalid Data Type

If the entered value does not match the expected data type (e.g., entering text for a numeric parameter), the system displays "Invalid value type. Expected: [type]" and prevents saving.

##### 6.2.2.2.2.2. Value Out of Range

If the entered value is outside the allowed range, the system displays "Value must be between [min] and [max]" and prevents saving.

##### 6.2.2.2.2.3. Critical Parameter Warning

If the parameter being changed is critical (e.g., MAX_PATIENTS_PER_DAY), the system displays a warning about potential system impact and requires confirmation.

### 6.2.2.3. Special Requirements

- Data type validation must be enforced (number, text, boolean).
- Range validation must be applied where applicable.
- All changes must be logged with complete audit trail.
- Some parameters may require system restart to take effect.

### 6.2.2.4. Pre-condition

- The user is authenticated as Admin.
- The parameter exists in the system.

### 6.2.2.5. Post-condition

- If successful: The parameter value is updated and the change is logged.
- If unsuccessful: The original value remains unchanged with an appropriate message.

### 6.2.2.6. Extension Points

- Some parameter changes may trigger immediate system behavior updates.
- Admin can view change history from the parameter detail view.

---

## 6.2.3. Use-case Specification: "Reset Parameter to Default"

### 6.2.3.1. Brief Description

Allows administrators to restore a system parameter to its factory default value.

### 6.2.3.2. Event Flow

#### 6.2.3.2.1. Main Event Flow

1. The admin selects a parameter and clicks "Reset to Default."
2. The system displays a confirmation dialog showing the current value and the default value that will be restored.
3. The admin confirms the reset action.
4. The system updates the parameter to its default value.
5. The system logs the reset action with timestamp, previous value, and user information.
6. The system displays a success message.

#### 6.2.3.2.2. Alternative Event Flows

##### 6.2.3.2.2.1. Already Default

If the parameter already has its default value, the system displays "Parameter is already at default value" and no action is taken.

##### 6.2.3.2.2.2. Reset Cancelled

If the admin cancels the confirmation, the parameter value remains unchanged.

##### 6.2.3.2.2.3. Critical Parameter Warning

If the parameter is critical, the system displays an additional warning about potential system impact.

### 6.2.3.3. Special Requirements

- Confirmation is required before resetting to prevent accidental changes.
- Reset actions must be logged for audit purposes.
- Option to reset all parameters to defaults should be available with extra confirmation.

### 6.2.3.4. Pre-condition

- The user is authenticated as Admin.
- The parameter exists and has a defined default value.

### 6.2.3.5. Post-condition

- If successful: The parameter is restored to its default value and the action is logged.
- If unsuccessful: The parameter value remains unchanged.

### 6.2.3.6. Extension Points

- Batch reset: Admin can reset multiple or all parameters to defaults at once.
- Admin can view the history of all changes including resets.

---

## System Parameters Reference

The following are examples of configurable system parameters:

| Parameter Name                   | Description                                           | Default Value | Type    |
| -------------------------------- | ----------------------------------------------------- | ------------- | ------- |
| MAX_PATIENTS_PER_DAY             | Maximum number of patients per day per doctor         | 30            | Number  |
| EXAMINATION_TIME_MINUTES         | Default examination duration in minutes               | 15            | Number  |
| MEDICINE_EXPIRY_WARNING_DAYS     | Days before expiry to show warning                    | 30            | Number  |
| AUTO_CANCEL_RECEPTION            | Automatically cancel pending receptions at end of day | true          | Boolean |
| APPOINTMENT_BOOKING_ADVANCE_DAYS | How far in advance patients can book appointments     | 30            | Number  |
| PASSWORD_EXPIRY_DAYS             | Days before password must be changed                  | 90            | Number  |
| FAILED_LOGIN_LOCKOUT_ATTEMPTS    | Number of failed attempts before account lockout      | 5             | Number  |

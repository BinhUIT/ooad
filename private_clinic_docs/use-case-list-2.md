# USE CASE LIST - PRIVATE CLINIC MANAGEMENT SYSTEM

---

## 1. AUTHENTICATION (4 use cases)

| No. | Use-case Name   | Description                                                                                        | Users                                                 | Notes                                          |
| --- | --------------- | -------------------------------------------------------------------------------------------------- | ----------------------------------------------------- | ---------------------------------------------- |
| 1   | Sign In         | Allows users to log into the system to perform available functions based on their assigned role.   | Patient, Doctor, Receptionist, Warehouse Staff, Admin | JWT Token with Access/Refresh tokens           |
| 2   | Sign Up         | Allows new patients to register an account to use the clinic's services through the online portal. | Patient                                               | Patients only, staff accounts created by admin |
| 3   | Forgot Password | Allows users to reset their password through email verification.                                   | All users                                             | Email verification required                    |
| 4   | Manage Profile  | Allows logged-in users to view and update their personal profile information.                      | All logged-in users                                   |                                                |

---

## 2. PATIENT MANAGEMENT (5 use cases)

| No. | Use-case Name            | Description                                                                                                                    | Users                       | Notes                            |
| --- | ------------------------ | ------------------------------------------------------------------------------------------------------------------------------ | --------------------------- | -------------------------------- |
| 5   | View and Filter Patients | Allows staff to view a list of all patients and filter by various criteria such as name, phone, email, age, or gender.         | Receptionist, Admin         | Pagination, export to Excel/PDF  |
| 6   | View Patient Details     | Allows staff to view complete patient information including personal data, appointment history, medical records, and invoices. | Receptionist, Admin, Doctor | Multiple tabs for different info |
| 7   | Add New Patient          | Allows receptionists to add new patient records to the system.                                                                 | Receptionist                |                                  |
| 8   | Edit Patient             | Allows staff to update existing patient information.                                                                           | Receptionist, Admin         |                                  |
| 9   | Delete Patient           | Allows admin to remove patient records from the system.                                                                        | Admin                       | Soft delete recommended          |

---

## 3. APPOINTMENT MANAGEMENT (5 use cases)

| No. | Use-case Name                | Description                                                                                                         | Users                         | Notes                               |
| --- | ---------------------------- | ------------------------------------------------------------------------------------------------------------------- | ----------------------------- | ----------------------------------- |
| 10  | View and Filter Appointments | Allows users to browse the clinic's appointment roster, applying filters such as date range, doctor, or status.     | Receptionist, Doctor, Patient | Real-time updates                   |
| 11  | View Appointment Details     | Allows authorized staff or patients to inspect a selected appointment's comprehensive information.                  | Receptionist, Doctor, Patient | Read-only view                      |
| 12  | Add New Appointment          | Allows receptionists or patients to schedule a new appointment by selecting an available slot, patient, and doctor. | Receptionist, Patient         | Capacity constraints enforced       |
| 13  | Edit Appointment             | Allows receptionists to modify appointment details such as date, time, or assigned doctor.                          | Receptionist                  |                                     |
| 14  | Delete/Cancel Appointment    | Allows receptionists or patients to cancel an existing appointment.                                                 | Receptionist, Patient         | Cancellation restrictions may apply |

---

## 4. RECEPTION MANAGEMENT (5 use cases)

| No. | Use-case Name              | Description                                                                                                                  | Users                | Notes                       |
| --- | -------------------------- | ---------------------------------------------------------------------------------------------------------------------------- | -------------------- | --------------------------- |
| 15  | View and Filter Receptions | Allows receptionists to view a list of all patient receptions and filter by date, status, doctor, or patient.                | Receptionist         | Default: today's receptions |
| 16  | View Reception Details     | Allows staff to view complete details of a reception including patient information, appointment details, and current status. | Receptionist, Doctor | Status timeline included    |
| 17  | Add New Reception          | Allows receptionists to create a new reception record when a patient arrives for their appointment.                          | Receptionist         | Initial status: "Waiting"   |
| 18  | Edit Reception Status      | Allows receptionists to update the status of a reception (Waiting, In Examination, Done, Cancelled).                         | Receptionist         | Status color-coded          |
| 19  | End Day/Close Receptions   | Allows receptionists to close all pending receptions at the end of the workday.                                              | Receptionist         | Batch status update         |

---

## 5. MEDICAL RECORD MANAGEMENT (3 use cases)

| No. | Use-case Name        | Description                                                                                                                                  | Users  | Notes                |
| --- | -------------------- | -------------------------------------------------------------------------------------------------------------------------------------------- | ------ | -------------------- |
| 20  | View Medical Records | Allows doctors to view a list of medical records and access detailed information including symptoms, diagnosis, prescriptions, and services. | Doctor | Privacy protected    |
| 21  | Add Medical Record   | Allows doctors to create a new medical record for a patient during or after an examination.                                                  | Doctor | Auto-creates invoice |
| 22  | Edit Medical Record  | Allows doctors to modify existing medical record details.                                                                                    | Doctor | Audit trail required |

---

## 6. PRESCRIPTION MANAGEMENT (3 use cases)

| No. | Use-case Name      | Description                                                                                                 | Users                  | Notes                                                 |
| --- | ------------------ | ----------------------------------------------------------------------------------------------------------- | ---------------------- | ----------------------------------------------------- |
| 23  | View Prescriptions | Allows doctors and pharmacy staff to view a list of prescriptions and access detailed medicine information. | Doctor, Pharmacy Staff | Status: Pending, Partially Dispensed, Fully Dispensed |
| 24  | Add Prescription   | Allows doctors to create a new prescription for a patient after completing a medical examination.           | Doctor                 | Inventory warnings displayed                          |
| 25  | Dispense Medicine  | Allows pharmacy staff to dispense medicines according to prescriptions.                                     | Pharmacy Staff         | FEFO rules applied                                    |

---

## 7. INVOICE/BILLING MANAGEMENT (6 use cases)

| No. | Use-case Name            | Description                                                                                                         | Users                                 | Notes                        |
| --- | ------------------------ | ------------------------------------------------------------------------------------------------------------------- | ------------------------------------- | ---------------------------- |
| 26  | View and Filter Invoices | Allows staff members to view a list of all invoices and apply filters to find specific invoices.                    | Receptionist, Pharmacy Staff          | Pagination, real-time totals |
| 27  | View Invoice Details     | Allows users to view complete details of a specific invoice including services, medicines, and payment information. | Receptionist, Pharmacy Staff, Patient | Itemized breakdown           |
| 28  | Add Service to Invoice   | Allows receptionists to add medical services to an existing invoice and automatically recalculate the total amount. | Receptionist                          | Cannot modify paid invoices  |
| 29  | Add Medicine to Invoice  | Allows pharmacy staff to add medicines to an invoice after dispensing.                                              | Pharmacy Staff                        | Linked to prescription       |
| 30  | Process Payment          | Allows receptionists to process full or partial payment for an invoice.                                             | Receptionist                          | Multiple payment methods     |
| 31  | Print Invoice            | Allows users to generate and print a formatted invoice document.                                                    | Receptionist, Patient                 | PDF format                   |

---

## 8. STAFF MANAGEMENT (6 use cases)

| No. | Use-case Name         | Description                                                                                                        | Users | Notes                             |
| --- | --------------------- | ------------------------------------------------------------------------------------------------------------------ | ----- | --------------------------------- |
| 32  | View and Filter Staff | Allows administrators to view a list of all staff members and filter by role or status.                            | Admin | Staff count per role displayed    |
| 33  | View Staff Details    | Allows administrators to view complete details of a staff member including personal information and work schedule. | Admin | Calendar format for schedule      |
| 34  | Add New Staff         | Allows administrators to add new staff members to the system and create their accounts.                            | Admin | Temporary password sent via email |
| 35  | Edit Staff            | Allows administrators to modify staff member information.                                                          | Admin |                                   |
| 36  | Delete Staff          | Allows administrators to remove staff members from the system.                                                     | Admin | Account deactivation              |
| 37  | Manage Staff Schedule | Allows administrators to set and modify work schedules for staff members.                                          | Admin | For doctors: appointment slots    |

---

## 9. MEDICINE INVENTORY MANAGEMENT (6 use cases)

| No. | Use-case Name           | Description                                                                                                                    | Users                           | Notes                     |
| --- | ----------------------- | ------------------------------------------------------------------------------------------------------------------------------ | ------------------------------- | ------------------------- |
| 38  | View Medicine Inventory | Allows warehouse and pharmacy staff to view a list of all medicines in inventory with stock quantities and expiry information. | Warehouse Staff, Pharmacy Staff | Low stock/expiry alerts   |
| 39  | View Medicine Details   | Allows warehouse staff to view complete details of a medicine including batch information and import/export history.           | Warehouse Staff                 | FEFO order display        |
| 40  | Add New Medicine Type   | Allows warehouse staff to add a new medicine type to the system catalog.                                                       | Warehouse Staff                 | Unique name required      |
| 41  | Edit Medicine Type      | Allows warehouse staff to modify medicine type information.                                                                    | Warehouse Staff                 |                           |
| 42  | Delete Medicine Type    | Allows warehouse staff to remove a medicine type from the catalog.                                                             | Warehouse Staff                 | Cannot delete if in stock |
| 43  | Add Medicine Price      | Allows warehouse staff to update the selling price of a medicine with effective date.                                          | Warehouse Staff                 | Price history maintained  |

---

## 10. MEDICINE IMPORT MANAGEMENT (5 use cases)

| No. | Use-case Name         | Description                                                                                                       | Users           | Notes                              |
| --- | --------------------- | ----------------------------------------------------------------------------------------------------------------- | --------------- | ---------------------------------- |
| 44  | View Medicine Imports | Allows warehouse staff to view a list of all medicine import records and filter by date or supplier.              | Warehouse Staff | Status: Draft, Approved, Completed |
| 45  | View Import Details   | Allows warehouse staff to view complete details of a medicine import including all items, quantities, and prices. | Warehouse Staff | Batch/lot numbers included         |
| 46  | Add New Import        | Allows warehouse staff to create a new medicine import record and update inventory accordingly.                   | Warehouse Staff | Expiry date validation             |
| 47  | Edit Import           | Allows warehouse staff to modify an import record before it is approved.                                          | Warehouse Staff | Only draft imports editable        |
| 48  | Delete Import         | Allows warehouse staff to remove an import record that has not been completed.                                    | Warehouse Staff | Cannot delete completed imports    |

---

## 11. SERVICE MANAGEMENT (5 use cases)

| No. | Use-case Name            | Description                                                                                                                        | Users               | Notes                             |
| --- | ------------------------ | ---------------------------------------------------------------------------------------------------------------------------------- | ------------------- | --------------------------------- |
| 49  | View and Filter Services | Allows administrators and receptionists to view a list of all medical services offered by the clinic and filter by type or status. | Admin, Receptionist | Current effective price displayed |
| 50  | View Service Details     | Allows administrators to view complete details of a specific service including pricing history.                                    | Admin               | Usage statistics included         |
| 51  | Add New Service          | Allows administrators to add new medical services to the system with pricing information.                                          | Admin               | Unique name required              |
| 52  | Edit Service             | Allows administrators to modify service information including price updates.                                                       | Admin               | Price history maintained          |
| 53  | Delete Service           | Allows administrators to remove a service from the system.                                                                         | Admin               | Soft delete if used in records    |

---

## 12. DISEASE TYPE MANAGEMENT (5 use cases)

| No. | Use-case Name                 | Description                                                                                               | Users         | Notes                          |
| --- | ----------------------------- | --------------------------------------------------------------------------------------------------------- | ------------- | ------------------------------ |
| 54  | View and Filter Disease Types | Allows administrators and doctors to view a list of all disease types in the system and search by name.   | Admin, Doctor | Case-insensitive search        |
| 55  | View Disease Type Details     | Allows administrators to view complete details of a specific disease type including its usage statistics. | Admin         | Medical record count displayed |
| 56  | Add New Disease Type          | Allows administrators to add new disease types to the system for use in medical record diagnoses.         | Admin         | Unique name required           |
| 57  | Edit Disease Type             | Allows administrators to modify the details of an existing disease type.                                  | Admin         |                                |
| 58  | Delete Disease Type           | Allows administrators to remove a disease type from the system.                                           | Admin         | Cannot delete if referenced    |

---

## 13. PAYMENT METHOD MANAGEMENT (5 use cases)

| No. | Use-case Name               | Description                                                                                               | Users               | Notes                           |
| --- | --------------------------- | --------------------------------------------------------------------------------------------------------- | ------------------- | ------------------------------- |
| 59  | View Payment Methods        | Allows administrators and receptionists to view a list of all available payment methods in the system.    | Admin, Receptionist | Receptionist: read-only         |
| 60  | View Payment Method Details | Allows administrators to view complete details of a specific payment method including usage history.      | Admin               | Transaction statistics included |
| 61  | Add New Payment Method      | Allows administrators to add new payment methods to the system for use during invoice payment processing. | Admin               | Active by default               |
| 62  | Edit Payment Method         | Allows administrators to modify the details of an existing payment method.                                | Admin               | Can activate/deactivate         |
| 63  | Delete Payment Method       | Allows administrators to remove a payment method from the system.                                         | Admin               | Cannot delete if used           |

---

## 14. SYSTEM PARAMETERS MANAGEMENT (3 use cases)

| No. | Use-case Name              | Description                                                                                            | Users | Notes                      |
| --- | -------------------------- | ------------------------------------------------------------------------------------------------------ | ----- | -------------------------- |
| 64  | View Parameters            | Allows administrators to view a list of all system configuration parameters with their current values. | Admin | Grouped by category        |
| 65  | Edit Parameter Value       | Allows administrators to modify the value of a system configuration parameter.                         | Admin | Validation and audit trail |
| 66  | Reset Parameter to Default | Allows administrators to restore a system parameter to its factory default value.                      | Admin | Confirmation required      |

---

## 15. PATIENT PORTAL / SELF-SERVICE (5 use cases)

| No. | Use-case Name               | Description                                                                                                                                          | Users   | Notes                                     |
| --- | --------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- | ------- | ----------------------------------------- |
| 67  | Book Appointment Online     | Allows logged-in patients to schedule new appointments with doctors through the online patient portal.                                               | Patient | Real-time availability, confirmation sent |
| 68  | View Medical Record History | Allows logged-in patients to view their own medical record history including diagnoses and prescriptions.                                            | Patient | Own records only                          |
| 69  | View Appointment History    | Allows logged-in patients to view their appointment history including past and upcoming appointments with the ability to cancel future appointments. | Patient | Upcoming/Past sections                    |
| 70  | View Invoice History        | Allows logged-in patients to view their invoice and payment history.                                                                                 | Patient | Download/print option                     |
| 71  | Patient Dashboard/Home      | Displays a summary dashboard for patients with quick access to appointments, medical records, and notifications.                                     | Patient | Overview and quick actions                |

---

## SUMMARY

| Category                      | Use Cases Count |
| ----------------------------- | --------------- |
| Authentication                | 4               |
| Patient Management            | 5               |
| Appointment Management        | 5               |
| Reception Management          | 5               |
| Medical Record Management     | 3               |
| Prescription Management       | 3               |
| Invoice/Billing Management    | 6               |
| Staff Management              | 6               |
| Medicine Inventory Management | 6               |
| Medicine Import Management    | 5               |
| Service Management            | 5               |
| Disease Type Management       | 5               |
| Payment Method Management     | 5               |
| System Parameters Management  | 3               |
| Patient Portal / Self-Service | 5               |
| **TOTAL**                     | **71**          |

---

## ACTORS SUMMARY

| Actor           | Description                                                                |
| --------------- | -------------------------------------------------------------------------- |
| Patient         | End users who use the clinic services                                      |
| Doctor          | Medical professionals who examine patients and create medical records      |
| Receptionist    | Staff who manage appointments, receptions, and payments                    |
| Pharmacy Staff  | Staff who manage prescriptions and dispense medicines                      |
| Warehouse Staff | Staff who manage medicine inventory and imports                            |
| Admin           | System administrators who manage staff, services, and system configuration |

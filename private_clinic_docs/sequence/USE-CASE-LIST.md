# DANH SÁCH USE CASE - PRIVATE CLINIC MANAGEMENT SYSTEM

Tài liệu tổng hợp tất cả các use case trong hệ thống Quản lý Phòng khám tư.

---

## 1. AUTHENTICATION (4 use cases)

| ID         | Use Case        | Actors                                                | File                                          |
| ---------- | --------------- | ----------------------------------------------------- | --------------------------------------------- |
| UC_AUTH_01 | Sign In         | Patient, Doctor, Receptionist, Warehouse Staff, Admin | [sign-in.md](auth/sign-in.md)                 |
| UC_AUTH_02 | Sign Up         | Patient                                               | [sign-up.md](auth/sign-up.md)                 |
| UC_AUTH_03 | Forgot Password | All users                                             | [forgot-password.md](auth/forgot-password.md) |
| UC_AUTH_04 | Manage Profile  | All logged-in users                                   | [manage-profile.md](auth/manage-profile.md)   |

**UCS**: [UCS-auth.md](auth/UCS-auth.md)

---

## 2. PATIENT MANAGEMENT (5 use cases)

| ID        | Use Case                 | Actors                      | File                                                                      |
| --------- | ------------------------ | --------------------------- | ------------------------------------------------------------------------- |
| UC_PAT_01 | View and Filter Patients | Receptionist, Admin         | [view-and-filter-patients.md](manage-patient/view-and-filter-patients.md) |
| UC_PAT_02 | View Patient Details     | Receptionist, Admin, Doctor | [view-patient-details.md](manage-patient/view-patient-details.md)         |
| UC_PAT_03 | Add New Patient          | Receptionist                | [add-new-patient.md](manage-patient/add-new-patient.md)                   |
| UC_PAT_04 | Edit Patient             | Receptionist, Admin         | [edit-patient.md](manage-patient/edit-patient.md)                         |
| UC_PAT_05 | Delete Patient           | Admin                       | [delete-patient.md](manage-patient/delete-patient.md)                     |

**UCS**: [UCS-manage-patient.md](manage-patient/UCS-manage-patient.md)

---

## 3. APPOINTMENT MANAGEMENT (5 use cases)

| ID        | Use Case                     | Actors                        | File                                                                                  |
| --------- | ---------------------------- | ----------------------------- | ------------------------------------------------------------------------------------- |
| UC_APT_01 | View and Filter Appointments | Receptionist, Doctor, Patient | [view-and-filter-appointments.md](manage-appointment/view-and-filter-appointments.md) |
| UC_APT_02 | View Appointment Details     | Receptionist, Doctor, Patient | [view-appointment-details.md](manage-appointment/view-appointment-details.md)         |
| UC_APT_03 | Add New Appointment          | Receptionist, Patient         | [add-new-appointment.md](manage-appointment/add-new-appointment.md)                   |
| UC_APT_04 | Edit Appointment             | Receptionist                  | [edit-appointment.md](manage-appointment/edit-appointment.md)                         |
| UC_APT_05 | Delete/Cancel Appointment    | Receptionist, Patient         | [delete-appointment.md](manage-appointment/delete-appointment.md)                     |

**UCS**: [UCS-manage-appointment.md](manage-appointment/UCS-manage-appointment.md)

---

## 4. RECEPTION MANAGEMENT (5 use cases)

| ID        | Use Case                   | Actors               | File                                                                            |
| --------- | -------------------------- | -------------------- | ------------------------------------------------------------------------------- |
| UC_RCP_01 | View and Filter Receptions | Receptionist         | [view-and-filter-receptions.md](manage-reception/view-and-filter-receptions.md) |
| UC_RCP_02 | View Reception Details     | Receptionist, Doctor | [view-reception-details.md](manage-reception/view-reception-details.md)         |
| UC_RCP_03 | Add New Reception          | Receptionist         | [add-new-reception.md](manage-reception/add-new-reception.md)                   |
| UC_RCP_04 | Edit Reception Status      | Receptionist         | [edit-reception-status.md](manage-reception/edit-reception-status.md)           |
| UC_RCP_05 | End Day/Close Receptions   | Receptionist         | [end-day-close-receptions.md](manage-reception/end-day-close-receptions.md)     |

**UCS**: [UCS-manage-reception.md](manage-reception/UCS-manage-reception.md)

---

## 5. MEDICAL RECORD MANAGEMENT (3 use cases)

| ID       | Use Case             | Actors | File                                                                         |
| -------- | -------------------- | ------ | ---------------------------------------------------------------------------- |
| UC_MR_01 | View Medical Records | Doctor | [view-medical-records.md](manage-medical-record/view-medical-records.md)     |
| UC_MR_02 | Add Medical Record   | Doctor | [add-new-medical-record.md](manage-medical-record/add-new-medical-record.md) |
| UC_MR_03 | Edit Medical Record  | Doctor | [edit-medical-record.md](manage-medical-record/edit-medical-record.md)       |

**UCS**: [UCS-manage-medical-record.md](manage-medical-record/UCS-manage-medical-record.md)

---

## 6. PRESCRIPTION MANAGEMENT (3 use cases)

| ID          | Use Case           | Actors                 | File                                                                   |
| ----------- | ------------------ | ---------------------- | ---------------------------------------------------------------------- |
| UC_PRESC_01 | View Prescriptions | Doctor, Pharmacy Staff | [view-prescriptions.md](manage-prescription/view-prescriptions.md)     |
| UC_PRESC_02 | Add Prescription   | Doctor                 | [add-new-prescription.md](manage-prescription/add-new-prescription.md) |
| UC_PRESC_03 | Dispense Medicine  | Pharmacy Staff         | [dispense-medicine.md](manage-prescription/dispense-medicine.md)       |

**UCS**: [UCS-manage-prescription.md](manage-prescription/UCS-manage-prescription.md)

---

## 7. INVOICE/BILLING MANAGEMENT (6 use cases)

| ID        | Use Case                 | Actors                                | File                                                                      |
| --------- | ------------------------ | ------------------------------------- | ------------------------------------------------------------------------- |
| UC_INV_01 | View and Filter Invoices | Receptionist, Pharmacy Staff          | [view-and-filter-invoices.md](manage-invoice/view-and-filter-invoices.md) |
| UC_INV_02 | View Invoice Details     | Receptionist, Pharmacy Staff, Patient | [view-invoice-details.md](manage-invoice/view-invoice-details.md)         |
| UC_INV_03 | Add Service to Invoice   | Receptionist                          | [add-service-to-invoice.md](manage-invoice/add-service-to-invoice.md)     |
| UC_INV_04 | Add Medicine to Invoice  | Pharmacy Staff                        | [add-medicine-to-invoice.md](manage-invoice/add-medicine-to-invoice.md)   |
| UC_INV_05 | Process Payment          | Receptionist                          | [process-payment.md](manage-invoice/process-payment.md)                   |
| UC_INV_06 | Print Invoice            | Receptionist, Patient                 | [print-invoice.md](manage-invoice/print-invoice.md)                       |

**UCS**: [UCS-manage-invoice.md](manage-invoice/UCS-manage-invoice.md)

---

## 8. STAFF MANAGEMENT (6 use cases)

| ID        | Use Case              | Actors | File                                                              |
| --------- | --------------------- | ------ | ----------------------------------------------------------------- |
| UC_STF_01 | View and Filter Staff | Admin  | [view-and-filter-staff.md](manage-staff/view-and-filter-staff.md) |
| UC_STF_02 | View Staff Details    | Admin  | [view-staff-details.md](manage-staff/view-staff-details.md)       |
| UC_STF_03 | Add New Staff         | Admin  | [add-new-staff.md](manage-staff/add-new-staff.md)                 |
| UC_STF_04 | Edit Staff            | Admin  | [edit-staff.md](manage-staff/edit-staff.md)                       |
| UC_STF_05 | Delete Staff          | Admin  | [delete-staff.md](manage-staff/delete-staff.md)                   |
| UC_STF_06 | Manage Staff Schedule | Admin  | [manage-staff-schedule.md](manage-staff/manage-staff-schedule.md) |

**UCS**: [UCS-manage-staff.md](manage-staff/UCS-manage-staff.md)

---

## 9. MEDICINE INVENTORY MANAGEMENT (6 use cases)

| ID        | Use Case                | Actors                          | File                                                                               |
| --------- | ----------------------- | ------------------------------- | ---------------------------------------------------------------------------------- |
| UC_MED_01 | View Medicine Inventory | Warehouse Staff, Pharmacy Staff | [view-medicine-inventory.md](manage-medicine-inventory/view-medicine-inventory.md) |
| UC_MED_02 | View Medicine Details   | Warehouse Staff                 | [view-medicine-details.md](manage-medicine-inventory/view-medicine-details.md)     |
| UC_MED_03 | Add New Medicine Type   | Warehouse Staff                 | [add-new-medicine-type.md](manage-medicine-inventory/add-new-medicine-type.md)     |
| UC_MED_04 | Edit Medicine Type      | Warehouse Staff                 | [edit-medicine-type.md](manage-medicine-inventory/edit-medicine-type.md)           |
| UC_MED_05 | Delete Medicine Type    | Warehouse Staff                 | [delete-medicine-type.md](manage-medicine-inventory/delete-medicine-type.md)       |
| UC_MED_06 | Add Medicine Price      | Warehouse Staff                 | [add-medicine-price.md](manage-medicine-inventory/add-medicine-price.md)           |

**UCS**: [UCS-manage-medicine-inventory.md](manage-medicine-inventory/UCS-manage-medicine-inventory.md)

---

## 10. MEDICINE IMPORT MANAGEMENT (5 use cases)

| ID        | Use Case              | Actors          | File                                                                        |
| --------- | --------------------- | --------------- | --------------------------------------------------------------------------- |
| UC_IMP_01 | View Medicine Imports | Warehouse Staff | [view-medicine-imports.md](manage-medicine-import/view-medicine-imports.md) |
| UC_IMP_02 | View Import Details   | Warehouse Staff | [view-import-details.md](manage-medicine-import/view-import-details.md)     |
| UC_IMP_03 | Add New Import        | Warehouse Staff | [add-new-import.md](manage-medicine-import/add-new-import.md)               |
| UC_IMP_04 | Edit Import           | Warehouse Staff | [edit-import.md](manage-medicine-import/edit-import.md)                     |
| UC_IMP_05 | Delete Import         | Warehouse Staff | [delete-import.md](manage-medicine-import/delete-import.md)                 |

**UCS**: [UCS-manage-medicine-import.md](manage-medicine-import/UCS-manage-medicine-import.md)

---

## 11. SERVICE MANAGEMENT (5 use cases)

| ID        | Use Case                 | Actors              | File                                                                      |
| --------- | ------------------------ | ------------------- | ------------------------------------------------------------------------- |
| UC_SVC_01 | View and Filter Services | Admin, Receptionist | [view-and-filter-services.md](manage-service/view-and-filter-services.md) |
| UC_SVC_02 | View Service Details     | Admin               | [view-service-details.md](manage-service/view-service-details.md)         |
| UC_SVC_03 | Add New Service          | Admin               | [add-new-service.md](manage-service/add-new-service.md)                   |
| UC_SVC_04 | Edit Service             | Admin               | [edit-service.md](manage-service/edit-service.md)                         |
| UC_SVC_05 | Delete Service           | Admin               | [delete-service.md](manage-service/delete-service.md)                     |

**UCS**: [UCS-manage-service.md](manage-service/UCS-manage-service.md)

---

## 12. DISEASE TYPE MANAGEMENT (5 use cases)

| ID        | Use Case                      | Actors        | File                                                                                     |
| --------- | ----------------------------- | ------------- | ---------------------------------------------------------------------------------------- |
| UC_DIS_01 | View and Filter Disease Types | Admin, Doctor | [view-and-filter-disease-types.md](manage-disease-type/view-and-filter-disease-types.md) |
| UC_DIS_02 | View Disease Type Details     | Admin         | [view-disease-type-details.md](manage-disease-type/view-disease-type-details.md)         |
| UC_DIS_03 | Add New Disease Type          | Admin         | [add-new-disease-type.md](manage-disease-type/add-new-disease-type.md)                   |
| UC_DIS_04 | Edit Disease Type             | Admin         | [edit-disease-type.md](manage-disease-type/edit-disease-type.md)                         |
| UC_DIS_05 | Delete Disease Type           | Admin         | [delete-disease-type.md](manage-disease-type/delete-disease-type.md)                     |

**UCS**: [UCS-manage-disease-type.md](manage-disease-type/UCS-manage-disease-type.md)

---

## 13. PAYMENT METHOD MANAGEMENT (5 use cases)

| ID        | Use Case                    | Actors              | File                                                                                   |
| --------- | --------------------------- | ------------------- | -------------------------------------------------------------------------------------- |
| UC_PAY_01 | View Payment Methods        | Admin, Receptionist | [view-payment-methods.md](manage-payment-method/view-payment-methods.md)               |
| UC_PAY_02 | View Payment Method Details | Admin               | [view-payment-method-details.md](manage-payment-method/view-payment-method-details.md) |
| UC_PAY_03 | Add New Payment Method      | Admin               | [add-new-payment-method.md](manage-payment-method/add-new-payment-method.md)           |
| UC_PAY_04 | Edit Payment Method         | Admin               | [edit-payment-method.md](manage-payment-method/edit-payment-method.md)                 |
| UC_PAY_05 | Delete Payment Method       | Admin               | [delete-payment-method.md](manage-payment-method/delete-payment-method.md)             |

**UCS**: [UCS-manage-payment-method.md](manage-payment-method/UCS-manage-payment-method.md)

---

## 14. SYSTEM PARAMETERS MANAGEMENT (3 use cases)

| ID        | Use Case                   | Actors | File                                                                                    |
| --------- | -------------------------- | ------ | --------------------------------------------------------------------------------------- |
| UC_PAR_01 | View Parameters            | Admin  | [view-parameters.md](manage-system-parameters/view-parameters.md)                       |
| UC_PAR_02 | Edit Parameter Value       | Admin  | [edit-parameter-value.md](manage-system-parameters/edit-parameter-value.md)             |
| UC_PAR_03 | Reset Parameter to Default | Admin  | [reset-parameter-to-default.md](manage-system-parameters/reset-parameter-to-default.md) |

**UCS**: [UCS-manage-system-parameters.md](manage-system-parameters/UCS-manage-system-parameters.md)

---

## 15. PATIENT PORTAL / SELF-SERVICE (5 use cases)

| ID           | Use Case                    | Actors  | File                                                                            |
| ------------ | --------------------------- | ------- | ------------------------------------------------------------------------------- |
| UC_PORTAL_01 | Book Appointment Online     | Patient | [book-appointment-online.md](patient-portal/book-appointment-online.md)         |
| UC_PORTAL_02 | View Medical Record History | Patient | [view-medical-record-history.md](patient-portal/view-medical-record-history.md) |
| UC_PORTAL_03 | View Appointment History    | Patient | [view-appointment-history.md](patient-portal/view-appointment-history.md)       |
| UC_PORTAL_04 | View Invoice History        | Patient | [view-invoice-history.md](patient-portal/view-invoice-history.md)               |
| UC_PORTAL_05 | Patient Dashboard/Home      | Patient | [patient-dashboard.md](patient-portal/patient-dashboard.md)                     |

**UCS**: [UCS-patient-portal.md](patient-portal/UCS-patient-portal.md)

---

## TỔNG HỢP

- **Tổng số nhóm chức năng**: 15
- **Tổng số use case**: 78 use cases
- **Đã hoàn thành**: 78 use cases ✅
- **Còn lại**: 0 use cases

**🎉 HOÀN THÀNH 100% TẤT CẢ USE CASES!**

## PRIORITY

### High Priority (Core Features)

1. Authentication
2. Patient Management
3. Appointment Management
4. Medical Record Management
5. Prescription Management
6. Invoice/Billing Management
7. Patient Portal / Self-Service

### Medium Priority

8. Staff Management
9. Medicine Inventory Management
10. Reception Management

### Low Priority

11. Medicine Import Management
12. Service Management
13. Disease Type Management
14. Payment Method Management
15. System Parameters Management

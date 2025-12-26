# LIST OF SCREENS - PRIVATE CLINIC MANAGEMENT SYSTEM

---

## 1. AUTHENTICATION & ACCOUNT (5 screens)

| No. | Name of Screen             | Type of Screen | Description/Note                                     |
| --- | -------------------------- | -------------- | ---------------------------------------------------- |
| 1   | Login Screen               | Form           | User authentication with email/username and password |
| 2   | Registration Screen        | Form           | Patient self-registration with personal information  |
| 3   | Forgot Password Screen     | Form           | Password recovery via email verification             |
| 4   | Account Information Screen | Detail View    | View and edit personal profile information           |
| 5   | Change Password Screen     | Form           | Update account password with validation              |

---

## 2. DASHBOARD / HOME (5 screens)

| No. | Name of Screen            | Type of Screen | Description/Note                                                                |
| --- | ------------------------- | -------------- | ------------------------------------------------------------------------------- |
| 6   | Warehouse Staff Dashboard | Main Screen    | Quick access to inventory management, medicine import operations                |
| 7   | Receptionist Dashboard    | Main Screen    | Quick access to patient registration, appointment booking, reception management |
| 8   | Doctor Dashboard          | Main Screen    | Quick access to examination, medical records management                         |
| 9   | Admin Dashboard           | Main Screen    | System overview, staff management, configuration shortcuts                      |
| 10  | Patient Dashboard         | Main Screen    | Patient portal home with appointments, medical history overview                 |

---

## 3. STAFF MANAGEMENT (5 screens)

| No. | Name of Screen                  | Type of Screen         | Description/Note                                                       |
| --- | ------------------------------- | ---------------------- | ---------------------------------------------------------------------- |
| 11  | Staff List Screen               | List & Search          | View, search, filter all staff (Receptionist, Warehouse Staff, Doctor) |
| 12  | Staff Details Screen            | Detail View            | View complete staff information and work schedule                      |
| 13  | Edit Staff Screen               | Form                   | Modify staff personal information                                      |
| 14  | Staff Work Schedule View Screen | Detail View (Optional) | View-only schedule display, edit via schedule management               |
| 15  | Add New Staff Screen            | Form                   | Create new staff account with role assignment                          |

---

## 4. WORK SCHEDULE MANAGEMENT (4 screens)

| No. | Name of Screen               | Type of Screen | Description/Note                                                     |
| --- | ---------------------------- | -------------- | -------------------------------------------------------------------- |
| 16  | Monthly Work Schedule Screen | Calendar View  | View and edit monthly schedule for all staff                         |
| 17  | Shift Detail Screen          | Modal/Popup    | View details of a specific shift (double-click on calendar cell)     |
| 18  | Edit Shift Screen            | Form           | Modify shift information (start time, end time, date, status)        |
| 19  | Add New Shift Screen         | Form           | Create new staff_schedule entry, optional drag-drop for date changes |

---

## 5. SYSTEM PARAMETERS (1 screen)

| No. | Name of Screen                | Type of Screen | Description/Note                                |
| --- | ----------------------------- | -------------- | ----------------------------------------------- |
| 20  | System Parameters List Screen | List & Edit    | View and modify system configuration parameters |

---

## 6. MEDICINE TYPE MANAGEMENT (7 screens)

| No. | Name of Screen               | Type of Screen        | Description/Note                                                                                                                                |
| --- | ---------------------------- | --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------- |
| 21  | Medicine Type List Screen    | List & Search         | View all medicine types with search and filter                                                                                                  |
| 22  | Medicine Type Details Screen | Detail View with Tabs | Tab 1: Basic info. Tab 2: Inventory batches (import date, expiry, import price, quantity). Tab 3: Price history (effective date, selling price) |
| 23  | Medicine Price Detail Screen | Detail View           | View specific price record details (can be merged with edit)                                                                                    |
| 24  | Edit Medicine Price Screen   | Form                  | Modify selling price (only same-day entries editable)                                                                                           |
| 25  | Add Medicine Price Screen    | Form                  | Add new selling price with effective date                                                                                                       |
| 26  | Edit Medicine Type Screen    | Form                  | Modify medicine name, unit of measurement                                                                                                       |
| 27  | Add Medicine Type Screen     | Form                  | Create new medicine type entry                                                                                                                  |

---

## 7. MEDICINE INVENTORY (2 screens - Optional)

| No. | Name of Screen                    | Type of Screen           | Description/Note                                                                                          |
| --- | --------------------------------- | ------------------------ | --------------------------------------------------------------------------------------------------------- |
| 28  | Medicine Inventory List Screen    | List & Search (Optional) | View all medicine_inventory records joined with medicine and import tables, including zero quantity items |
| 29  | Medicine Inventory Details Screen | Detail View              | Auto-updated based on import_detail, read-only                                                            |

---

## 8. MEDICINE IMPORT MANAGEMENT (7 screens)

| No. | Name of Screen                 | Type of Screen | Description/Note                                                                                        |
| --- | ------------------------------ | -------------- | ------------------------------------------------------------------------------------------------------- |
| 30  | Medicine Import List Screen    | List & Search  | View all import records (date, supplier, total quantity, total value)                                   |
| 31  | Medicine Import Details Screen | Detail View    | View import header info and list of import details                                                      |
| 32  | Edit Medicine Import Screen    | Form with Tabs | Tab 1: Edit import header. Tab 2: Add/edit/delete import details. May have time restriction for editing |
| 33  | Add Medicine Import Screen     | Form with Tabs | Tab 1: Basic import info. Tab 2: Add import details with medicine selection (combobox or search form)   |
| 34  | Import Detail View Screen      | Detail View    | View specific import detail (can be merged with edit)                                                   |
| 35  | Add Import Detail Screen       | Form           | Add medicine to import with quantity, price, expiry, batch number                                       |
| 36  | Edit Import Detail Screen      | Form           | Modify import detail record                                                                             |

---

## 9. PATIENT MANAGEMENT (4 screens)

| No. | Name of Screen         | Type of Screen        | Description/Note                                                                                                                                                |
| --- | ---------------------- | --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 37  | Patient List Screen    | List & Search         | View, search, filter all patients                                                                                                                               |
| 38  | Patient Details Screen | Detail View with Tabs | Tab 1: Personal info. Tab 2: Appointments list. Tab 3: Medical records (with prescription view button). Tab 4: Invoices (with service/medicine invoice buttons) |
| 39  | Edit Patient Screen    | Form                  | Modify patient personal information                                                                                                                             |
| 40  | Add Patient Screen     | Form                  | Register new patient                                                                                                                                            |

---

## 10. APPOINTMENT MANAGEMENT (4 screens - Optional main list)

| No. | Name of Screen             | Type of Screen           | Description/Note                                                                            |
| --- | -------------------------- | ------------------------ | ------------------------------------------------------------------------------------------- |
| 41  | Appointment List Screen    | List & Search (Optional) | View all appointments with filters                                                          |
| 42  | Appointment Details Screen | Detail View              | View appointment information                                                                |
| 43  | Edit Appointment Screen    | Form                     | Modify appointment, display doctor schedules for rescheduling                               |
| 44  | Add Appointment Screen     | Form                     | Create new appointment with patient search, doctor schedule display based on staff_schedule |

---

## 11. RECEPTION MANAGEMENT (4 screens)

| No. | Name of Screen           | Type of Screen | Description/Note                                                                                                                 |
| --- | ------------------------ | -------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| 45  | Reception List Screen    | List & Search  | View all receptions with queue number. Include "End Day" button to batch update status to Absent/Cancelled (POST without params) |
| 46  | Reception Details Screen | Detail View    | View reception information and status                                                                                            |
| 47  | Edit Reception Screen    | Form           | Change status from Waiting (no edit if reception date < current date)                                                            |
| 48  | Add Reception Screen     | Form           | Create new reception with patient search form                                                                                    |

---

## 12. DISEASE TYPE MANAGEMENT (4 screens)

| No. | Name of Screen              | Type of Screen | Description/Note                         |
| --- | --------------------------- | -------------- | ---------------------------------------- |
| 49  | Disease Type List Screen    | List & Search  | View all disease types                   |
| 50  | Disease Type Details Screen | Detail View    | View disease type information            |
| 51  | Edit Disease Type Screen    | Form           | Modify disease type name and description |
| 52  | Add Disease Type Screen     | Form           | Create new disease type                  |

---

## 13. MEDICAL RECORD MANAGEMENT (4 screens)

| No. | Name of Screen                | Type of Screen       | Description/Note                                                                                                                                                                                                             |
| --- | ----------------------------- | -------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 53  | Medical Record List Screen    | List & Search        | View all medical records. Doctor finds records via reception/examination slip to update and add prescriptions                                                                                                                |
| 54  | Medical Record Details Screen | Detail View with Tab | View record info. Tab: Prescription details with medicine list. Route to prescription detail                                                                                                                                 |
| 55  | Edit Medical Record Screen    | Form                 | Modify diagnosis, symptoms, notes                                                                                                                                                                                            |
| 56  | Add Medical Record Screen     | Form                 | Create record from reception (changes status to Done/In Examination), auto-creates invoice. Display today's receptions (filter by In Examination status for easier selection). Include refresh button for manual list update |

---

## 14. PRESCRIPTION MANAGEMENT (4 screens - Optional main list)

| No. | Name of Screen              | Type of Screen           | Description/Note                                                                    |
| --- | --------------------------- | ------------------------ | ----------------------------------------------------------------------------------- |
| 57  | Prescription List Screen    | List & Search (Optional) | View all prescriptions                                                              |
| 58  | Prescription Details Screen | Detail View              | View prescription info and medicine details list. Route to prescription detail view |
| 59  | Edit Prescription Screen    | Form                     | Modify prescription medicines                                                       |
| 60  | Add Prescription Screen     | Form                     | Create prescription with medicine search form, inventory quantity not critical      |

---

## 15. SERVICE MANAGEMENT (4 screens)

| No. | Name of Screen         | Type of Screen | Description/Note                        |
| --- | ---------------------- | -------------- | --------------------------------------- |
| 61  | Service List Screen    | List & Search  | View all medical services               |
| 62  | Service Details Screen | Detail View    | View service information and pricing    |
| 63  | Edit Service Screen    | Form           | Modify service name, description, price |
| 64  | Add Service Screen     | Form           | Create new medical service              |

---

## 16. PAYMENT METHOD MANAGEMENT (4 screens)

| No. | Name of Screen                | Type of Screen | Description/Note                   |
| --- | ----------------------------- | -------------- | ---------------------------------- |
| 65  | Payment Method List Screen    | List & Search  | View all payment methods           |
| 66  | Payment Method Details Screen | Detail View    | View payment method information    |
| 67  | Edit Payment Method Screen    | Form           | Modify payment method name, status |
| 68  | Add Payment Method Screen     | Form           | Create new payment method          |

---

## 17. INVOICE MANAGEMENT (6 screens)

| No. | Name of Screen               | Type of Screen        | Description/Note                                                                                                                                                  |
| --- | ---------------------------- | --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 69  | Invoice List Screen          | List & Search         | View all invoices. Staff search patient via invoice list, find invoice by record_id for billing                                                                   |
| 70  | Invoice Details Screen       | Detail View with Tabs | Tab 1: Invoice info with payment method. Tab 2: Service invoice details. Tab 3: Medicine invoice details                                                          |
| 71  | Edit Medicine Invoice Screen | Form                  | Add/edit/delete medicine details (dispensing). Medicine selection with inventory availability check (min 3 months before expiry). If unavailable, cannot dispense |
| 72  | Edit Service Invoice Screen  | Form                  | Add/edit/delete service details with quantity                                                                                                                     |
| 73  | Add Invoice Screen           | Form                  | Auto-created with medical record (optional manual)                                                                                                                |
| 74  | Process Payment Screen       | Form                  | Record payment with method selection                                                                                                                              |

---

## 18. PATIENT PORTAL (7 screens)

| No. | Name of Screen                | Type of Screen        | Description/Note                                                                              |
| --- | ----------------------------- | --------------------- | --------------------------------------------------------------------------------------------- |
| 75  | Book Appointment Screen       | Form                  | Patient self-booking with doctor/date selection                                               |
| 76  | Medical Record History Screen | List                  | View patient's own medical record history                                                     |
| 77  | Medical Record Detail Screen  | Detail View           | View prescription form with medicine list, record info joined with reception and disease type |
| 78  | Appointment History Screen    | List                  | View patient's appointment history                                                            |
| 79  | Invoice History Screen        | List                  | View patient's invoice history                                                                |
| 80  | Invoice Detail Screen         | Detail View with Tabs | Tab 1: Invoice info with payment method. Tab 2: Service details. Tab 3: Medicine details      |
| 81  | Patient Home Screen           | Main Screen           | Patient portal dashboard with overview                                                        |

---

## SUMMARY

| Category                   | Screen Count |
| -------------------------- | ------------ |
| Authentication & Account   | 5            |
| Dashboard / Home           | 5            |
| Staff Management           | 5            |
| Work Schedule Management   | 4            |
| System Parameters          | 1            |
| Medicine Type Management   | 7            |
| Medicine Inventory         | 2            |
| Medicine Import Management | 7            |
| Patient Management         | 4            |
| Appointment Management     | 4            |
| Reception Management       | 4            |
| Disease Type Management    | 4            |
| Medical Record Management  | 4            |
| Prescription Management    | 4            |
| Service Management         | 4            |
| Payment Method Management  | 4            |
| Invoice Management         | 6            |
| Patient Portal             | 7            |
| **TOTAL**                  | **81**       |

---

## SCREEN TYPE LEGEND

| Type                  | Description                                      |
| --------------------- | ------------------------------------------------ |
| Main Screen           | Dashboard/Home page with navigation and overview |
| List & Search         | Table view with search, filter, and pagination   |
| Detail View           | Read-only display of record information          |
| Detail View with Tabs | Multi-tab detail view for complex entities       |
| Form                  | Input form for create/edit operations            |
| Form with Tabs        | Multi-step or multi-section input form           |
| Modal/Popup           | Overlay dialog for quick actions                 |
| Calendar View         | Calendar-based schedule display                  |

---

## NOTES

- **(Optional)** screens can be implemented as secondary priority
- Screens marked with route navigation should support deep linking
- Tab-based screens should maintain state when switching tabs
- Forms should include validation and error handling
- List screens should support pagination (20-50 items per page)

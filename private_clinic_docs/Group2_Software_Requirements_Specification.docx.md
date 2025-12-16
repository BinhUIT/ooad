

# **Software Requirements Specification**

# **for**

# **Private Clinic System**

**Version 1.0 approved**

**Prepared by Đặng Lê Bình – Phan Lê Minh –**   
**Bùi Thị Phương Huyền – Đặng Phú Thiện**

**Group 2 Object-Oriented Analysis and Design**

**25/09/2025**

**Table of Contents**

**Table of Contents	[ii](#table-of-contents)**

**Revision History	[ii](#revision-history)**

**1\.**	**Introduction	[2](#introduction)**  
1.1	Document Purpose	[2](#this-software-requirements-specification-\(srs\)-describes-the-requirements-for-the-private-clinic-management-system-\(pcms\)-developed-by-group-02,-class-se100.q11.-the-purpose-of-this-document-is-to-provide-a-comprehensive-overview-of-the-system-and-serve-as-a-reference-for-all-stakeholders.-the-document-is-organized-into-sections-that-explain-the-scope,-purpose,-standards,-and-references-of-the-system.-readers-can-use-this-srs-to-understand-how-the-system-supports-private-clinic-operations,-including-patient-registration,-appointment-scheduling,-medical-examination,-prescription-management,-inventory-tracking,-billing,-and-reporting.)  
1.2	Document Conventions	[2](#the-product-described-in-this-document-is-the-private-clinic-management-system-\(pcms\)-for-phúc-sinh-private-clinic,-nha-trang.-this-version-\(release-1.0\)-automates-daily-clinic-operations-to-improve-efficiency,-accuracy,-and-transparency.)  
1.3	Project Scope	[2](#this-srs-follows-a-structured-format-inspired-by-ieee-830-standards-for-software-requirement-specifications.-the-following-conventions-are-applied:)  
1.4	References	[2](#the-private-clinic-management-system-is-a-desktop/web-based-application-designed-to-support-the-full-cycle-of-clinic-operations-at-phúc-sinh-private-clinic.-the-system-will:)

**2\.**	**Overall Description	[2](#the-following-resources-and-documents-were-referenced-in-preparing-this-srs:)**  
2.1	Product Perspective	[2](#this-section-provides-a-high-level-overview-of-the-private-clinic-management-system-\(pcms\),-the-environment-in-which-it-will-be-used,-the-types-of-users-expected-to-interact-with-it,-and-the-constraints,-assumptions,-and-dependencies-that-influence-the-development-process.-the-goal-is-to-help-stakeholders-understand-how-the-system-fits-into-the-clinic’s-daily-operations-and-how-it-supports-end-users-in-delivering-healthcare-services-effectively.)  
2.2	User Classes and Characteristics	[2](#the-pcms-is-a-standalone-product-designed-to-replace-manual-workflows-in-small--to-medium-sized-private-clinics.-it-is-not-an-upgrade-of-an-existing-application-but-a-new-system-that-automates-core-functions-such-as-patient-registration,-appointment-scheduling,-medical-examination,-prescription-generation,-inventory-control,-and-billing.)  
2.3	Operating Environment	[2](#the-system-is-intended-for-several-classes-of-users,-each-with-specific-roles-and-access-permissions:)  
2.4	Design and Implementation Constraints	[2](#the-software-will-operate-in-the-following-environment:)  
2.5	Assumptions and Dependencies	[2](#the-backend-must-be-implemented-using-java-spring-boot-and-the-frontend-using-reactjs.)

**3\.**	**System Features	[2](#it-is-assumed-that-clinic-staff-have-basic-computer-literacy-to-operate-the-system-effectively.)**  
3.1	System Feature 1	[2](#this-section-illustrates-the-organization-of-the-functional-requirements-for-the-product-by-system-features,-which-represent-the-major-services-provided-by-the-system.-the-features-are-organized-according-to-user-classes-and-core-business-processes.-each-feature-includes-a-description,-stimulus/response-sequences,-and-specific-functional-requirements.)  
3.2	System Feature 2 (and so on)	[2](#fr1:-allow-patients-to-book-appointments-online.)

**4\.**	**Data Requirements	[2](#data-requirements)**  
4.1	Logical Data Model	[2](#logical-data-model)  
4.2	Data Dictionary	[2](#the-logical-data-model-consists-of-several-core-entities-and-their-relationships:)  
4.3	Reports	[2](#patientid:-unique-identifier,-type-=-integer,-format-=-auto-increment,-mandatory.)  
4.4	Data Acquisition, Integrity, Retention, and Disposal	[2](#the-system-will-provide-both-operational-and-management-level-reports:)

**5\.**	**External Interface Requirements	[2](#acquisition:-data-is-collected-through-staff-input-\(reception,-doctors,-warehouse-clerks\)-and-online-patient-registration.-drug-acquisition-records-are-entered-upon-new-stock-arrivals.)**  
5.1	User Interfaces	[2](#user-interfaces)  
5.2	Software Interfaces	[2](#the-private-clinic-management-system-will-provide-a-react-based-web-interface-with-the-following-features:)  
5.3	Hardware Interfaces	[2](#the-system-will-interact-with-multiple-software-components:)  
5.4	Communications Interfaces	[2](#client-side-hardware:-pcs-or-tablets-used-by-doctors,-receptionists,-and-pharmacists.)

**6\.**	**Quality Attributes	[2](#protocols:-https-for-secure-data-transmission-between-frontend-and-backend.)**  
6.1	Usability	[2](#usability)  
6.2	Performance	[2](#the-system-must-be-easy-to-use-for-non-technical-clinic-staff.)  
6.3	Security	[2](#api-calls-\(appointment-booking,-patient-search\)-should-respond-within-1-second-under-normal-load.)  
6.4	Safety	[2](#role-based-access-control-for-administrator,-doctor,-receptionist,-pharmacist.)  
6.5	\[Others as relevant\]	[2](#patient-data-must-not-be-lost-or-corrupted;-aiven’s-automatic-backup-is-mandatory.)

**7\.**	**Internationalization and Localization Requirements	[2](#availability:-system-uptime-≥-99.5%-during-clinic-working-hours.)**

**8\.**	**Other Requirements	[2](#language-support:)**

**9\.	Glossary	[2](#heading=h.f9fmqji16eej)**

**10\.	Analysis Models	[2](#heading=h.lisde6vu8too)**

**Revision History**

| Name | Date | Reason For Changes | Version |
| :---- | :---- | :---- | :---- |
|  |  |  |  |
|  |  |  |  |

1. # **Introduction** {#introduction}

This Software Requirements Specification (SRS) describes the requirements for the Private Clinic Management System (PCMS) developed by Group 02, Class SE100.Q11. The purpose of this document is to provide a comprehensive overview of the system and serve as a reference for all stakeholders. The document is organized into sections that explain the scope, purpose, standards, and references of the system. Readers can use this SRS to understand how the system supports private clinic operations, including patient registration, appointment scheduling, medical examination, prescription management, inventory tracking, billing, and reporting.

1. ## **Document Purpose**

The product described in this document is the **Private Clinic Management System (PCMS)** for **Phúc Sinh Private Clinic, Nha Trang**. This version (Release 1.0) automates daily clinic operations to improve efficiency, accuracy, and transparency.

This document is intended for multiple audiences:

**Developers** – to understand functional and non-functional requirements for system implementation.

**Project Managers** – to track project scope and ensure the system aligns with business objectives.

**Testers/QA Engineers** – to verify that the system fulfills the requirements specified.

**Clinic Staff (Doctors, Receptionists, Pharmacists/Accountants)** – to understand the scope of functionalities they will use.

**System Administrators** – to manage user access, permissions, and system configurations.

2. ## Documentation Writers **– to prepare user manuals and training materials. Document Conventions**

This SRS follows a structured format inspired by IEEE 830 standards for software requirement specifications. The following conventions are applied:

**Requirement Identifiers**: Each functional requirement will be labeled as **FR-xx** (e.g., FR-01: Patient Registration). Each non-functional requirement will be labeled as **NFR-xx**.

**Bold Text**: Indicates system components, modules, or keywords (e.g., **Patient**, **Invoice**, **Appointment**).

*Italic Text*: Highlights important notes or emphasis.

**Monospaced Text**: Used for code snippets, database field names, and unique identifiers.

Status values (e.g., *Pending*, *Completed*, *Cancelled*) are written in *italics* to distinguish them from descriptive text.

3. ## **Project Scope**

The **Private Clinic Management System** is a desktop/web-based application designed to support the full cycle of clinic operations at Phúc Sinh Private Clinic. The system will:

Maintain **patient records** including personal and medical history.

Manage **appointments** with conflict detection for doctors’ schedules.

Record **medical examinations** with diagnoses, services, and prescribed medicines.

Support **prescription and drug inventory management**, including FEFO (First-Expire-First-Out) stock handling.

Generate **invoices** for consultation, services, and medicines with multiple payment options.

Provide **reports** such as daily/monthly revenue, medicine usage, inventory, and pending invoices.

The main objectives of this project are to **digitize clinic operations**, reduce errors in manual processes, ensure regulatory compliance in medicine handling, and improve overall patient experience. The system directly supports the clinic’s business strategy of delivering efficient, transparent, and high-quality healthcare services.

4. ## **References**

The following resources and documents were referenced in preparing this SRS:  
IEEE Std 830-1998 – IEEE Recommended Practice for Software Requirements Specifications. IEEE Computer Society. Available at: [830-1998 \- IEEE Recommended Practice for Software Requirements Specifications](https://ieeexplore.ieee.org/document/720574)

World Health Organization (WHO). Essential environmental health standards in health care. Availblable at:  
[Essential environmental health standards in health care](https://www.who.int/publications/i/item/9789241547239)

2. # **Overall Description**

This section provides a high-level overview of the **Private Clinic Management System (PCMS)**, the environment in which it will be used, the types of users expected to interact with it, and the constraints, assumptions, and dependencies that influence the development process. The goal is to help stakeholders understand how the system fits into the clinic’s daily operations and how it supports end-users in delivering healthcare services effectively.

1. ## **Product Perspective**

The **PCMS** is a standalone product designed to replace manual workflows in small- to medium-sized private clinics. It is not an upgrade of an existing application but a new system that automates core functions such as patient registration, appointment scheduling, medical examination, prescription generation, inventory control, and billing.

The system is structured as a **client-server application** with two main layers:

**Backend**: Built with **Java Spring Boot**, handling business logic, database management, and API integration.

**Frontend**: Developed using **ReactJS**, delivering a responsive, user-friendly interface accessible from standard browsers.

The architecture allows future expansion, such as integration with third-party pharmacy management systems or government healthcare reporting tools via RESTful APIs.

2. ## **User Classes and Characteristics**

The system is intended for several classes of users, each with specific roles and access permissions:

**Patients (Customers)**

* Can register basic information during their first visit.  
  * View their appointment schedules, prescriptions, and invoices.  
  * May receive reminders for upcoming appointments or medicine refills.  
  * Require a very simple, intuitive interface with minimal technical knowledge.

  **Doctors**

  * View and update patient medical history.  
  * Record diagnoses, prescribe medicines, and request laboratory tests.  
  * Require quick access to accurate information with minimal navigation.

  **Receptionists/Assistants**

  * Register patients, schedule appointments, and handle check-ins.  
  * Process payments and issue invoices.  
  * Need a simple, guided interface to minimize errors.

  **Pharmacy Staff**

  * Fulfill prescriptions, manage drug inventory, and monitor stock levels.  
  * Require real-time updates to prevent stockouts or overstocking.

  **Administrators**

  * Manage system users, assign roles, configure access rights, and generate reports.  
  * Need higher-level system control and audit capabilities.

Each user class interacts with only the relevant modules, ensuring usability and security while avoiding unnecessary complexity.

3. ## **Operating Environment**

The software will operate in the following environment:  
**Backend**: Java Spring Boot running on JDK 17+ in Linux or Windows server environments.

**Frontend**: ReactJS application running in modern browsers (Google Chrome, Microsoft Edge, Mozilla Firefox).

**Database**: MySQL as the primary relational database system.

**Deployment**: Can be hosted on local servers or cloud infrastructure (Docker-enabled for scalability).

**Network**: Local clinic intranet with secure HTTPS connections for remote access.

**User Devices**: 

* **Minimum**: Dual-core processor, 2 GB RAM, stable internet connection.

  * **Recommended**: 4 GB RAM or more for smoother multitasking with other applications.

  4. ## **Design and Implementation Constraints**

  The backend **must** be implemented using **Java Spring Boot** and the frontend using **ReactJS**.


  Sensitive patient and financial data **must** comply with healthcare data protection regulations.


  The system should be accessible only on devices with modern browsers that support ES6+ JavaScript features.


  Integration with third-party APIs (e.g., payment gateways or lab systems) must conform to RESTful standards.


  Hardware requirements: servers with at least 8 GB RAM, quad-core processors, and SSD storage for database operations.


  Budget and time limitations restrict the initial release (v1.0) to essential modules, with advanced reporting and analytics planned for future versions.

  5. ## **Assumptions and Dependencies**

  It is assumed that clinic staff have basic computer literacy to operate the system effectively.


  It is assumed that the clinic maintains stable internet connectivity for synchronization between frontend and backend.


  The system depends on third-party libraries, frameworks (Spring Boot, ReactJS), and open-source packages, which must remain actively maintained.


  External dependencies include hosting infrastructure (cloud providers or on-premises servers) and RDBMS support.


  If regulatory requirements for medical software change, the system may need modifications to maintain compliance.


  Long-term scalability depends on the availability of cloud deployment and containerization technologies.

3. # **System Features**

This section illustrates the organization of the functional requirements for the product by system features, which represent the major services provided by the system. The features are organized according to user classes and core business processes. Each feature includes a description, stimulus/response sequences, and specific functional requirements.

1. ## **Appointment Scheduling**

   1. ### 	**Description**

   High priority. The system allows patients to book appointments either online or directly at the clinic. This ensures efficient allocation of daily examination slots and prevents overbooking.

      2. ### 	**Stimulus/Response Sequences**

   Patient online booking: Patient selects preferred date/time → System checks availability → System confirms and sends notification.

   

   Walk-in booking at clinic: Receptionist inputs patient details → System validates against daily limit (max 40 patients) → System adds to appointment list and confirms booking.

   

   If the daily capacity is exceeded → System notifies users and blocks new bookings.

      3. ### **Functional Requirements**

   FR1: Allow patients to book appointments online.

   FR2: Allow staff to create appointments for walk-in patients.

   FR3: Enforce a daily maximum of **TBD** patients.

   FR4: Notify patients/staff if the selected time slot or day is full.

   2. ## **Medical Examination Management**

      1. ### 	**Description**

   High priority. Supports doctors in recording diagnoses, symptoms, and prescribed services.

      2. ### 	**Stimulus/Response Sequences**

   Doctor opens patient record → System displays medical form → Doctor enters diagnosis and treatment plan.

   

   If mandatory fields are missing → System displays warning.

      3. ### **Functional Requirements**

   FR1: Allow doctors to record symptoms, diagnosis, and notes.

   FR2: Link medical form to the patient’s record.

   FR3: Validate required fields before saving.

   3. ## **Prescription Management**

      1. ### 	**Description**

   High priority. The system supports doctors in prescribing medication and ensures inventory validation. Doctors prescribe freely, but the system alerts them if drugs are unavailable or insufficient. Staff only confirm dispensing and billing for available drugs.

      2. ### 	**Stimulus/Response Sequences**

   Doctor creates prescription → System validates against inventory → If insufficient/unavailable drugs are found → System alerts doctor → Doctor decides to change or keep prescription.

   

   Staff reviews prescription → System automatically filters out unavailable drugs → Staff confirms dispensing of available drugs → Invoice includes only dispensed medicines.

      3. ### **Functional Requirements**

   FR1: Allow doctors to create prescriptions without inventory restrictions.

   FR2: Automatically check inventory for prescribed drugs.

   FR3: Notify the doctor if a drug is unavailable or insufficient.

   FR4: Allow doctors to modify or keep prescriptions after system alert.

   FR5: Allow staff to confirm dispensing of only available drugs.

   FR6: Exclude unavailable drugs from invoices; notify patient to purchase outside.

   4. ## **Service Billing**

      1. ### **Description**

   High priority. Generate invoices for medical services and prescriptions.

      2. ### 	**Stimulus/Response Sequences**

   Staff finalizes services/medicines → System generates invoice → Patient pays and system records transaction.

      3. ### **Functional Requirements**

   FR1: Generate invoices for services and medicines.

   FR2: Support multiple payment methods.

   FR3: Prevent invoice duplication.

   5. ## **Prescription Management**

      1. ### **Description**

   Medium priority. Tracks available medicines and updates stock after prescriptions.

      2. ### 	**Stimulus/Response Sequences**

   New medicine stock added → System updates inventory.

   

   Medicine prescribed → System automatically deducts from inventory.

      3. ### **Functional Requirements**

   FR1: Allow adding/editing/deleting medicines in inventory.

   FR2: Automatically update stock after prescription.

   FR3: Notify low-stock medicines.

   6. ## **Reporting & Analytics**

      1. ### **Description**

   Medium priority. Provides reports and insights for administrators.

      2. ### 	**Stimulus/Response Sequences**

   Admin selects “Generate Report” → System produces charts/tables (patients, revenue, medicine usage).

      3. ### **Functional Requirements**

   FR1: Generate daily/weekly/monthly reports.

   FR2: Provide charts and graphs.

   FR3: Allow exporting to PDF/Excel.

   FR4: Restrict access based on user roles.

4. # **Data Requirements** {#data-requirements}

The system must manage data related to patients, staff, appointments, medical examinations, prescriptions, drugs, invoices, and inventory. Each patient is assigned a unique identifier and can have multiple visits and examination records. Appointments must prevent schedule conflicts for doctors. Drug management requires tracking multiple lots with different expiration dates and purchase prices, applying the FEFO (First-Expire-First-Out) principle. Invoices must capture consultation, service, and medication costs, and remain immutable once finalized to ensure transparency. Reports will aggregate data for managerial insights, including revenue, patient visits, drug usage, and stock levels.

1. ## **Logical Data Model** {#logical-data-model}

The logical data model consists of several core entities and their relationships:

Patient: Stores demographic and contact information. Linked to appointments, reception entries, and examination records.

Staff: Includes doctors, receptionists, and warehouse clerks. Associated with appointments, examinations, prescriptions, drug entries, and invoices.

Appointment: Connects patients and doctors, including date, time, and status. Prevents overlapping schedules.

Reception: Represents patient check-in, linked to appointments and examination records.

Examination Record (Medical Form): Contains symptoms, diagnosis, consultation fees, and links to doctor, patient, and prescriptions.

Prescription: Lists prescribed drugs, quantities, dosage, and total costs. Connected to both examinations and drugs.

Drug: Captures drug details (composition, unit, dosage, manufacturer, pricing). Linked to inventory and prescription records.

Drug Lot / Inventory: Tracks quantity, expiration date, storage type, and usage status. Updated via purchase entries and consumption in prescriptions.

Invoice: Combines costs of consultations, services, and drugs. Connected to patient and examination records.

Reports: Generated from transactional data to support decision-making.  
*![A computer screen shot of a computer flow chartAI-generated content may be incorrect.][image1]*

2. ## **Data Dictionary**

PatientID: Unique identifier, type \= integer, format \= auto-increment, mandatory.

PatientName: Full name, type \= string, length \= 100, mandatory.

DateOfBirth: Patient birth date, type \= date, format \= YYYY-MM-DD.

Gender: Male/Female/Other, type \= enum.

PhoneNumber: Contact number, type \= string, length \= 15, format \= \[0-9+\].

AppointmentStatus: Enum {Scheduled, Completed, Cancelled, Absent}.

DrugID: Unique identifier for drugs, type \= integer.

DrugName: Commercial name, type \= string, length \= 150\.

LotNumber: Identifier for drug batches, type \= string, length \= 50\.

ExpiryDate: Expiration date of a drug lot, type \= date.

InvoiceTotal: Total amount, type \= decimal(12,2), format \= currency.

PaymentStatus: Enum {Pending, Paid, Cancelled}.

3. ## **Reports**

The system will provide both operational and management-level reports:

Revenue Reports: Summarize consultation and medication fees by day, month, and doctor.

Drug Usage Reports: Detail consumption of drugs, highlighting high-demand medications.

Inventory Reports: Show current stock levels, low-stock alerts, and upcoming expirations.

Patient Visit Reports: Track new versus returning patients, number of appointments, and no-  
shows.

Outstanding Invoices: List unpaid invoices with patient details for follow-up.

Reports will be generated in tabular format with sorting and filtering options, and may be exported to PDF or Excel for external use.

4. ## **Data Acquisition, Integrity, Retention, and Disposal**

Acquisition: Data is collected through staff input (reception, doctors, warehouse clerks) and online patient registration. Drug acquisition records are entered upon new stock arrivals.

Integrity: The system enforces primary/foreign key constraints, validation rules (e.g., appointment conflicts, drug expiration checks), and immutable invoices once paid. Transaction logging ensures accountability.

Retention: Patient records and invoices must be retained indefinitely for legal and medical auditing. Inventory and drug lot data are retained until regulatory disposal requirements are met.

Disposal: Expired or invalid data (e.g., drug lots marked as discarded) are archived but not permanently deleted, ensuring traceability. Temporary caches and backups are automatically purged after a predefined retention period (e.g., 30–90 days).

Backup and Recovery: The system performs daily backups and maintains redundancy (checkpointing, mirroring). Recovery policies ensure minimal data loss in case of failure.

5. # **External Interface Requirements**

   1. ## **User Interfaces** {#user-interfaces}

The Private Clinic Management System will provide a React-based web interface with the following features:

Login screen for doctors, receptionists, pharmacists, and administrators.

Dashboard showing daily appointments, pending invoices, and notifications.

Patient management forms with validation and search functionality.

Prescription and pharmacy management screens with auto-complete drug search.

Invoice and payment forms with clear breakdown of consultation, medication, and service fees.

Consistent design using Material UI, responsive layout for desktop and tablet.

Error messages and confirmations displayed through modal dialogs.

2. ## **Software Interfaces**

The system will interact with multiple software components:

Backend: Spring Boot 3.x providing REST APIs.

Database: MySQL hosted on Aiven Cloud for scalability and reliability.

Persistence: JPA/Hibernate for ORM mapping.

Security: Spring Security with JWT-based authentication and role-based authorization.

Frontend: React 18 consuming REST APIs.

External Services: SMTP email service for sending appointment reminders and billing notifications.

Data Formats: JSON for client-server communication, SQL for database operations.

3. ## **Hardware Interfaces**

Client-side hardware: PCs or tablets used by doctors, receptionists, and pharmacists.

Supported browsers: Chrome, Edge, Firefox.

Printers for prescriptions, invoices, and monthly reports.

No additional specialized hardware is required, but optional barcode scanners may be used for medication management.

4. ## **Communications Interfaces**

Protocols: HTTPS for secure data transmission between frontend and backend.

Authentication: JWT tokens issued by Spring Security.

Email Notifications: Sent via SMTP integration (appointment confirmations, reminders).

Cloud Hosting: Backend and database hosted on Aiven for high availability.

CORS Policy: Configured to allow communication between React frontend and Spring Boot backend.

6. # **Quality Attributes**

   1. ## **Usability** {#usability}

The system must be easy to use for non-technical clinic staff.

Training for new users should not exceed 2 hours for basic operations (register patient, book appointment, issue invoice).

Forms must include validation and tooltips to guide staff.

Responsive design for use on desktop and tablet devices.

2. ## **Performance**

API calls (appointment booking, patient search) should respond within 1 second under normal load.

Support up to 150 concurrent users (doctors, staff, and patients accessing the portal).

Daily report generation must complete in less than 10 seconds.

Aiven-managed MySQL must handle at least 50,000 patient records efficiently.

3. ## **Security**

Role-based access control for Administrator, Doctor, Receptionist, Pharmacist.

Authentication via Spring Security with JWT tokens.

Passwords hashed using BCrypt with salting.

Patient medical history encrypted at rest by Aiven-managed MySQL.

All sensitive data transmitted over TLS 1.2+ connections.

Automatic logout after 15 minutes of inactivity.

4. ## **Safety**

Patient data must not be lost or corrupted; Aiven’s automatic backup is mandatory.

Double confirmation before deleting patient records or invoices.

Safeguards to prevent scheduling conflicts in appointments.

Protection against invalid prescriptions (e.g., drug interactions flagged for review).

5. ## **Other Quality Attributes**

Availability: System uptime ≥ 99.5% during clinic working hours.

Reliability: Database replication and failover supported by Aiven.

Scalability: Ability to expand from single-clinic to multi-branch operations.

Portability: Backend deployable to other cloud providers supporting Docker and Java.

Maintainability: Codebase follows layered architecture (Controller, Service, Repository) for easier updates.

Interoperability: Designed for future integration with insurance or national health systems via APIs.

7. # **Internationalization and Localization Requirements**

Language Support:  
The system must support the Vietnamese language as the default. Future extensions may include English for foreign patients or staff.

Date and Time Formatting:  
Dates must be displayed in the format dd/MM/yyyy to align with Vietnamese conventions. The system should allow configuration to switch formats if deployed in another country.

Currency:  
All billing and invoices are in Vietnamese Dong (VND). The system should allow currency settings for international expansion.

Character Encoding:  
The system must support UTF-8 encoding to correctly display Vietnamese diacritics and other international character sets.

8. # **Other Requirements**

Regulatory Compliance:  
The system must comply with Vietnamese regulations regarding medical records and prescription management.

Audit Trail:  
The system must maintain logs of prescription creation, modification, and dispensing for accountability and tracking.

System Startup and Shutdown:  
The system must be able to restart gracefully without data loss, ensuring database integrity.

Security:  
Only authorized doctors can prescribe medicines; only authorized staff can confirm dispensing. Authentication and role-based access control are required.

9. # **Glossary**

| Term / Acronym | Definition |
| ----- | ----- |
| **PCMS (Private Clinic Management System)** | The system being developed in this project. |
| **FR (Functional Requirement)** | Describes a specific functionality the system must provide. |
| **NFR (Non-Functional Requirement)** | Describes quality attributes such as performance, security, or usability. |
| **FEFO (First-Expire-First-Out)** | Inventory management method that ensures medicines with the earliest expiration dates are dispensed first. |
| **JWT (JSON Web Token)** | A standard token format used for authentication and authorization in the system. |
| **ORM (Object-Relational Mapping)** | A technique that maps objects in code to database tables (implemented via JPA/Hibernate). |
| **API (Application Programming Interface)** | A set of rules that allows the frontend and backend to communicate. |
| **RESTful API** | API architecture style based on HTTP methods such as GET, POST, PUT, DELETE. |
| **TLS (Transport Layer Security)** | A protocol for encrypting data in transit over the network. |
| **Backup/Recovery** | Mechanisms to ensure data can be restored in case of system failure or data loss. |
| **Invoice** | A billing document generated for patient services and prescribed medicines. |
| **Appointment** | A scheduled consultation between a patient and a doctor, booked online or at the clinic. |
| **Prescription** | A medical order created by a doctor that lists medicines and treatments for a patient. |
| **Inventory** | The clinic’s stock of medicines, including quantities and expiration dates. |
| **Administrator** | The system user with the highest level of control, responsible for configurations and audits. |
| **Receptionist** | Staff member responsible for patient registration, appointment booking, and billing. |
| **Pharmacist** | Staff member responsible for inventory management and dispensing medicines. |
| **Stakeholder** | Any individual or organization with an interest in the system (e.g., doctors, patients, managers, regulatory bodies). |
| **Audit Trail** | A system log that records all activities to ensure traceability and accountability (e.g., prescriptions, invoices). |
| **RDBMS (Relational Database Management System)** | A software system for creating, managing, and querying relational databases using structured tables. Example: MySQL, PostgreSQL. |
| **TBD (To Be Determined)** | A placeholder indicating that the specific value or detail will be defined later. |
| **Spring Boot** | A Java-based framework used for backend development and API implementation. |
| **ReactJS** | A JavaScript library for building responsive and interactive user interfaces. |
| **Material UI** | A React component library used for consistent, responsive, and user-friendly design. |
| **MySQL** | A relational database management system used to store and query system data. |
| **Aiven Cloud** | Cloud provider used for hosting MySQL databases with scalability and reliability. |
| **JPA (Java Persistence API)** | Java standard specification for ORM (mapping objects to relational data). |
| **Hibernate** | A framework implementing JPA for ORM and database operations. |
| **SMTP (Simple Mail Transfer Protocol)** | Protocol used by the system to send emails such as appointment reminders and invoices. |
| **ES6+ (ECMAScript 6 and above)** | Modern JavaScript standard supported by the frontend. |
| **Docker** | A containerization platform enabling scalable and portable deployment. |
| **Bcrypt** | A secure password-hashing algorithm with salting, used for authentication. |
| **CORS (Cross-Origin Resource Sharing)** | A browser mechanism that allows the React frontend to communicate with the backend API securely. |
| **RBAC (Role-Based Access Control)** | Security mechanism restricting system access based on user roles. |
| **Entity-Relationship Diagram (ERD)** | A visual model representing entities and relationships in the system’s database. |
| **State-Transition Diagram** | A diagram showing how system states change in response to events (e.g., appointment: Scheduled → Completed → Cancelled). |
| **Audit Log** | A detailed chronological record of system operations (technical part of the Audit Trail). |

10. # **Analysis Models**

***ERD diagrams***

***DFD diagrams level 1***

[image1]: <data:image/png;base64,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>
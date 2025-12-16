# Sequence Add Medical Record

## Description

This sequence diagram describes creating a new medical record after patient examination.

## Diagram

<!-- diagram id="sequence-manage-medical-record-add-new" -->

```plantuml
@startuml
autonumber

actor Doctor as Dr
boundary MedicalRecordView as MRV
control MedicalRecordController as MRC
entity MEDICAL_RECORD as MR
entity RECEPTION as R
entity INVOICE as INV

title Add Medical Record Sequence

Dr -> MRV: Select reception to examine (receptionID)
activate Dr
activate MRV

MRV -> MRC: Request reception details
activate MRC

MRC -> R: Get reception details
activate R
R -> R: Query reception by ID
activate R
deactivate R

break Reception not found
  MRC <-- R: Error notification
  MRV <-- MRC: Error notification
  MRV -> MRV: Display reception not found
  activate MRV
  deactivate MRV
end

MRC <-- R: Reception data
deactivate R

MRV <-- MRC: Reception information
deactivate MRC
MRV -> MRV: Display examination form
activate MRV
deactivate MRV

Dr -> MRV: Enter examination details (symptoms, diagnosis, services)
Dr -> MRV: Click "Save"
deactivate Dr

MRV -> MRV: Validate input
activate MRV
deactivate MRV

break Invalid input
  MRV -> MRV: Display validation error
  activate MRV
  deactivate MRV
end

MRV -> MRC: Send create medical record request
activate MRC

MRC -> MR: Create medical record
activate MR
MR -> MR: Insert medical record
activate MR
deactivate MR
MRC <-- MR: Medical record created
deactivate MR

MRC -> R: Update reception status to "Done"
activate R
R -> R: Update reception status
activate R
deactivate R
MRC <-- R: Status updated
deactivate R

MRC -> INV: Create invoice with default values
activate INV
INV -> INV: Insert invoice record
activate INV
deactivate INV
MRC <-- INV: Invoice created
deactivate INV

MRV <-- MRC: Success notification
deactivate MRC
MRV -> MRV: Display success message
activate MRV
deactivate MRV

deactivate MRV

@enduml
```

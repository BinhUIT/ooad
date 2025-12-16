# Sequence Edit Medical Record

## Description

This sequence diagram describes editing an existing medical record (limited to adding notes/corrections).

## Diagram

<!-- diagram id="sequence-manage-medical-record-edit" -->

```plantuml
@startuml
autonumber

actor Doctor as Dr
boundary MedicalRecordDetailView as MRDV
boundary EditMedicalRecordView as EMRV
control MedicalRecordController as MRC
entity MEDICAL_RECORD as MR

title Edit Medical Record Sequence

Dr -> MRDV: Select "Edit" from medical record details
activate Dr
activate MRDV

MRDV -> MRC: Request medical record data
activate MRC

MRC -> MR: Get medical record information
activate MR
MR -> MR: Query medical record by ID
activate MR
deactivate MR

break Medical record not found
  MRC <-- MR: Error notification
  MRDV <-- MRC: Error notification
  MRDV -> MRDV: Display medical record not found
  activate MRDV
  deactivate MRDV
end

MRC <-- MR: Medical record data
deactivate MR

MRDV <-- MRC: Medical record information
deactivate MRC
MRDV -> EMRV: Navigate to edit form
deactivate MRDV
activate EMRV
EMRV -> EMRV: Display edit form with current data
activate EMRV
deactivate EMRV

Dr -> EMRV: Update medical record (add notes, update diagnosis)
Dr -> EMRV: Click "Save"
deactivate Dr

EMRV -> EMRV: Validate input data
activate EMRV
deactivate EMRV

break Invalid data
  EMRV -> EMRV: Display validation error
  activate EMRV
  deactivate EMRV
end

EMRV -> MRC: Send update request
activate MRC

MRC -> MR: Update medical record
activate MR
MR -> MR: Update medical record information
activate MR
deactivate MR
MRC <-- MR: Update successful
deactivate MR

EMRV <-- MRC: Success notification
deactivate MRC
EMRV -> EMRV: Display success message
activate EMRV
deactivate EMRV

deactivate EMRV

@enduml
```

# Sequence View Reception Details

## Description

This sequence diagram describes viewing detailed information of a reception.

## Diagram

<!-- diagram id="sequence-manage-reception-view-details" -->

```plantuml
@startuml
autonumber

actor Staff as St
boundary ReceptionDetailView as RDV
control ReceptionController as RC
entity RECEPTION as RE
entity PATIENT as P
entity APPOINTMENT as A
entity MEDICAL_RECORD as MR

title View Reception Details Sequence

St -> RDV: Select reception (receptionID)
activate St
activate RDV

RDV -> RC: Request reception details
activate RC

RC -> RE: Get reception information
activate RE
RE -> RE: Query reception by ID
activate RE
deactivate RE

break Reception not found
  RC <-- RE: Error notification
  RDV <-- RC: Error notification
  RDV -> RDV: Display reception not found
  activate RDV
  deactivate RDV
end

RC <-- RE: Reception data
deactivate RE

RC -> P: Get patient information
activate P
P -> P: Query patient by ID
activate P
deactivate P
RC <-- P: Patient data
deactivate P

RC -> A: Get appointment information
activate A
A -> A: Query appointment by ID
activate A
deactivate A
RC <-- A: Appointment data
deactivate A

RC -> MR: Get medical record (if exists)
activate MR
MR -> MR: Query medical record by reception
activate MR
deactivate MR
RC <-- MR: Medical record data
deactivate MR

RDV <-- RC: Complete reception details
deactivate RC
RDV -> RDV: Display reception details
activate RDV
deactivate RDV

deactivate RDV
deactivate St

@enduml
```

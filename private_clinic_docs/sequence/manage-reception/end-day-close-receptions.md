# Sequence End Day/Close Receptions

## Description

This sequence diagram describes closing all pending receptions at end of day.

## Diagram

<!-- diagram id="sequence-manage-reception-end-day" -->

```plantuml
@startuml
autonumber

actor Receptionist as R
boundary ReceptionListView as RLV
boundary ConfirmationDialog as CD
control ReceptionController as RC
entity RECEPTION as RE

title End Day/Close Receptions Sequence

R -> RLV: Click "End Day"
activate R
activate RLV

RLV -> RC: Request pending receptions count
activate RC

RC -> RE: Count pending receptions
activate RE
RE -> RE: Query receptions with status Waiting or In Examination
activate RE
deactivate RE
RC <-- RE: Pending count
deactivate RE

opt No pending receptions
  RLV <-- RC: No pending receptions
  deactivate RC
  RLV -> RLV: Display no pending message
  activate RLV
  deactivate RLV
end

RLV <-- RC: Pending receptions count
deactivate RC
RLV -> CD: Display confirmation dialog with count
deactivate RLV
activate CD
CD -> CD: Show pending receptions list
activate CD
deactivate CD

R -> CD: Confirm end day
deactivate R

break Receptionist cancels
  CD -> CD: Close dialog
  activate CD
  deactivate CD
end

CD -> RC: Send end day request
deactivate CD
activate RC

RC -> RE: Update all pending receptions to Cancelled
activate RE
RE -> RE: Batch update status with cancellation reason
activate RE
deactivate RE
RC <-- RE: Receptions updated
deactivate RE

RC -> RC: Send cancellation notifications to patients
activate RC
deactivate RC

activate RLV
RLV <-- RC: Success notification
deactivate RC
RLV -> RLV: Display success message
activate RLV
deactivate RLV
RLV -> RLV: Refresh reception list
activate RLV
deactivate RLV

deactivate RLV

@enduml
```

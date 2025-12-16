# Sequence View Staff Details

## Description

This sequence diagram describes viewing detailed information of a staff member.

## Diagram

<!-- diagram id="sequence-manage-staff-view-details" -->

```plantuml
@startuml
autonumber

actor Admin as A
boundary StaffDetailView as SDV
control StaffController as SC
entity STAFF as S
entity ACCOUNT as AC
entity STAFF_SCHEDULE as SS

title View Staff Details Sequence

A -> SDV: Select staff (staffID)
activate A
activate SDV

SDV -> SC: Request staff details
activate SC

SC -> S: Get staff information
activate S
S -> S: Query staff by ID
activate S
deactivate S

break Staff not found
  SC <-- S: Error notification
  SDV <-- SC: Error notification
  SDV -> SDV: Display staff not found
  activate SDV
  deactivate SDV
end

SC <-- S: Staff data
deactivate S

SC -> AC: Get account information
activate AC
AC -> AC: Query account by staff
activate AC
deactivate AC
SC <-- AC: Account data
deactivate AC

SC -> SS: Get staff schedules
activate SS
SS -> SS: Query schedules by staff
activate SS
deactivate SS
SC <-- SS: Schedule list
deactivate SS

SDV <-- SC: Complete staff details
deactivate SC
SDV -> SDV: Display staff details with tabs
activate SDV
deactivate SDV

deactivate SDV
deactivate A

@enduml
```

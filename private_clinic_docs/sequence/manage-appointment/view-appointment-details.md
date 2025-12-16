# Sequence View Appointment Details

## Description

This sequence diagram describes viewing detailed information of an appointment.

## Diagram

<!-- diagram id="sequence-manage-appointment-view-details" -->

```plantuml
@startuml
autonumber

actor Staff as St
boundary AppointmentDetailView as ADV
control AppointmentController as AC
entity APPOINTMENT as A
entity PATIENT as P
entity STAFF as D

title View Appointment Details Sequence

St -> ADV: Select appointment (appointmentID)
activate St
activate ADV

ADV -> AC: Request appointment details
activate AC

AC -> A: Get appointment details
activate A
A -> A: Query appointment by ID
activate A
deactivate A

break Appointment not found
  AC <-- A: Error notification
  ADV <-- AC: Error notification
  ADV -> ADV: Display appointment not found
  activate ADV
  deactivate ADV
end

AC <-- A: Appointment data
deactivate A

AC -> P: Get patient info
activate P
P -> P: Query patient by ID
activate P
deactivate P
AC <-- P: Patient data
deactivate P

AC -> D: Get doctor info
activate D
D -> D: Query doctor by ID
activate D
deactivate D
AC <-- D: Doctor data
deactivate D

ADV <-- AC: Complete appointment details
deactivate AC
ADV -> ADV: Display appointment details
activate ADV
deactivate ADV

deactivate ADV
deactivate St

@enduml
```

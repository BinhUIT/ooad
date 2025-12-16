# Sequence View and Filter Appointments

## Description

This sequence diagram describes viewing and filtering appointments.

## Diagram

<!-- diagram id="sequence-manage-appointment-view-and-filter" -->

```plantuml
@startuml
autonumber

actor Staff as St
boundary AppointmentListView as ALV
control AppointmentController as AC
entity APPOINTMENT as A
entity PATIENT as P
entity STAFF as D

title View and Filter Appointments Sequence

St -> ALV: Request appointment list
activate St
activate ALV

ALV -> AC: Request appointments with filters
activate AC

AC -> A: Query appointments with filters
activate A

A -> P: Get patient info
activate P
P -> P: Query patient data
activate P
deactivate P
A <-- P: Patient data
deactivate P

A -> D: Get doctor info
activate D
D -> D: Query doctor data
activate D
deactivate D
A <-- D: Doctor data
deactivate D

A -> A: Combine appointment details
activate A
deactivate A

AC <-- A: Appointment list with details
deactivate A

opt No appointments
  ALV <-- AC: Empty list
  deactivate AC
  ALV -> ALV: Display no appointments message
  activate ALV
  deactivate ALV
end

ALV <-- AC: Appointments data
deactivate AC
ALV -> ALV: Display appointment list
activate ALV
deactivate ALV

== Filter by Date/Status/Doctor ==

St -> ALV: Apply filter criteria
ALV -> AC: Send filter request
activate AC

AC -> A: Query filtered appointments
activate A
A -> A: Apply filters
activate A
deactivate A
AC <-- A: Filtered results
deactivate A

opt No results
  ALV <-- AC: Empty results
  deactivate AC
  ALV -> ALV: Display no results message
  activate ALV
  deactivate ALV
end

ALV <-- AC: Filtered data
deactivate AC
ALV -> ALV: Display filtered results
activate ALV
deactivate ALV

deactivate ALV
deactivate St

@enduml
```

U -> V: (11) Apply filters\n(date, status, doctorID)

V -> AC: (12) GET /api/appointments\n{token, date, status, doctorID}
activate AC

AC -> A: (13) Query with filters
activate A
A --> AC: (14) Filtered appointments
deactivate A

AC --> V: (15) 200 OK\n{appointments[]}
V --> U: (16) Display filtered results

deactivate AC
deactivate V

@enduml

```

```

# Sequence View and Filter Receptions

## Description

This sequence diagram describes viewing and filtering receptions.

## Diagram

<!-- diagram id="sequence-manage-reception-view-and-filter" -->

```plantuml
@startuml
autonumber

actor Receptionist as R
boundary ReceptionListView as RLV
control ReceptionController as RC
entity RECEPTION as RE
entity PATIENT as P
entity APPOINTMENT as A
entity STAFF as S

title View and Filter Receptions Sequence

R -> RLV: Request reception list
activate R
activate RLV

RLV -> RC: Request receptions with filters
activate RC

RC -> RE: Query receptions
activate RE

RE -> P: Get patient info
activate P
P -> P: Query patient data
activate P
deactivate P
RE <-- P: Patient data
deactivate P

RE -> A: Get appointment info
activate A
A -> A: Query appointment data
activate A
deactivate A
RE <-- A: Appointment data
deactivate A

RE -> S: Get doctor info
activate S
S -> S: Query staff data
activate S
deactivate S
RE <-- S: Doctor data
deactivate S

RE -> RE: Combine reception details
activate RE
deactivate RE

RC <-- RE: Reception list with details
deactivate RE

opt No receptions
  RLV <-- RC: Empty list
  deactivate RC
  RLV -> RLV: Display no receptions message
  activate RLV
  deactivate RLV
end

RLV <-- RC: Receptions data
deactivate RC
RLV -> RLV: Display receptions list
activate RLV
deactivate RLV

== Filter by Date/Status/Doctor/Patient ==

R -> RLV: Apply filter criteria
RLV -> RC: Send filter request
activate RC

RC -> RE: Query filtered receptions
activate RE
RE -> RE: Apply filters
activate RE
deactivate RE
RC <-- RE: Filtered results
deactivate RE

opt No results
  RLV <-- RC: Empty results
  deactivate RC
  RLV -> RLV: Display no results message
  activate RLV
  deactivate RLV
end

RLV <-- RC: Filtered data
deactivate RC
RLV -> RLV: Display filtered results
activate RLV
deactivate RLV

deactivate RLV
deactivate R

@enduml
```

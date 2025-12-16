# Sequence View and Filter Patients

## Description

This sequence diagram describes the view and filter patients flow for staff in the Private Clinic Management System.

## Diagram

<!-- diagram id="sequence-manage-patient-view-and-filter" -->

```plantuml
@startuml
autonumber

actor Staff as St
boundary PatientListView as PLV
control PatientController as PC
entity PATIENT as P

title View and Filter Patients Sequence

St -> PLV: Access patients list
activate St
activate PLV
PLV -> PC: Request patients list
activate PC
PC -> P: Get patients with statistics
activate P
P -> P: Query patients data
activate P
deactivate P
PC <-- P: Patients list
deactivate P

opt No patients
  PLV <-- PC: Empty list
  deactivate PC
  PLV -> PLV: Display no patients message
  activate PLV
  deactivate PLV
end

PLV <-- PC: Patients data with statistics
deactivate PC
PLV -> PLV: Display patients list
activate PLV
deactivate PLV

St -> PLV: Enter filter criteria
PLV -> PC: Send filter request
activate PC
PC -> P: Get filtered patients
activate P
P -> P: Query with filter criteria
activate P
deactivate P
PC <-- P: Filtered results
deactivate P

opt No results
  PLV <-- PC: Empty results
  deactivate PC
  PLV -> PLV: Display no results message
  activate PLV
  deactivate PLV
end

PLV <-- PC: Filtered data
deactivate PC
PLV -> PLV: Display filtered results
activate PLV
deactivate PLV

deactivate PLV
deactivate St

@enduml
```

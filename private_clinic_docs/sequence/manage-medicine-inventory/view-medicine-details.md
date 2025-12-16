# Sequence View Medicine Details

## Description

This sequence diagram describes viewing detailed information of a medicine type.

## Diagram

<!-- diagram id="sequence-manage-medicine-inventory-view-details" -->

```plantuml
@startuml
autonumber

actor Staff as St
boundary MedicineDetailView as MDV
control MedicineController as MC
entity MEDICINE as M
entity MEDICINE_INVENTORY as MI
entity MEDICINE_PRICE as MP
entity MEDICINE_IMPORT as MIM

title View Medicine Details Sequence

St -> MDV: Select medicine (medicineID)
activate St
activate MDV

MDV -> MC: Request medicine details
activate MC

MC -> M: Get medicine information
activate M
M -> M: Query medicine by ID
activate M
deactivate M

break Medicine not found
  MC <-- M: Error notification
  MDV <-- MC: Error notification
  MDV -> MDV: Display medicine not found
  activate MDV
  deactivate MDV
end

MC <-- M: Medicine data
deactivate M

MC -> MI: Get inventory batches
activate MI
MI -> MI: Query inventory by medicine with batch details
activate MI
deactivate MI
MC <-- MI: Inventory batches with expiry dates
deactivate MI

MC -> MP: Get price history
activate MP
MP -> MP: Query prices by medicine
activate MP
deactivate MP
MC <-- MP: Price history
deactivate MP

MC -> MIM: Get import history
activate MIM
MIM -> MIM: Query imports by medicine
activate MIM
deactivate MIM
MC <-- MIM: Import history
deactivate MIM

MDV <-- MC: Complete medicine details
deactivate MC
MDV -> MDV: Display medicine details with tabs
activate MDV
deactivate MDV

deactivate MDV
deactivate St

@enduml
```

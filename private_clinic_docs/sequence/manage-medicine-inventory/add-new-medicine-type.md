# Sequence Add New Medicine Type

## Description

This sequence diagram describes adding a new medicine type to the system.

## Diagram

<!-- diagram id="sequence-manage-medicine-inventory-add-new" -->

```plantuml
@startuml
autonumber

actor WarehouseStaff as WS
boundary AddMedicineView as AMV
control MedicineController as MC
entity MEDICINE as M

title Add New Medicine Type Sequence

WS -> AMV: Click "Add New Medicine"
activate WS
activate AMV

AMV -> AMV: Display medicine form
activate AMV
deactivate AMV

WS -> AMV: Enter medicine information (name, unit, usage instructions, etc.)
WS -> AMV: Click "Save"
deactivate WS

AMV -> AMV: Validate input data
activate AMV
deactivate AMV

break Invalid data
  AMV -> AMV: Display validation error
  activate AMV
  deactivate AMV
end

AMV -> MC: Send add medicine request
activate MC

MC -> M: Check if medicine exists (name)
activate M
M -> M: Query medicine by name
activate M
deactivate M

break Medicine already exists
  MC <-- M: Medicine exists error
  AMV <-- MC: Error notification
  AMV -> AMV: Display error message
  activate AMV
  deactivate AMV
end

MC <-- M: Medicine not found
deactivate M

MC -> M: Create medicine record
activate M
M -> M: Insert medicine information
activate M
deactivate M
MC <-- M: Medicine created
deactivate M

AMV <-- MC: Success notification
deactivate MC
AMV -> AMV: Display success message
activate AMV
deactivate AMV

deactivate AMV

@enduml
```

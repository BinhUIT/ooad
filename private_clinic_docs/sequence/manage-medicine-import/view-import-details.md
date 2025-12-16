# Sequence View Import Details

## Description

This sequence diagram describes viewing detailed information of an import record.

## Diagram

<!-- diagram id="sequence-manage-medicine-import-view-details" -->

```plantuml
@startuml
autonumber

actor WarehouseStaff as WS
boundary ImportDetailView as IDV
control ImportController as IC
entity MEDICINE_IMPORT as MI
entity MEDICINE_IMPORT_DETAIL as MID
entity MEDICINE as M

title View Import Details Sequence

WS -> IDV: Select import (importID)
activate WS
activate IDV

IDV -> IC: Request import details
activate IC

IC -> MI: Get import information
activate MI
MI -> MI: Query import by ID
activate MI
deactivate MI

break Import not found
  IC <-- MI: Error notification
  IDV <-- IC: Error notification
  IDV -> IDV: Display import not found
  activate IDV
  deactivate IDV
end

IC <-- MI: Import data
deactivate MI

IC -> MID: Get import details
activate MID
MID -> M: Get medicine info
activate M
M -> M: Query medicine data
activate M
deactivate M
MID <-- M: Medicine data
deactivate M

MID -> MID: Combine detail with medicine info
activate MID
deactivate MID
IC <-- MID: Import details with medicines
deactivate MID

IDV <-- IC: Complete import details
deactivate IC
IDV -> IDV: Display import details
activate IDV
deactivate IDV

deactivate IDV
deactivate WS

@enduml
```

# Sequence View Medicine Inventory

## Description

This sequence diagram describes viewing medicine inventory with stock levels.

## Diagram

<!-- diagram id="sequence-manage-medicine-inventory-view" -->

```plantuml
@startuml
autonumber

actor Staff as St
boundary MedicineInventoryView as MIV
control MedicineController as MC
entity MEDICINE as M
entity MEDICINE_INVENTORY as MI

title View Medicine Inventory Sequence

St -> MIV: Request medicine inventory list
activate St
activate MIV

MIV -> MC: Request medicines with inventory
activate MC

MC -> M: Query medicines
activate M
M -> M: Query all medicine types
activate M
deactivate M

M -> MI: Get inventory levels for each medicine
activate MI
MI -> MI: Aggregate inventory by medicine
activate MI
deactivate MI
M <-- MI: Inventory levels
deactivate MI

M -> M: Calculate total stock and expiring soon
activate M
deactivate M

MC <-- M: Medicine list with inventory
deactivate M

opt No medicines
  MIV <-- MC: Empty list
  deactivate MC
  MIV -> MIV: Display no medicines message
  activate MIV
  deactivate MIV
end

MIV <-- MC: Medicine inventory data
deactivate MC
MIV -> MIV: Display inventory list with alerts
activate MIV
deactivate MIV

== Filter by Category/Stock Status ==

St -> MIV: Apply filter criteria
MIV -> MC: Send filter request
activate MC

MC -> M: Query filtered medicines
activate M
M -> M: Apply filters
activate M
deactivate M
MC <-- M: Filtered results
deactivate M

opt No results
  MIV <-- MC: Empty results
  deactivate MC
  MIV -> MIV: Display no results message
  activate MIV
  deactivate MIV
end

MIV <-- MC: Filtered data
deactivate MC
MIV -> MIV: Display filtered results
activate MIV
deactivate MIV

deactivate MIV
deactivate St

@enduml
```

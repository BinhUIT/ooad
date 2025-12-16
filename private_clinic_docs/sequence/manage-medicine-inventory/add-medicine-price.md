# Sequence Add Medicine Price

## Description

This sequence diagram describes adding a new price for a medicine type.

## Diagram

<!-- diagram id="sequence-manage-medicine-inventory-add-price" -->

```plantuml
@startuml
autonumber

actor WarehouseStaff as WS
boundary MedicineDetailView as MDV
boundary AddPriceDialog as APD
control MedicineController as MC
entity MEDICINE_PRICE as MP

title Add Medicine Price Sequence

WS -> MDV: Click "Add Price" from medicine details
activate WS
activate MDV

MDV -> APD: Display price dialog
deactivate MDV
activate APD
APD -> APD: Show current price and price history
activate APD
deactivate APD

WS -> APD: Enter new price and effective date
WS -> APD: Click "Save"
deactivate WS

APD -> APD: Validate price data
activate APD
deactivate APD

break Invalid data
  APD -> APD: Display validation error
  activate APD
  deactivate APD
end

break Effective date conflict
  APD -> APD: Display date conflict error
  activate APD
  deactivate APD
end

APD -> MC: Send add price request
activate MC

MC -> MP: Check for overlapping price
activate MP
MP -> MP: Query prices by medicine and date range
activate MP
deactivate MP

break Price overlap exists
  MC <-- MP: Overlap error
  APD <-- MC: Error notification
  APD -> APD: Display overlap message
  activate APD
  deactivate APD
end

MC <-- MP: No overlap
deactivate MP

MC -> MP: Create price record
activate MP
MP -> MP: Insert price information
activate MP
deactivate MP
MC <-- MP: Price created
deactivate MP

APD <-- MC: Success notification
deactivate MC
APD -> APD: Display success message
activate APD
deactivate APD
APD -> MDV: Refresh medicine details
deactivate APD
activate MDV
MDV -> MDV: Reload price history
activate MDV
deactivate MDV

deactivate MDV

@enduml
```

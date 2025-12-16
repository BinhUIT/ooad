# Sequence Add Medicine to Invoice

## Description

This sequence diagram describes adding medicine to invoice (from prescription dispensing).

## Diagram

<!-- diagram id="sequence-manage-invoice-add-medicine" -->

```plantuml
@startuml
autonumber

actor PharmacyStaff as PS
boundary InvoiceDetailView as IDV
control InvoiceController as IC
entity INVOICE as I
entity INVOICE_MEDICINE_DETAIL as IMD
entity PRESCRIPTION as PR
entity MEDICINE_INVENTORY as MI

title Add Medicine to Invoice Sequence

PS -> IDV: View invoice with prescription
activate PS
activate IDV

IDV -> IC: Request prescription details
activate IC

IC -> PR: Get prescription with medicines
activate PR
PR -> PR: Query prescription details
activate PR
deactivate PR
IC <-- PR: Prescription data
deactivate PR

IDV <-- IC: Prescription medicines
deactivate IC
IDV -> IDV: Display medicines available to dispense
activate IDV
deactivate IDV

PS -> IDV: Select medicines to dispense with quantities
PS -> IDV: Click "Dispense and Add to Invoice"
deactivate PS

IDV -> IDV: Validate selected medicines
activate IDV
deactivate IDV

break Invalid selection
  IDV -> IDV: Display validation error
  activate IDV
  deactivate IDV
end

IDV -> IC: Send dispense request
activate IC

IC -> MI: Check inventory and update (FEFO)
activate MI
MI -> MI: Query and update inventory batches
activate MI
deactivate MI

break Insufficient inventory
  IC <-- MI: Insufficient inventory error
  IDV <-- IC: Error notification
  IDV -> IDV: Display insufficient inventory message
  activate IDV
  deactivate IDV
end

IC <-- MI: Inventory updated
deactivate MI

IC -> IMD: Add medicine details to invoice
activate IMD
IMD -> IMD: Insert invoice medicine details
activate IMD
deactivate IMD
IC <-- IMD: Medicine details added
deactivate IMD

IC -> I: Recalculate invoice total
activate I
I -> I: Calculate total including new medicines
activate I
deactivate I
IC <-- I: Invoice updated
deactivate I

IDV <-- IC: Success notification
deactivate IC
IDV -> IDV: Display success message
activate IDV
deactivate IDV
IDV -> IDV: Refresh invoice details
activate IDV
deactivate IDV

deactivate IDV

@enduml
```

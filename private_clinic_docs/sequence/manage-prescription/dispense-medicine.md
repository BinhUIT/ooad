# Sequence Dispense Medicine

## Description

This sequence diagram describes dispensing medicines from a prescription.

## Diagram

<!-- diagram id="sequence-manage-prescription-dispense-medicine" -->

```plantuml
@startuml
autonumber

actor PharmacyStaff as PS
boundary DispenseMedicineView as DMV
control InvoiceController as IC
entity INVOICE as INV
entity INVOICE_MEDICINE_DETAIL as IMD
entity PRESCRIPTION as PR
entity MEDICINE_INVENTORY as MI

title Dispense Medicine Sequence

PS -> DMV: Search patient by name/ID
activate PS
activate DMV

DMV -> IC: Request invoice search
activate IC

IC -> INV: Search invoices
activate INV
INV -> INV: Query invoices by patient
activate INV
deactivate INV
IC <-- INV: Invoice list
deactivate INV

DMV <-- IC: Invoice data
deactivate IC
DMV -> DMV: Display invoice list
activate DMV
deactivate DMV

PS -> DMV: Select invoice

DMV -> IC: Request invoice details
activate IC

IC -> INV: Get invoice details
activate INV
INV -> INV: Query invoice by ID
activate INV
deactivate INV
IC <-- INV: Invoice data
deactivate INV

IC -> PR: Get prescription
activate PR
PR -> PR: Query prescription with details
activate PR
deactivate PR
IC <-- PR: Prescription with details
deactivate PR

DMV <-- IC: Complete invoice and prescription
deactivate IC
DMV -> DMV: Display prescription and medicines
activate DMV
deactivate DMV

PS -> DMV: Select medicines to dispense (only available ones)
PS -> DMV: Click "Dispense"
deactivate PS

DMV -> IC: Send dispense request
activate IC

IC -> MI: Check and update inventory (FEFO applied)
activate MI
MI -> MI: Query inventory batches by expiry date
activate MI
deactivate MI

break Medicines not available
  IC <-- MI: Insufficient inventory error
  DMV <-- IC: Error notification
  DMV -> DMV: Display insufficient inventory message
  activate DMV
  deactivate DMV
end

MI -> MI: Update inventory quantities (FEFO)
activate MI
deactivate MI
IC <-- MI: Inventory updated
deactivate MI

IC -> IMD: Add medicine details to invoice
activate IMD
IMD -> IMD: Insert invoice medicine details
activate IMD
deactivate IMD
IC <-- IMD: Details added
deactivate IMD

IC -> INV: Update invoice total
activate INV
INV -> INV: Calculate and update total amount
activate INV
deactivate INV
IC <-- INV: Invoice updated
deactivate INV

DMV <-- IC: Success notification
deactivate IC
DMV -> DMV: Display success message
activate DMV
deactivate DMV
DMV -> DMV: Print prescription label
activate DMV
deactivate DMV

deactivate DMV
    IC --> V: (23) 409 Conflict
    V --> PS: (24) Display error message
end

deactivate MI
deactivate IC
deactivate V

@enduml
```

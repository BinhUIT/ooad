# Sequence View Invoice Details

## Description

This sequence diagram describes viewing detailed information of an invoice.

## Diagram

<!-- diagram id="sequence-manage-invoice-view-details" -->

```plantuml
@startuml
autonumber

actor Staff as St
boundary InvoiceDetailView as IDV
control InvoiceController as IC
entity INVOICE as I
entity INVOICE_SERVICE_DETAIL as ISD
entity INVOICE_MEDICINE_DETAIL as IMD
entity PATIENT as P
entity PAYMENT as PM

title View Invoice Details Sequence

St -> IDV: Select invoice (invoiceID)
activate St
activate IDV

IDV -> IC: Request invoice details
activate IC

IC -> I: Get invoice information
activate I
I -> I: Query invoice by ID
activate I
deactivate I

break Invoice not found
  IC <-- I: Error notification
  IDV <-- IC: Error notification
  IDV -> IDV: Display invoice not found
  activate IDV
  deactivate IDV
end

IC <-- I: Invoice data
deactivate I

IC -> P: Get patient information
activate P
P -> P: Query patient by ID
activate P
deactivate P
IC <-- P: Patient data
deactivate P

IC -> ISD: Get service details
activate ISD
ISD -> ISD: Query service details by invoice
activate ISD
deactivate ISD
IC <-- ISD: Service details list
deactivate ISD

IC -> IMD: Get medicine details
activate IMD
IMD -> IMD: Query medicine details by invoice
activate IMD
deactivate IMD
IC <-- IMD: Medicine details list
deactivate IMD

IC -> PM: Get payment history
activate PM
PM -> PM: Query payments by invoice
activate PM
deactivate PM
IC <-- PM: Payment history
deactivate PM

IDV <-- IC: Complete invoice details
deactivate IC
IDV -> IDV: Display invoice details with tabs
activate IDV
deactivate IDV

deactivate IDV
deactivate St

@enduml
```

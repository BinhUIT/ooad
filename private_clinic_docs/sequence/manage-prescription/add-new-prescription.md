# Sequence Add Prescription

## Description

This sequence diagram describes creating a prescription for a medical record.

## Diagram

<!-- diagram id="sequence-manage-prescription-add-new" -->

```plantuml
@startuml
autonumber

actor Doctor as Dr
boundary PrescriptionView as PV
control PrescriptionController as PC
entity PRESCRIPTION as PR
entity PRESCRIPTION_DETAIL as PD
entity MEDICINE as M
entity MEDICINE_INVENTORY as MI

title Add Prescription Sequence

Dr -> PV: Click "Add Prescription" for medical record
activate Dr
activate PV

PV -> PV: Display prescription form
activate PV
deactivate PV

Dr -> PV: Search medicines (query)

PV -> PC: Request medicine search
activate PC

PC -> M: Search medicines
activate M
M -> M: Query medicines by name/code
activate M
deactivate M
PC <-- M: Medicine list
deactivate M

PV <-- PC: Medicine data
deactivate PC
PV -> PV: Display medicine list
activate PV
deactivate PV

Dr -> PV: Select medicine and enter quantity, dosage
Dr -> PV: Click "Save"
deactivate Dr

PV -> PV: Validate prescription data
activate PV
deactivate PV

break Invalid data
  PV -> PV: Display validation error
  activate PV
  deactivate PV
end

PV -> PC: Send create prescription request
activate PC

PC -> MI: Check medicine inventory
activate MI
MI -> MI: Query inventory for selected medicines
activate MI
deactivate MI

opt Some medicines unavailable
  PC <-- MI: Insufficient inventory warning
  note right
    System alerts doctor
    but still allows creating
    prescription
  end note
end

PC <-- MI: Inventory status
deactivate MI

PC -> PR: Create prescription
activate PR
PR -> PR: Insert prescription record
activate PR
deactivate PR
PC <-- PR: Prescription created
deactivate PR

PC -> PD: Create prescription details
activate PD
PD -> PD: Insert prescription detail records
activate PD
deactivate PD
PC <-- PD: Details created
deactivate PD

PV <-- PC: Success notification
deactivate PC
PV -> PV: Display success message
activate PV
deactivate PV

deactivate PV

    PC --> V: (18) 201 Created\nwith warnings
    V --> D: (19) Display warning:\n"Some medicines unavailable"
end

deactivate MI
deactivate PC
deactivate V

@enduml
```

# Sequence Sign Up

## Description

This sequence diagram describes the Sign Up flow for new patients in the Private Clinic Management System.

## Diagram

<!-- diagram id="sequence-auth-sign-up" -->

```plantuml
@startuml
autonumber

actor Patient as P
boundary SignUpView as SUV
control AuthController as AC
entity ACCOUNT as A
entity PATIENT as PT

title Sign Up Sequence

P -> SUV: Enter registration info\n(name, email, phone, password)
activate P
activate SUV
SUV -> SUV: Validate input format
activate SUV
deactivate SUV

break Invalid data
  SUV -> SUV: Display error notification
  activate SUV
  deactivate SUV
end

SUV -> AC: Send sign-up request
activate AC

AC -> A: (4) Check if username exists
activate A

alt Username available
    AC <-- A: Username available
    deactivate A

    AC -> A: Create new account (hash password)
    activate A
    A -> A: Insert account record
    activate A
    deactivate A
    AC <-- A: Account created
    deactivate A

    AC -> PT: Create patient profile
    activate PT
    PT -> PT: Insert patient record
    activate PT
    deactivate PT
    AC <-- PT: Patient profile created
    deactivate PT

    SUV <-- AC: Success notification
    deactivate AC
    SUV -> SUV: Display success message
    activate SUV
    deactivate SUV
    SUV -> SUV: Send welcome email
    activate SUV
    deactivate SUV
    deactivate SUV

else Username exists
    AC <-- A: Username already exists
    deactivate A
    SUV <-- AC: Error notification
    deactivate AC
    SUV -> SUV: Display error message
    activate SUV
    deactivate SUV
    deactivate SUV
end

deactivate P
deactivate A
deactivate AC
deactivate V

@enduml
```

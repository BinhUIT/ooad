# Sequence Forgot Password

## Description

This sequence diagram describes the Forgot Password flow for users in the Private Clinic Management System.

## Diagram

<!-- diagram id="sequence-auth-forgot-password" -->

```plantuml
@startuml
autonumber

actor User as U
boundary ForgotPasswordView as FPV
control AuthController as AC
entity ACCOUNT as A
boundary EmailService as ES

title Forgot Password Sequence

U -> FPV: Enter email
activate U
activate FPV

FPV -> AC: Send forgot password request
activate AC

AC -> A: (3) Find account by email
activate A

alt Account found
    AC <-- A: Account found
    deactivate A

    AC -> AC: Generate reset token
    activate AC
    deactivate AC

    AC -> ES: Send reset email
    activate ES
    ES -> ES: Send email with reset link
    activate ES
    deactivate ES
    AC <-- ES: Email sent
    deactivate ES

    FPV <-- AC: Success notification
    deactivate AC
    FPV -> FPV: Display success message
    activate FPV
    deactivate FPV
    deactivate FPV

else Account not found
    AC <-- A: Account not found
    deactivate A
    note right
        Return success for security
    end note
    FPV <-- AC: Success notification
    deactivate AC
    FPV -> FPV: Display success message
    activate FPV
    deactivate FPV
    deactivate FPV

deactivate A
deactivate AC
deactivate V

U -> FPV: Click reset link from email
activate FPV

FPV -> AC: Send reset password request
activate AC

AC -> A: (12) Validate token
activate A

alt Token valid
    AC <-- A: Token valid
    deactivate A

    AC -> AC: Hash new password
    activate AC
    deactivate AC

    AC -> A: Update password
    activate A
    A -> A: Update password field
    activate A
    deactivate A
    AC <-- A: Password updated
    deactivate A

    FPV <-- AC: Success notification
    deactivate AC
    FPV -> FPV: Display success message
    activate FPV
    deactivate FPV
    deactivate FPV

else Token invalid/expired
    AC <-- A: Token invalid
    deactivate A
    FPV <-- AC: Error notification
    deactivate AC
    FPV -> FPV: Display error message
    activate FPV
    deactivate FPV
    deactivate FPV
end

deactivate U

@enduml
```

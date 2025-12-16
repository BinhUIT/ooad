# Sequence Manage Profile

## Description

This sequence diagram describes the Profile Management flow for users in the Private Clinic Management System.

## Diagram

<!-- diagram id="sequence-auth-manage-profile" -->

```plantuml
@startuml
autonumber

actor User as U
boundary ProfileView as PV
control AuthController as AC
entity ACCOUNT as A
entity USER_PROFILE as UP

title Manage Profile Sequence

== View Profile ==

U -> PV: Request view profile
activate U
activate PV

PV -> AC: Request profile information
activate AC

AC -> A: Validate token and get user
activate A
A -> A: Query account by token
activate A
deactivate A

break Invalid token
  AC <-- A: Error notification
  PV <-- AC: Error notification
  PV -> PV: Display authentication error
  activate PV
  deactivate PV
end

AC <-- A: User data
deactivate A

AC -> UP: Get profile details
activate UP
UP -> UP: Query profile by user ID
activate UP
deactivate UP
AC <-- UP: Profile data
deactivate UP

PV <-- AC: Complete profile information
deactivate AC
PV -> PV: Display profile information
activate PV
deactivate PV

== Update Profile ==

U -> PV: Update profile info (phone, address, email)
PV -> PV: Validate input
activate PV
deactivate PV

break Invalid input
  PV -> PV: Display validation error
  activate PV
  deactivate PV
end

PV -> AC: Send update request
activate AC

AC -> A: Validate token
activate A
A -> A: Verify token
activate A
deactivate A

break Invalid token
  AC <-- A: Error notification
  PV <-- AC: Error notification
  PV -> PV: Display authentication error
  activate PV
  deactivate PV
end

AC <-- A: Token valid
deactivate A

AC -> UP: Update profile
activate UP
UP -> UP: Update profile information
activate UP
deactivate UP
AC <-- UP: Profile updated
deactivate UP

PV <-- AC: Success notification
deactivate AC
PV -> PV: Display success message
activate PV
deactivate PV

== Change Password ==

U -> PV: Change password (old_password, new_password)
deactivate U
PV -> PV: Validate input
activate PV
deactivate PV

break Invalid input
  PV -> PV: Display validation error
  activate PV
  deactivate PV
end

PV -> AC: Send change password request
activate AC

AC -> A: Validate old password
activate A
A -> A: Verify old password hash
activate A
deactivate A

break Old password incorrect
  AC <-- A: Password invalid
  PV <-- AC: Error notification
  PV -> PV: Display incorrect password error
  activate PV
  deactivate PV
end

AC <-- A: Password valid

AC -> A: Update password
activate A
A -> A: Hash new password and update
activate A
deactivate A
AC <-- A: Password updated
deactivate A
deactivate A

PV <-- AC: Success notification
deactivate AC
PV -> PV: Display success message
activate PV
deactivate PV

deactivate PV

@enduml
```

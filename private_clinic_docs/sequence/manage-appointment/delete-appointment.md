# Sequence Delete Appointment

## Description

This sequence diagram describes canceling/deleting an appointment.

## Diagram

<!-- diagram id="sequence-manage-appointment-delete" -->

```plantuml
@startuml
autonumber

actor Staff as St
boundary AppointmentListView as ALV
control AppointmentController as AC
entity APPOINTMENT as A

title Delete/Cancel Appointment Sequence

St -> ALV: Select appointment to cancel (appointmentID)
activate St
activate ALV

ALV -> ALV: Display confirmation dialog
activate ALV
deactivate ALV

St -> ALV: Confirm cancellation
deactivate St

break Staff cancels
  ALV -> ALV: Close confirmation dialog
  activate ALV
  deactivate ALV
end

ALV -> AC: Send delete request
activate AC

AC -> A: Get appointment details
activate A
A -> A: Query appointment by ID
activate A
deactivate A

break Appointment not found
  AC <-- A: Appointment not found
  ALV <-- AC: Error notification
  ALV -> ALV: Display not found message
  activate ALV
  deactivate ALV
end

AC <-- A: Appointment found

break Appointment already completed/in-progress
  AC <-- A: Cannot cancel error
  ALV <-- AC: Error notification
  ALV -> ALV: Display cannot cancel message
  activate ALV
  deactivate ALV
end

AC -> A: Update status to "Cancelled" or delete appointment
activate A
A -> A: Update appointment status
activate A
deactivate A
AC <-- A: Appointment canceled
deactivate A
deactivate A

ALV <-- AC: Success notification
deactivate AC
ALV -> ALV: Display success message
activate ALV
deactivate ALV
ALV -> ALV: Refresh appointment list
activate ALV
deactivate ALV

deactivate ALV

@enduml
```

    AC --> V: (9) 409 Conflict
    V --> U: (10) Display error message

end

deactivate A
deactivate AC
deactivate V

@enduml

```

```

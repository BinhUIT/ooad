# Sequence Book Appointment Online

## Description

Bệnh nhân tự đặt lịch hẹn khám qua portal với kiểm tra slot và giới hạn.

## Diagram

<!-- diagram id="sequence-patient-portal-book-appointment" -->

```plantuml
@startuml
autonumber

actor Patient as P
boundary BookingView as BV
control AppointmentController as AC
entity STAFF_SCHEDULE as SS
entity APPOINTMENT as AP
entity SYSTEM_PARAMETERS as SP

title Book Appointment Online Sequence

P -> BV: Access booking page
activate P
activate BV
BV -> AC: Request available schedules
activate AC
AC -> SS: Query staff schedules
activate SS
AC <-- SS: Available schedules
deactivate SS
BV <-- AC: Schedules data
deactivate AC
BV -> BV: Display doctor schedules\n(calendar view)

P -> BV: Select doctor, date, time
P -> BV: Enter reason for visit
BV -> AC: Send booking request
activate AC
AC -> AC: Validate date >= today
break Invalid date
  BV <-- AC: Date error
  BV -> BV: Display error
end

AC -> SS: Check slot availability
activate SS
break Slot not available
  AC <-- SS: No slot
  BV <-- AC: Slot error
  BV -> BV: Display error
end
AC <-- SS: Slot available
deactivate SS

AC -> SP: Get MAX_PATIENTS_PER_DAY
activate SP
AC <-- SP: Max limit
deactivate SP

AC -> AP: Count appointments for date
activate AP
break Limit exceeded
  AC <-- AP: Count >= Max
  BV <-- AC: Limit error
  BV -> BV: Display error
end
AC <-- AP: Count OK
deactivate AP

AC -> AP: Create appointment
activate AP
AC <-- AP: Appointment created
deactivate AP
BV <-- AC: Success
deactivate AC
BV -> BV: Display confirmation
deactivate BV
deactivate P

@enduml
```

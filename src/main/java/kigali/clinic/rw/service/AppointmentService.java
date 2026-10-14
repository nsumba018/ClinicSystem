package kigali.clinic.rw.service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    String saveAppointment(Appointment appointment);
    List<Appointment> getAppointmentsByStatus(AppointmentStatus status);
    List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end);

    List<Object[]> countAppointmentsByStatus();

    String cancelDayOfDoctor(Long doctorId, LocalDate date);

    Page<Appointment> getAppointmentsPage(Pageable pageable);
    String deleteCancelledBefore(LocalDate date);
    String updateAppointmentStatus(Long id, AppointmentStatus status);
}

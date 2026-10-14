package kigali.clinic.rw.service.impl;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public String saveAppointment(Appointment appointment) {
        // B1
        if(appointment.getAppointmentDate().isBefore(LocalDate.now())){
            return "Appointment date cannot be in the past";
        }
        boolean alreadyBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndStatusNot(
                appointment.getDoctor().getId(), appointment.getAppointmentDate(), AppointmentStatus.CANCELLED);
        if(alreadyBooked){
            return "Doctor is already booked on that date";
        }
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
        return "Appointment Saved Successfully";
    }

    @Override
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatusOrderByAppointmentDateAsc(status);
    }

    @Override
    public List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end) {
        return appointmentRepository.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    @Override
    public List<Object[]> countAppointmentsByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }

    @Override
    @Transactional
    public String cancelDayOfDoctor(Long doctorId, LocalDate date) {
        int howMany = appointmentRepository.cancelDayOfDoctor(doctorId, date);
        return howMany + " appointments cancelled";
    }

    @Override
    public Page<Appointment> getAppointmentsPage(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public String deleteCancelledBefore(LocalDate date) {
        int howMany = appointmentRepository.deleteCancelledBefore(date);
        return howMany + " appointments deleted";
    }
}

package kigali.clinic.rw.service.impl;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;
import kigali.clinic.rw.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

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

    @Override
    public String updateAppointmentStatus(Long id, AppointmentStatus status) {
        // B2
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if(appointment.isEmpty()){
            return "The appointment with that id does not exist";
        }
        Appointment foundAppointment = appointment.get();
        AppointmentStatus oldStatus = foundAppointment.getStatus();
        if(oldStatus == AppointmentStatus.SCHEDULED &&
                (status == AppointmentStatus.CONFIRMED || status == AppointmentStatus.CANCELLED)){
            foundAppointment.setStatus(status);
            appointmentRepository.save(foundAppointment);
            return "Appointment status updated successfully";
        }else if(oldStatus == AppointmentStatus.CONFIRMED &&
                (status == AppointmentStatus.COMPLETED || status == AppointmentStatus.CANCELLED)){
            foundAppointment.setStatus(status);
            appointmentRepository.save(foundAppointment);
            return "Appointment status updated successfully";
        }else{
            return "Cannot change status from " + oldStatus + " to " + status;
        }
    }

    @Override
    public String rescheduleAppointment(Long id, LocalDate newDate) {
        // B3
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if(appointment.isEmpty()){
            return "The appointment with that id does not exist";
        }
        Appointment foundAppointment = appointment.get();
        if(foundAppointment.getStatus() == AppointmentStatus.COMPLETED){
            return "A COMPLETED appointment cannot be rescheduled";
        }else if(foundAppointment.getStatus() == AppointmentStatus.CANCELLED){
            return "A CANCELLED appointment cannot be rescheduled";
        }else if(newDate.isBefore(LocalDate.now())){
            return "Appointment date cannot be in the past";
        }
        boolean alreadyBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndStatusNotAndIdNot(
                foundAppointment.getDoctor().getId(), newDate, AppointmentStatus.CANCELLED, id);
        if(alreadyBooked){
            return "Doctor is already booked on that date";
        }
        foundAppointment.setAppointmentDate(newDate);
        foundAppointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(foundAppointment);
        return "Appointment rescheduled successfully";
    }

    @Override
    public String deleteAppointment(Long id) {
        // C4
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if(appointment.isEmpty()){
            return "The appointment with that id does not exist";
        }else if(prescriptionRepository.existsByAppointmentId(id)){
            return "That appointment has prescriptions, delete them first";
        }else{
            appointmentRepository.deleteById(id);
            return "Appointment deleted successfully";
        }
    }
}

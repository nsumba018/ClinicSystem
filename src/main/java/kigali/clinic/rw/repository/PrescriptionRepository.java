package kigali.clinic.rw.repository;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    // C2
    List<Prescription> findByAppointment(Appointment appointment);

    // C3
    @Query("select p from Prescription p where p.appointment.patient.ssNumber = ?1 order by p.issuedDate desc")
    List<Prescription> findPrescriptionsOfPatient(String ssNumber);

    boolean existsByAppointmentId(Long appointmentId);
}

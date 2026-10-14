package kigali.clinic.rw.repository;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // A2
    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    // A3
    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(LocalDate start, LocalDate end);

    // A4
    boolean existsByDoctorIdAndAppointmentDateAndStatusNot(Long doctorId, LocalDate appointmentDate, AppointmentStatus status);
    boolean existsByDoctorIdAndAppointmentDateAndStatusNotAndIdNot(Long doctorId, LocalDate appointmentDate, AppointmentStatus status, Long id);

    // C1
    @Query("select a.status, count(a) from Appointment a group by a.status")
    List<Object[]> countAppointmentsByStatus();

    // C4
    @Transactional
    @Modifying
    @Query("""
            update Appointment a set a.status = kigali.clinic.rw.domain.AppointmentStatus.CANCELLED
            where a.doctor.id = ?1 and a.appointmentDate = ?2
            and a.status <> kigali.clinic.rw.domain.AppointmentStatus.COMPLETED""")
    int cancelDayOfDoctor(Long doctorId, LocalDate date);

    // BN2
    @Transactional
    @Modifying
    @Query("""
            delete from Appointment a
            where a.status = kigali.clinic.rw.domain.AppointmentStatus.CANCELLED
            and a.appointmentDate < ?1""")
    int deleteCancelledBefore(LocalDate date);

    // D1
    List<Appointment> findByDoctorIdAndAppointmentDateAndStatusNot(Long doctorId, LocalDate date, AppointmentStatus status);

    // D2
    List<Appointment> findByPatientSsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
            String ssNumber, LocalDate date, List<AppointmentStatus> statuses);

    long countByAppointmentDate(LocalDate date);
    long countByStatus(AppointmentStatus status);
}

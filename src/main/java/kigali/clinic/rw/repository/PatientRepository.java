package kigali.clinic.rw.repository;

import kigali.clinic.rw.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Transactional
    @Modifying
    @Query("""
            update Patient p set p.firstName = ?1, p.lastName = ?2, p.dateOfBirth = ?3, p.ssNumber = ?4
            where p.id = ?5""")
    int updatePatientById(String firstName, String lastName, Date dateOfBirth, String ssNumber, Long id);

    // A1
    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // B4
    @Query("select distinct a.patient from Appointment a where a.doctor.id = ?1")
    List<Patient> findPatientsOfDoctor(Long doctorId);

    // C2
    @Query("""
            select p from Appointment a join a.patient p
            group by p
            having count(a) >= ?1
            order by count(a) desc""")
    List<Patient> findFrequentPatients(long min);
}

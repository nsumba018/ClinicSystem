package kigali.clinic.rw.repository;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, Date dateOfBirth);
    boolean existsByOffice(Office office);



    @Transactional
    @Modifying
    @Query("""
            update Doctor d set d.firstName = ?1, d.lastName = ?2, d.dateOfBirth = ?3, d.office = ?5
            where d.id = ?4""")
    int updateDoctorById(String firstName, String lastName, Date dateOfBirth,
                         Long id, Office office);

    @Query("select d from Doctor d join d.specializations s where lower(s.name) = lower(?1)")
    List<Doctor> findDoctorsBySpecialization(String name);

    @Query("select d from Doctor d where d.office is null order by d.lastName")
    List<Doctor> findDoctorsWithoutOffice();


}

package kigali.clinic.rw.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;

@Repository
public interface OfficeRepository extends JpaRepository<Office,Long> {

    List<Office> findByName(String name);
    Optional<Office> findByOfficeNumber(int officeN);

    @Transactional
    @Modifying
    @Query("update Office o set o.name = ?1, o.officeNumber = ?2 where o.id = ?3")
    int updateOfficeById(String name, int officeNumber, @NonNull Long id);

    // C3
    @Query("""
            select o.name, o.officeNumber, count(a)
            from Appointment a join a.doctor d join d.office o
            group by o.id, o.name, o.officeNumber
            order by count(a) desc""")
    List<Object[]> findOfficesByAppointmentCount(Pageable pageable);
}

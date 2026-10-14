package kigali.clinic.rw.service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
    String saveDoctor(Doctor doctor);
    List<Doctor> getDoctors();
    String deleteDoctor(Long id);
    String updateDoctor(Doctor doctor);

    List<Doctor> getDoctorsBySpecialization(String name);

    List<Doctor> getDoctorsWithoutOffice();
    String updateDoctorSpecialization(Long doctorId, Long specializationId);
    boolean doctorExists(Long id);
    String removeDoctorSpecialization(Long doctorId, Long specializationId);
    String changeDoctorOffice(Long doctorId, int officeNumber);
    List<Appointment> getDoctorAgenda(Long doctorId, LocalDate date);
    List<Doctor> searchDoctors(String name);
    List<Object[]> getDoctorWorkload(LocalDate start, LocalDate end);
}

package kigali.clinic.rw.service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;

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
}

package kigali.clinic.rw.service;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.domain.Appointment;

import java.util.List;

public interface PatientService {
    String savePatient(Patient patient);
    List<Patient> getPatients();
    String deletePatient(Long id);
    String updatePatient(Patient patient);

    List<Patient> getPatientsByLastName(String lastName);
    List<Patient> getPatientsOfDoctor(Long doctorId);
    List<Patient> getFrequentPatients(long min);
    List<Appointment> getUpcomingAppointments(String ssNumber);

}

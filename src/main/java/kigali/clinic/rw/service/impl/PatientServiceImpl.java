package kigali.clinic.rw.service.impl;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.PatientRepository;
import kigali.clinic.rw.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public String savePatient(Patient patient) {
        patientRepository.save(patient);
        return "Patient saved successfully";
    }

    @Override
    public List<Patient> getPatients() {
        List<Patient> allPatients = patientRepository.findAll();
        return allPatients;
    }

    @Override
    public String deletePatient(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isEmpty()){
            return "Patient not found";
        }else{
            patientRepository.deleteById(id);
            return "Patient deleted successfully";
        }

    }

    @Override
    public String updatePatient(Patient patient) {
        patientRepository.updatePatientById(patient.getFirstName(),patient.getLastName(),
                patient.getDateOfBirth(),patient.getSsNumber(), patient.getId());
        return "Patient updated successfully";
    }

    @Override
    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    @Override
    public List<Patient> getPatientsOfDoctor(Long doctorId) {
        return patientRepository.findPatientsOfDoctor(doctorId);
    }

    @Override
    public List<Patient> getFrequentPatients(long min) {
        return patientRepository.findFrequentPatients(min);
    }
}

package kigali.clinic.rw.service.impl;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.SpecializationRepository;
import kigali.clinic.rw.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Override
    public String saveDoctor(Doctor doctor) {
        boolean doctorExists = doctorRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                doctor.getFirstName(), doctor.getLastName(), doctor.getDateOfBirth());
        if(doctorExists){
            return "Doctor Already Exists";
        }
        doctorRepository.save(doctor);
        return "Doctor saved successfully";
    }

    @Override
    public List<Doctor> getDoctors() {
        List<Doctor> allDoctors = doctorRepository.findAll();
        return allDoctors;
    }

    @Override
    @Transactional
    public String deleteDoctor(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if(doctor.isEmpty()){
            return "Doctor not found";
        }else{
            Doctor foundDoctor = doctor.get();
            if(foundDoctor.getOffice() != null){
                foundDoctor.getOffice().setDoctor(null);
                foundDoctor.setOffice(null);
            }
            doctorRepository.delete(foundDoctor);
            return "Doctor deleted successfully";
        }

    }

    @Override
    public String updateDoctor(Doctor doctor) {

        doctorRepository.updateDoctorById(doctor.getFirstName(),doctor.getLastName(),
                doctor.getDateOfBirth(), doctor.getId(), doctor.getOffice());
        return "Doctor updated successfully";
    }

    @Override
    public List<Doctor> getDoctorsBySpecialization(String name) {
        return doctorRepository.findDoctorsBySpecialization(name);
    }

    @Override
    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepository.findDoctorsWithoutOffice();
    }

    @Override
    @Transactional
    public String updateDoctorSpecialization(Long doctorId, Long specializationId) {
        Optional<Doctor> findDoctor = doctorRepository.findById(doctorId);
        Optional<Specialization> specialization = specializationRepository.findById(specializationId);
        if (findDoctor.isEmpty()){
            return "The doctor with that id does not exist";
        }else if(specialization.isEmpty()){
            return "That specialization does not exist";
        }else{
            Doctor doctor = findDoctor.get();
            if(doctor.getSpecializations().contains(specialization.get())){
                return "The doctor already has that specialization";
            }
            doctor.getSpecializations().add(specialization.get());
            doctorRepository.save(doctor);
            return "Specialization added to the doctor";
        }
    }

    @Override
    public boolean doctorExists(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return doctor.isPresent();
    }
}

package kigali.clinic.rw.service.impl;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.SpecializationRepository;
import kigali.clinic.rw.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

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
    @Transactional
    public String removeDoctorSpecialization(Long doctorId, Long specializationId) {
        Optional<Doctor> findDoctor = doctorRepository.findById(doctorId);
        Optional<Specialization> specialization = specializationRepository.findById(specializationId);
        if (findDoctor.isEmpty()){
            return "The doctor with that id does not exist";
        }else if(specialization.isEmpty()){
            return "That specialization does not exist";
        }else{
            Doctor doctor = findDoctor.get();
            if(!doctor.getSpecializations().contains(specialization.get())){
                return "The doctor does not have that specialization";
            }
            doctor.getSpecializations().remove(specialization.get());
            doctorRepository.save(doctor);
            return "Specialization removed from the doctor";
        }
    }

    @Override
    public String changeDoctorOffice(Long doctorId, int officeNumber) {
        Optional<Doctor> findDoctor = doctorRepository.findById(doctorId);
        Optional<Office> office = officeRepository.findByOfficeNumber(officeNumber);
        if(findDoctor.isEmpty()){
            return "The doctor with that id does not exist";
        }else if(office.isEmpty()){
            return "The office with that office number does not exist";
        }else if(doctorRepository.existsByOffice(office.get())){
            Doctor doctor = findDoctor.get();
            if(doctor.getOffice() == null || !doctor.getOffice().getId().equals(office.get().getId())){
                return "That office is taken by other doctor";
            }
        }
        Doctor doctor = findDoctor.get();
        doctor.setOffice(office.get());
        doctorRepository.save(doctor);
        return "Office changed successfully";
    }

    @Override
    public List<Appointment> getDoctorAgenda(Long doctorId, LocalDate date) {
        // D1
        return appointmentRepository.findByDoctorIdAndAppointmentDateAndStatusNot(doctorId, date, AppointmentStatus.CANCELLED);
    }

    @Override
    public List<Doctor> searchDoctors(String name) {
        // D3
        return doctorRepository.searchDoctorsByName(name);
    }


    @Override
    public boolean doctorExists(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return doctor.isPresent();
    }
}

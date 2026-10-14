package kigali.clinic.rw.service.impl;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;
import kigali.clinic.rw.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public String savePrescription(Prescription prescription, Long appointmentId) {
        // C1
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(appointment.isEmpty()){
            return "The appointment with that id does not exist";
        }else if(appointment.get().getStatus() != AppointmentStatus.COMPLETED){
            return "Prescriptions are only for COMPLETED appointments";
        }else{
            prescription.setIssuedDate(LocalDate.now());
            prescription.setAppointment(appointment.get());
            prescriptionRepository.save(prescription);
            return "Prescription saved successfully";
        }
    }

    @Override
    public List<Prescription> getPrescriptionsOfAppointment(Long appointmentId) {
        // C2
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(appointment.isEmpty()){
            return null;
        }
        return prescriptionRepository.findByAppointment(appointment.get());
    }

    @Override
    public List<Prescription> getPrescriptionsOfPatient(String ssNumber) {
        // C3
        return prescriptionRepository.findPrescriptionsOfPatient(ssNumber);
    }

    @Override
    public String updatePrescription(Prescription prescription, Long id) {
        // C4
        Optional<Prescription> checkPrescription = prescriptionRepository.findById(id);
        if(checkPrescription.isEmpty()){
            return "The prescription with that id does not exist";
        }else{
            Prescription foundPrescription = checkPrescription.get();
            foundPrescription.setMedicineName(prescription.getMedicineName());
            foundPrescription.setDosage(prescription.getDosage());
            foundPrescription.setDurationDays(prescription.getDurationDays());
            prescriptionRepository.save(foundPrescription);
            return "Prescription updated successfully";
        }
    }

    @Override
    public String deletePrescription(Long id) {
        // C4
        Optional<Prescription> prescription = prescriptionRepository.findById(id);
        if(prescription.isEmpty()){
            return "The prescription with that id does not exist";
        }else{
            prescriptionRepository.deleteById(id);
            return "Prescription deleted successfully";
        }
    }

    @Override
    public boolean appointmentHasPrescriptions(Long appointmentId) {
        return prescriptionRepository.existsByAppointmentId(appointmentId);
    }

    @Override
    public List<Object[]> getTopMedicines() {
        // Bonus 1
        return prescriptionRepository.findTopMedicines();
    }
}

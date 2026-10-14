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
}

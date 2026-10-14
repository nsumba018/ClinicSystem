package kigali.clinic.rw.service;

import kigali.clinic.rw.domain.Prescription;

import java.util.List;

public interface PrescriptionService {
    String savePrescription(Prescription prescription, Long appointmentId);
    List<Prescription> getPrescriptionsOfAppointment(Long appointmentId);
    List<Prescription> getPrescriptionsOfPatient(String ssNumber);
    String updatePrescription(Prescription prescription, Long id);
    String deletePrescription(Long id);
    boolean appointmentHasPrescriptions(Long appointmentId);
    List<Object[]> getTopMedicines();
}

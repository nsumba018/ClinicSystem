package kigali.clinic.rw.service;

import kigali.clinic.rw.domain.Prescription;

public interface PrescriptionService {
    String savePrescription(Prescription prescription, Long appointmentId);
}

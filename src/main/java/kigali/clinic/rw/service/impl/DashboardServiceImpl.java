package kigali.clinic.rw.service.impl;

import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.PatientRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;
import kigali.clinic.rw.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Override
    public Map<String, Long> getSummary() {
        // D4
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("patients", patientRepository.count());
        summary.put("doctors", doctorRepository.count());
        summary.put("freeOffices", (long) officeRepository.findFreeOffices().size());
        summary.put("appointmentsToday", appointmentRepository.countByAppointmentDate(LocalDate.now()));
        summary.put("scheduled", appointmentRepository.countByStatus(AppointmentStatus.SCHEDULED));
        summary.put("prescriptions", prescriptionRepository.count());
        return summary;
    }
}

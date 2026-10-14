package kigali.clinic.rw.controller;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.service.DoctorService;
import kigali.clinic.rw.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    @PostMapping("/save")
    public ResponseEntity<String> savePatient(@RequestBody Patient patient){
        String returnedMsg = patientService.savePatient(patient);

        if(returnedMsg.equals("Patient saved successfully")){
            return ResponseEntity.status(HttpStatus.CREATED).body(returnedMsg);
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient already exists");
        }
    }

    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients(){
        List<Patient> listOfAllPatients = patientService.getPatients();
        return ResponseEntity.status(HttpStatus.OK).body(listOfAllPatients);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id){
        String msg = patientService.deletePatient(id);
        if (msg.equals("Patient not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id " + id + " does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(msg);

    }
    @PutMapping
    public ResponseEntity<String> updatePatient(@RequestBody Patient patient){
        String returnedMsg = patientService.updatePatient(patient);
        return  ResponseEntity.status(HttpStatus.OK).body("Patient updated successfully");
    }

    @GetMapping("/by-last-name")
    public ResponseEntity<?> getPatientsByLastName(@RequestParam String lastName){
        List<Patient> patients = patientService.getPatientsByLastName(lastName);
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    // B4
    @GetMapping("/of-doctor/{doctorId}")
    public ResponseEntity<?> getPatientsOfDoctor(@PathVariable Long doctorId){
        if(!doctorService.doctorExists(doctorId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The doctor with that id does not exist");
        }
        List<Patient> patients = patientService.getPatientsOfDoctor(doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    // C2
    @GetMapping("/frequent")
    public ResponseEntity<?> getFrequentPatients(@RequestParam long min){
        List<Patient> patients = patientService.getFrequentPatients(min);
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    // D2
    @GetMapping("/{ssNumber}/upcoming")
    public ResponseEntity<?> getUpcomingAppointments(@PathVariable String ssNumber){
        List<Appointment> appointments = patientService.getUpcomingAppointments(ssNumber);
        if(appointments == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("That patient with that ssnumber does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(appointments);
    }
}

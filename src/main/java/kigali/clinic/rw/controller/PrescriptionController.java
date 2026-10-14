package kigali.clinic.rw.controller;

import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    // C1
    @PostMapping("/save")
    public ResponseEntity<String> savePrescription(@RequestBody Prescription prescription, @RequestParam Long appointmentId){
        String returnedMsg = prescriptionService.savePrescription(prescription, appointmentId);
        if(returnedMsg.equals("The appointment with that id does not exist")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnedMsg);
        }else if(returnedMsg.equals("Prescriptions are only for COMPLETED appointments")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnedMsg);
        }else{
            return ResponseEntity.status(HttpStatus.CREATED).body(returnedMsg);
        }
    }

    // C2
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionsOfAppointment(@PathVariable Long appointmentId){
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsOfAppointment(appointmentId);
        if(prescriptions == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The appointment with that id does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(prescriptions);
    }

    // C3
    @GetMapping("/patient/{ssNumber}")
    public ResponseEntity<?> getPrescriptionsOfPatient(@PathVariable String ssNumber){
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsOfPatient(ssNumber);
        return ResponseEntity.status(HttpStatus.OK).body(prescriptions);
    }

    // C4
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePrescription(@PathVariable Long id, @RequestBody Prescription prescription){
        String returnedMsg = prescriptionService.updatePrescription(prescription, id);
        if(returnedMsg.equals("The prescription with that id does not exist")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnedMsg);
        }
        return ResponseEntity.status(HttpStatus.OK).body(returnedMsg);
    }

    // C4
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable Long id){
        String returnedMsg = prescriptionService.deletePrescription(id);
        if(returnedMsg.equals("The prescription with that id does not exist")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnedMsg);
        }
        return ResponseEntity.status(HttpStatus.OK).body(returnedMsg);
    }

    // Bonus 1
    @GetMapping("/stats/top-medicines")
    public ResponseEntity<?> getTopMedicines(){
        List<Object[]> medicines = prescriptionService.getTopMedicines();
        return ResponseEntity.status(HttpStatus.OK).body(medicines);
    }
}

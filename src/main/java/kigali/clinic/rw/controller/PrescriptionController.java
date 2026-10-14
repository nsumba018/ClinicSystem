package kigali.clinic.rw.controller;

import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

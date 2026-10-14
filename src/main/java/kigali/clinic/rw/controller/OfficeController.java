package kigali.clinic.rw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;


@RestController
@RequestMapping(value="/api/offices")
public class OfficeController {

    @Autowired
    private OfficeService offServe;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveOffice(@RequestBody Office office){

       String returnedMessage =  offServe.saveOffice(office);

       if(returnedMessage.equals("Office is saved successfully")){
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
       }else{
        return new ResponseEntity<>("That office with that number already exist", HttpStatus.CONFLICT);
       }

    }

    @GetMapping(value = "/offices")
    public ResponseEntity<?> getOffice(){
        List<Office> listOfOffices = offServe.getOffices();
        return ResponseEntity.status(HttpStatus.OK).body(listOfOffices);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteOffice(@PathVariable Long id){
        String deleteResponse = offServe.deleteOffice(id);
        return ResponseEntity.status(HttpStatus.OK).body("Office is deleted successfully");
    }

    @PutMapping
    public ResponseEntity<String> updateOffice(@RequestBody Office office){
        offServe.updateOffice(office);
        return new ResponseEntity<>("Office has been updated successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/busiest")
    public ResponseEntity<?> getBusiestOffice(){
        List<Object[]> offices = offServe.getOfficesByAppointmentCount();
        if(offices.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("No appointments yet");
        }
        return ResponseEntity.status(HttpStatus.OK).body(offices.get(0));
    }

    // A3
    @GetMapping(value = "/free")
    public ResponseEntity<?> getFreeOffices(){
        List<Office> offices = offServe.getFreeOffices();
        return ResponseEntity.status(HttpStatus.OK).body(offices);
    }
}

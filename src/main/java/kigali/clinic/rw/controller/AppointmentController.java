package kigali.clinic.rw.controller;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointeService;

    @PostMapping("/save")
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment) {
        String returnedMsg = appointeService.saveAppointment(appointment);
        if(returnedMsg.equals("Appointment Saved Successfully")) {
            return new ResponseEntity<>("Appointment Saved Successfully", HttpStatus.CREATED);
        }else if(returnedMsg.equals("Appointment date cannot be in the past")){
            return new ResponseEntity<>(returnedMsg, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(returnedMsg, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/by-status")
    public ResponseEntity<?> getAppointmentsByStatus(@RequestParam AppointmentStatus status){
        List<Appointment> appointments = appointeService.getAppointmentsByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(appointments);
    }

    @GetMapping("/between")
    public ResponseEntity<?> getAppointmentsBetween(@RequestParam String start, @RequestParam String end){
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<Appointment> appointments = appointeService.getAppointmentsBetween(startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(appointments);
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<?> countAppointmentsByStatus(){
        List<Object[]> stats = appointeService.countAppointmentsByStatus();
        return ResponseEntity.status(HttpStatus.OK).body(stats);
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDayOfDoctor(@RequestParam Long doctorId, @RequestParam String date){
        LocalDate theDate = LocalDate.parse(date);
        String returnedMsg = appointeService.cancelDayOfDoctor(doctorId, theDate);
        return ResponseEntity.status(HttpStatus.OK).body(returnedMsg);
    }

    @GetMapping("/page")
    public ResponseEntity<?> getAppointmentsPage(@RequestParam int page, @RequestParam int size,
                                                 @RequestParam String sort){
        String[] pieces = sort.split(",");
        Pageable pageable;
        if(pieces[1].equals("desc")){
            pageable = PageRequest.of(page, size, Sort.by(pieces[0]).descending());
        }else{
            pageable = PageRequest.of(page, size, Sort.by(pieces[0]).ascending());
        }
        Page<Appointment> appointmentsPage = appointeService.getAppointmentsPage(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentsPage);
    }
    
    @DeleteMapping("/cancelled-before")
    public ResponseEntity<String> deleteCancelledBefore(@RequestParam String date){
        LocalDate theDate = LocalDate.parse(date);
        String returnedMsg = appointeService.deleteCancelledBefore(theDate);
        return ResponseEntity.status(HttpStatus.OK).body(returnedMsg);
    }
}

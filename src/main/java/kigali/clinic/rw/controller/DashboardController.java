package kigali.clinic.rw.controller;

import kigali.clinic.rw.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // D4
    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(){
        Map<String, Long> summary = dashboardService.getSummary();
        return ResponseEntity.status(HttpStatus.OK).body(summary);
    }
}

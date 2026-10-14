package kigali.clinic.rw.service;

import kigali.clinic.rw.domain.Office;

import java.util.List;

public interface OfficeService {
    String saveOffice(Office office);
    List<Office> getOffices();
    String deleteOffice(Long id);
    String updateOffice(Office office);
    List<Office> getFreeOffices();
    List<Object[]> getOfficesByAppointmentCount();
}

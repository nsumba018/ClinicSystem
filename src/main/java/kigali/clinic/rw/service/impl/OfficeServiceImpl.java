package kigali.clinic.rw.service.impl;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeServiceImpl implements OfficeService {

    @Autowired
    private OfficeRepository offRepo;

    @Override
    public String saveOffice(Office office){
        Optional<Office> checkOffice = offRepo.findByOfficeNumber(office.getOfficeNumber());
        if(checkOffice.isPresent()){
            return "Office Already Exists";
        }
         offRepo.save(office);
        return "Office is saved successfully";
    }

    @Override
    public List<Office> getOffices(){
        List<Office> allOffices = offRepo.findAll();
        return allOffices;
    }

    @Override
    public String deleteOffice(Long id){
        offRepo.deleteById(id);
        return "Deleted Office successfully";
    }

    @Override
    public String updateOffice(Office office){
        offRepo.updateOfficeById(office.getName(), office.getOfficeNumber(), office.getId());
        return "Office updated successfully";
    }

    @Override
    public List<Object[]> getOfficesByAppointmentCount(){
        Pageable topOne = PageRequest.of(0, 1);
        return offRepo.findOfficesByAppointmentCount(topOne);
    }
}

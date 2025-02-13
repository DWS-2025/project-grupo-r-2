package com.spartanwrath.service;

import com.spartanwrath.exceptions.NoSuchMem;
import com.spartanwrath.model.Membership;
import com.spartanwrath.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository memRepo;

    public MembershipService() {}

    public Membership findById(long id) throws NoSuchMem {
        return memRepo.findById(id).orElseThrow(NoSuchMem::new);
    }

    public List<Membership> findAll() {
        return memRepo.findAll();
    }

    public Membership save(Membership membership) {
        membership.setFechaalta(LocalDate.now());
        if (membership.getDescripcion().contains("1 mes")){
            membership.setFechafin(membership.getFechaalta().plusMonths(1));
        } else if (membership.getDescripcion().contains("3 meses")) {
            membership.setFechafin(membership.getFechaalta().plusMonths(3));
        }
        membership.setActive(true);
        return memRepo.save(membership);
    }

    public void delete(long id) {
        memRepo.deleteById(id);
    }

}



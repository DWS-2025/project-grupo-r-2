package com.spartanwrath.service;

import com.spartanwrath.dto.MembershipDTO;
import com.spartanwrath.mappers.MembershipMapper;
import com.spartanwrath.exceptions.NoSuchMem;
import com.spartanwrath.model.Membership;
import com.spartanwrath.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository memRepo;

    @Qualifier("membershipMapperImpl")
    @Autowired
    private MembershipMapper mapper;

    public MembershipService() {}

    public Membership findById(long id) throws NoSuchMem {
        return memRepo.findById(id).orElseThrow(NoSuchMem::new);
    }
    public MembershipDTO findByIdDTO(long id) throws NoSuchMem {
        Membership membership = memRepo.findById(id).orElseThrow(NoSuchMem::new);
        return toDTO(membership);
    }

    public List<Membership> findAll() {
        return memRepo.findAll();
    }
    public List<MembershipDTO> findAllDTO() {
        List<Membership> memberships = memRepo.findAll();
        return toDTOs(memberships);
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
    public MembershipDTO saveDTO(MembershipDTO membershipDTO) {
        Membership membership = toDomain(membershipDTO);
        membership.setFechaalta(LocalDate.now());

        if (membership.getDescripcion().contains("1 mes")) {
            membership.setFechafin(membership.getFechaalta().plusMonths(1));
        } else if (membership.getDescripcion().contains("3 meses")) {
            membership.setFechafin(membership.getFechaalta().plusMonths(3));
        }
        membership.setActive(true);

        membership = memRepo.save(membership);
        return toDTO(membership);
    }

    public void delete(long id) {
        memRepo.deleteById(id);
    }
    public MembershipDTO toDTO(Membership membership){
        return mapper.toDTO(membership);
    }
    public List<MembershipDTO> toDTOs(List<Membership> memberships){
        return mapper.toDTOs(memberships);
    }
    public Membership toDomain(MembershipDTO membershipDTO){
        return mapper.toDomain(membershipDTO);
    }
}



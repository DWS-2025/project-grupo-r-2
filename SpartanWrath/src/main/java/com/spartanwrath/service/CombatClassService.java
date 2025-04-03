package com.spartanwrath.service;

import com.spartanwrath.dto.CombatClassDTO;
import com.spartanwrath.dto.CombatClassMapper;
import com.spartanwrath.model.CombatClass;
import com.spartanwrath.repository.CombatClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CombatClassService {

    @Autowired
    private CombatClassRepository ccRepo;

    @Qualifier("combatClassMapperImpl")
    @Autowired
    private CombatClassMapper mapper;

    public CombatClassService() {
    }

    public Optional<CombatClass> findById(long id) {
        return ccRepo.findById(id);
    }
    public CombatClassDTO findByIdDTO(long id) {
        CombatClass combatClass = ccRepo.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));
        return toDTO(combatClass);
    }
    public boolean exist(long id) {
        return ccRepo.existsById(id);
    }

    public List<CombatClass> findAll() {
        return ccRepo.findAll();
    }
    public List<CombatClassDTO> findAllDTO() {
        List<CombatClass> combatClasses = ccRepo.findAll();
        return toDTOs(combatClasses);
    }

    public CombatClass save(CombatClass clase) {
        return ccRepo.save(clase);
    }
    public CombatClassDTO save(CombatClassDTO combatClassDTO) {
        CombatClass combatClass = toDomain(combatClassDTO);
        combatClass = ccRepo.save(combatClass);
        return toDTO(combatClass);
    }

    public void delete(long id) {
        ccRepo.deleteById(id);
    }
    public CombatClassDTO toDTO(CombatClass combatClass){
        return mapper.toDTO(combatClass);
    }

    public List<CombatClassDTO> toDTOs(List<CombatClass> combatclasses){
        return mapper.toDTOs(combatclasses);
    }

    public CombatClass toDomain(CombatClassDTO combatClassDTO){
        return mapper.toDomain(combatClassDTO);
    }

}

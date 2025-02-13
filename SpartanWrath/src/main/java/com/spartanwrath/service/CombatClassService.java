package com.spartanwrath.service;

import com.spartanwrath.model.CombatClass;
import com.spartanwrath.repository.CombatClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CombatClassService {

    @Autowired
    private CombatClassRepository ccRepo;

    public CombatClassService() {
    }

    public Optional<CombatClass> findById(long id) {
        return ccRepo.findById(id);
    }

    public boolean exist(long id) {
        return ccRepo.existsById(id);
    }

    public List<CombatClass> findAll() {
        return ccRepo.findAll();
    }

    public CombatClass save(CombatClass clase) {
        return ccRepo.save(clase);
    }

    public void delete(long id) {
        ccRepo.deleteById(id);
    }
}

package com.spartanwrath.restController;

import com.spartanwrath.model.CombatClass;
import com.spartanwrath.service.CombatClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/combatclasses")
public class CombatClassRestController {

    @Autowired
    private CombatClassService combatClassService;

    @GetMapping("")
    public ResponseEntity<List<CombatClass>> getAllCombatClasses(){
        List<CombatClass> combatClasses = combatClassService.findAll();
        if (combatClasses.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(combatClasses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CombatClass>> getCombatClass(@PathVariable long id){
        Optional<CombatClass> combatClassOptional = combatClassService.findById(id);
        if (combatClassOptional.isPresent()){
            return ResponseEntity.ok().body(combatClassOptional);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/new")
    public ResponseEntity<CombatClass> createCombatClass(@RequestBody CombatClass combatClass){
        CombatClass newCombatClass = combatClassService.save(combatClass);
        return ResponseEntity.ok().body(newCombatClass);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCombatClass(@PathVariable long id){
        if(combatClassService.exist(id)) {
            combatClassService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

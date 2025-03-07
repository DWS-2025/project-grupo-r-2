package com.spartanwrath.restController;

import com.fasterxml.jackson.annotation.JsonView;
import com.spartanwrath.model.CombatClass;
import com.spartanwrath.model.Membership;
import com.spartanwrath.service.CombatClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CombatClassRestController {

    @Autowired
    private CombatClassService combatClassService;
    @JsonView(CombatClass.Basico.class)
    @GetMapping("/combatclass")
    public ResponseEntity<List<CombatClass>> getAllCombatClasses(){
        List<CombatClass> combatClasses = combatClassService.findAll();
        if (combatClasses.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(combatClasses);
    }

    interface CombatClassDetails extends CombatClass.Basico, CombatClass.Memberships, Membership.Basico {}

    @JsonView(CombatClassDetails.class)
    @GetMapping("/combatclass/{id}")
    public ResponseEntity<Optional<CombatClass>> getCombatClass(@PathVariable long id){
        Optional<CombatClass> combatClassOptional = combatClassService.findById(id);
        if (combatClassOptional.isPresent()){
            return ResponseEntity.ok().body(combatClassOptional);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/combatclass")
    public ResponseEntity<CombatClass> createCombatClass(@RequestBody CombatClass combatClass){
        CombatClass newCombatClass = combatClassService.save(combatClass);
        return ResponseEntity.ok().body(newCombatClass);
    }

    @DeleteMapping("/combatclass/{id}")
    public ResponseEntity<Object> deleteCombatClass(@PathVariable long id){
        if(combatClassService.exist(id)) {
            combatClassService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

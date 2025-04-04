package com.spartanwrath.restController;

import com.fasterxml.jackson.annotation.JsonView;
import com.spartanwrath.model.CombatClass;
import com.spartanwrath.model.Membership;
import com.spartanwrath.service.CombatClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spartanwrath.dto.CombatClassDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CombatClassRestController {

    @Autowired
    private CombatClassService combatClassService;

    @GetMapping("/combatclass")
    public ResponseEntity<List<CombatClassDTO>> getAllCombatClassesDTO(){
        List<CombatClassDTO> combatClasses = combatClassService.findAll().stream()
                .map(combatClass -> combatClassService.toDTO(combatClass))  // Convertir cada CombatClass a CombatClassDTO
                .toList();

        if (combatClasses.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(combatClasses);
    }


    @GetMapping("/combatclass/{id}")
    public ResponseEntity<CombatClassDTO> getCombatClassDTO(@PathVariable long id){
        Optional<CombatClass> combatClassOptional = combatClassService.findById(id);
        if (combatClassOptional.isPresent()){
            CombatClassDTO combatClassDTO = combatClassService.toDTO(combatClassOptional.get());  // Convertir a DTO a través del servicio
            return ResponseEntity.ok().body(combatClassDTO);
        }
        return ResponseEntity.notFound().build();
    }

    /*@PostMapping("/combatclass")
    public ResponseEntity<CombatClass> createCombatClass(@RequestBody CombatClass combatClass){
        CombatClass newCombatClass = combatClassService.save(combatClass);
        return ResponseEntity.ok().body(newCombatClass);
    }*/
    @PostMapping("/combatclass")
    public ResponseEntity<CombatClassDTO> createCombatClassDTO(@RequestBody CombatClassDTO combatClassDTO){
        CombatClass combatClass = combatClassService.toDomain(combatClassDTO);  // Convertir DTO a entidad
        CombatClass newCombatClass = combatClassService.save(combatClass);
        CombatClassDTO newCombatClassDTO = combatClassService.toDTO(newCombatClass);
        return ResponseEntity.ok().body(newCombatClassDTO);  // Convertir la entidad guardada a DTO
    }

    /*@DeleteMapping("/combatclass/{id}")
    public ResponseEntity<Object> deleteCombatClass(@PathVariable long id){
        if(combatClassService.exist(id)) {
            combatClassService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
    @DeleteMapping("/combatclass/{id}")
    public ResponseEntity<Object> deleteCombatClassDTO(@PathVariable long id){
        if(combatClassService.exist(id)) {
            combatClassService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

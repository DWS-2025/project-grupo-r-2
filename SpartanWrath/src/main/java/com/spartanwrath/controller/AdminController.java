package com.spartanwrath.controller;

import com.spartanwrath.model.CombatClass;
import com.spartanwrath.service.CombatClassService;
import com.spartanwrath.service.MembershipService;
import com.spartanwrath.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    MembershipService membershipService;
    @Autowired
    CombatClassService combatClassService;

    @GetMapping("/Admin")
    public String showAdmin(){
        return "admin";
    }

    @GetMapping("/Admin/combatclass")
    public String showCombatClasses(Model model){
        List<CombatClass> combatClassList = combatClassService.findAll();
        model.addAttribute("combatclasses",combatClassList);
        return "combatclass";
    }

    @GetMapping("/Admin/combatclass/{id}")
    public String showCombatClass(Model model, @PathVariable Long id) {

        Optional<CombatClass> combatClass = combatClassService.findById(id);
        if (combatClass.isPresent()) {
            CombatClass combatclass = combatClass.get();
            model.addAttribute("combatclasses", combatclass);
            return "combatclass";
        } else {
            return "redirect:/error";
        }
    }
    @PostMapping("/nuevaclase")
    public String newCombatClass(Model model, CombatClass combatClass) throws IOException {

        CombatClass newCombatClass = combatClassService.save(combatClass);

        model.addAttribute("combatclasses", newCombatClass.getId());

        return "redirect:/Admin/combatclass/"+newCombatClass.getId();
    }

    @GetMapping("/Admin/combatclass/{id}/delete")
    public String deleteCombatClass(@PathVariable long id) {

        combatClassService.delete(id);

        return "redirect:/Admin/combatclass";
    }

    @GetMapping("/Admin/combatclass/formcombatclass")
    public String showClassForm(){
        return "formcombatclass";
    }

}

package com.spartanwrath.controller;

import com.spartanwrath.exceptions.NoSuchClass;
import com.spartanwrath.model.CombatClass;
import com.spartanwrath.model.Membership;
import com.spartanwrath.service.CombatClassService;
import com.spartanwrath.service.MembershipService;
import com.spartanwrath.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.security.Principal;
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
    public String showCombatClass(Model model, @PathVariable Long id,HttpServletRequest request) {

        Optional<CombatClass> combatClass = combatClassService.findById(id);
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        if (combatClass.isPresent()) {
            CombatClass combatclass = combatClass.get();
            model.addAttribute("combatclasses", combatclass);
            return "combatclass";
        } else {
            return "redirect:/error";
        }
    }
    @PostMapping("/nuevaclase")
    public String newCombatClass(Model model, CombatClass combatClass,HttpServletRequest request) throws IOException {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        CombatClass newCombatClass = combatClassService.save(combatClass);

        model.addAttribute("combatclasses", newCombatClass.getId());

        return "redirect:/Admin/combatclass/"+newCombatClass.getId();
    }

    @GetMapping("/Admin/combatclass/{id}/delete")
    public String deleteCombatClass(@PathVariable long id) throws NoSuchClass {
        Optional<CombatClass> combatClass = combatClassService.findById(id);
        if (combatClass.isEmpty()){
            throw new NoSuchClass();
        }
        List<Membership> memberships = combatClass.get().getMemberships();
        for (Membership membership : memberships) {
            membership.setCombatClass(null);
            membershipService.save(membership);
        }
        combatClassService.delete(id);

        return "redirect:/Admin/combatclass";
    }

    @GetMapping("/Admin/combatclass/formcombatclass")
    public String showClassForm(){
        return "formcombatclass";
    }

}

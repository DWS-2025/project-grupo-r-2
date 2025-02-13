package com.spartanwrath.controller;

import com.spartanwrath.exceptions.NoSuchMem;
import com.spartanwrath.model.Membership;
import com.spartanwrath.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class MembershipsController {

    private final MembershipService membershipService;
    @Autowired
    public MembershipsController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }




    @GetMapping("/Membership")
    public String showMembership(Model model) {
        List<Membership> membershipList = membershipService.findAll();
        model.addAttribute("membership", membershipList);
        return "membership";
    }

    @GetMapping("/Mymemberships")
    public String showMymemberships(){
        return "mymemberships";
    }

    @GetMapping("/Membership/formsuscripcion")
    public String showFormSuscripcion(){
        return "formsuscripcion";
    }

    @GetMapping("/Membership/{id}")
    public String showMembership(Model model, @PathVariable Long id) throws NoSuchMem {

        Membership membership = membershipService.findById(id);
        if (membership != null) {
            model.addAttribute("membership", membership);
            return "Membership";
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/nuevasuscripcion")
    public String newMembership(Model model, Membership membership) throws IOException {

        Membership newmembership = membershipService.save(membership);

        model.addAttribute("MembershipId", newmembership.getId());

        return "redirect:/Membership/" + newmembership.getId();
    }

    @GetMapping("/Membership/{id}/delete")
    public String deleteMembership(@PathVariable long id) {

        membershipService.delete(id);

        return "Membership";
    }
}

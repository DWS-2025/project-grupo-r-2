package com.spartanwrath.controller;

import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.NoSuchMem;
import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.Membership;
import com.spartanwrath.model.User;
import com.spartanwrath.service.MembershipService;
import com.spartanwrath.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class MembershipsController {

    private final MembershipService membershipService;
    @Autowired
    UserService userService;
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

    @PostMapping("/Membership/{id}")
    public String subscribeToMembership(@PathVariable long id, Model model, HttpServletRequest request) {
        try {
            CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
            model.addAttribute("token", token.getToken());
            String authenticatedUsername = request.getUserPrincipal().getName();
            User authenticatedUser = userService.getUserbyUsername(authenticatedUsername);
            long userId = authenticatedUser.getId();
            boolean isAdmin = request.isUserInRole("ADMIN");
            if (!isAdmin && !authenticatedUser.getId().equals(userId)) {
                model.addAttribute("error", "No tienes permiso para realizar esta acción.");
                return "error";
            }

            User user = userService.getUserbyId(userId);

            Membership membership = membershipService.findById(id);

            user.setMembership(membership);

            userService.updateUser(user.getUsername(),user);

            return "redirect:/private";

        } catch (UserNotFound e) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al realizar la suscripción.");
            return "error";
        }
    }


    @PostMapping("/nuevasuscripcion")
    public String newMembership(Model model, Membership membership,HttpServletRequest request) throws IOException {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        Membership newmembership = membershipService.save(membership);

        model.addAttribute("MembershipId", newmembership.getId());

        return "redirect:/Membership/" + newmembership.getId();
    }

    @GetMapping("/Membership/{id}/delete")
    public String deleteMembership(@PathVariable long id) throws NoSuchMem, UserNotFound, InvalidUser {
        Membership membership = membershipService.findById(id);
        if (membership == null){
            throw new NoSuchMem();
        }
        List<User> users = membership.getUser();
        for (User user : users) {
            user.setMembership(null);
            userService.updateUser(user.getUsername(),user);  // Asegúrate de guardar los cambios en los usuarios
        }
        membershipService.delete(id);

        return "redirect:/Membership";
    }
}

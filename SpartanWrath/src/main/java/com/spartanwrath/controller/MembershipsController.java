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
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public String showMembership(Model model, HttpServletRequest request) {
        boolean isAdmin = request.isUserInRole("ADMIN");
        model.addAttribute("admin", isAdmin);

        List<Membership> membershipList = membershipService.findAll();
        model.addAttribute("membership", membershipList);

        return "memberships";
    }

    @GetMapping("/Membership/{id}")
    public String showMembershipDetails(Model model, @PathVariable Long id, HttpServletRequest request) throws NoSuchMem {
        boolean isAdmin = request.isUserInRole("ADMIN");
        model.addAttribute("admin", isAdmin);

        Membership membership = membershipService.findById(id);
        if (membership != null) {
            model.addAttribute("membership", membership);
            CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
            model.addAttribute("token", token.getToken());
            return "membership";  // Nueva página que muestra detalles de la membresía
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
            userService.updateUser(user.getUsername(), user);

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
    public String newMembership(Model model, Membership membership, HttpServletRequest request) throws IOException {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());

        Membership newMembership = membershipService.save(membership);
        model.addAttribute("MembershipId", newMembership.getId());

        return "redirect:/Membership/" + newMembership.getId();
    }

    @GetMapping("/Membership/{id}/delete")
    public String deleteMembership(@PathVariable long id) throws NoSuchMem, UserNotFound, InvalidUser {
        Membership membership = membershipService.findById(id);
        if (membership == null) {
            throw new NoSuchMem();
        }
        List<User> users = membership.getUser();
        for (User user : users) {
            user.setMembership(null);
            userService.updateUser(user.getUsername(), user);  // Asegúrate de guardar los cambios en los usuarios
        }
        membershipService.delete(id);

        return "redirect:/Membership";
    }
}

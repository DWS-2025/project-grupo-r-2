package com.spartanwrath.restController;

import com.fasterxml.jackson.annotation.JsonView;
import com.spartanwrath.exceptions.NoMembership;
import com.spartanwrath.exceptions.NoSuchMem;
import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.CombatClass;
import com.spartanwrath.model.Membership;
import com.spartanwrath.model.User;
import com.spartanwrath.service.MembershipService;
import com.spartanwrath.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MembershipRestController {

    @Autowired
    private MembershipService membershipService;
    @Autowired
    private UserService userService;
    @JsonView(Membership.Basico.class)
    @GetMapping("/Membership")
    public ResponseEntity<List<Membership>> getAllMembership(){
        List<Membership> membership = membershipService.findAll();
        if (membership.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(membership);
    }

    interface MemDetails extends Membership.Basico, Membership.CombatClasses, Membership.Users, CombatClass.Basico, User.Basico {}
    @JsonView(MemDetails.class)
    @GetMapping("/Membership/{id}")
    public ResponseEntity<Membership> getMembership(@PathVariable long id) throws NoSuchMem {
        try {
            Membership membership = membershipService.findById(id);
            return ResponseEntity.ok().body(membership);
        }catch (NoSuchMem noSuchMem){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/Membership")
    public ResponseEntity<Membership> newMembership(@RequestBody Membership membership){
        membershipService.save(membership);
        return ResponseEntity.ok().body(membership);
    }
    @PostMapping("/Membership/{id}")
    public ResponseEntity<?> subscribeToMembership(@PathVariable long id, HttpServletRequest request) {
        try {
            String authenticatedUsername = request.getUserPrincipal().getName();
            User authenticatedUser = userService.getUserbyUsername(authenticatedUsername);
            long userId = authenticatedUser.getId();
            boolean isAdmin = request.isUserInRole("ADMIN");

            if (!isAdmin && !authenticatedUser.getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para realizar esta acción.");
            }

            User user = userService.getUserbyId(userId);
            Membership membership = membershipService.findById(id);

            if (membership == null) {
                return ResponseEntity.notFound().build();
            }

            user.setMembership(membership);
            userService.updateUser(user.getUsername(), user);

            return ResponseEntity.ok().body("Usuario suscrito a la membresía correctamente.");

        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ocurrió un error al realizar la suscripción.");
        }
    }

    @DeleteMapping("/Membership/{id}")
    public ResponseEntity<Membership> deleteMem(@PathVariable long id){
        membershipService.delete(id);
        return ResponseEntity.ok().build();
    }

}

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
import com.spartanwrath.dto.MembershipDTO;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MembershipRestController {

    @Autowired
    private MembershipService membershipService;
    @Autowired
    private UserService userService;
    @JsonView(Membership.Basico.class)
    // ?? @JsonView(MembershipDTO.Basico.class)
    /*@GetMapping("/Membership")
    public ResponseEntity<List<Membership>> getAllMembership(){
        List<Membership> membership = membershipService.findAll();
        if (membership.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(membership);
    }*/
    @GetMapping("/Membership")
    public ResponseEntity<List<MembershipDTO>> getAllMembershipDTO() {
        List<MembershipDTO> membershipDTOs = membershipService.findAll().stream()
                .map(membershipService::toDTO)  // Convertir cada Membership a MembershipDTO
                .toList();

        if (membershipDTOs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(membershipDTOs);
    }

    interface MemDetails extends Membership.Basico, Membership.CombatClasses, Membership.Users, CombatClass.Basico, User.Basico {}

    /*@GetMapping("/Membership/{id}")
    public ResponseEntity<Membership> getMembership(@PathVariable long id) throws NoSuchMem {
        try {
            Membership membership = membershipService.findById(id);
            return ResponseEntity.ok().body(membership);
        }catch (NoSuchMem noSuchMem){
            return ResponseEntity.notFound().build();
        }
    }*/
    @JsonView(MemDetails.class)
    @GetMapping("/Membership/{id}")
    public ResponseEntity<MembershipDTO> getMembershipDTO(@PathVariable long id) throws NoSuchMem {
        try {
            Membership membership = membershipService.findById(id);
            MembershipDTO membershipDTO = membershipService.toDTO(membership);  // Convertir a DTO
            return ResponseEntity.ok().body(membershipDTO);
        } catch (NoSuchMem noSuchMem) {
            return ResponseEntity.notFound().build();
        }
    }


    /*@PostMapping("/Membership")
    public ResponseEntity<Membership> newMembership(@RequestBody Membership membership){
        membershipService.save(membership);
        return ResponseEntity.ok().body(membership);
    }*/
    @PostMapping("/Membership")
    public ResponseEntity<MembershipDTO> newMembershipDTO(@RequestBody MembershipDTO membershipDTO) {
        Membership membership = membershipService.toDomain(membershipDTO);  // Convertir DTO a entidad
        membershipService.save(membership);
        return ResponseEntity.ok().body(membershipService.toDTO(membership));  // Convertir la entidad guardada a DTO
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
    public ResponseEntity<?> deleteMem(@PathVariable long id) {
        try {
            // catch membership
            Membership membership = membershipService.findById(id);

            // Unlink users from membership
            for (User user : membership.getUser()) {
                user.setMembership(null);
                userService.updateUser(user.getUsername(), user);
            }

            // delete membership
            membershipService.delete(id);
            return ResponseEntity.ok().body("Membresía eliminada correctamente.");

        } catch (NoSuchMem e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La membresía con ID " + id + " no existe.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la membresía: " + e.getMessage());
        }
    }

}

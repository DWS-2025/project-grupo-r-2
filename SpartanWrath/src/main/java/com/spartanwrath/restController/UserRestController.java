package com.spartanwrath.restController;


import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.NoUsers;
import com.spartanwrath.exceptions.UserAlreadyRegister;
import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.User;
import com.spartanwrath.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spartanwrath.dto.UserDTO;


import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userServ ;


    @GetMapping("/User")
    public ResponseEntity<List<UserDTO>> getAllUsersDTO(HttpServletRequest request) {
        if (!request.isUserInRole("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<UserDTO> users = userServ.toDTOs(userServ.GetAllUsers());
            return ResponseEntity.ok().body(users);
        } catch (NoUsers noUsers) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/User/{username}")
    public ResponseEntity<UserDTO> getUserDTO(@PathVariable String username, HttpServletRequest request) {
        String authenticatedUsername = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");

        if (!isAdmin && !authenticatedUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            UserDTO userDTO = userServ.toDTO(userServ.getUserbyUsername(username));
            return ResponseEntity.ok().body(userDTO);
        } catch (UserNotFound userNotFound) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/User")
    public ResponseEntity<User> newUser(@RequestBody User user){
        try {
            userServ.add(user);
            return ResponseEntity.ok().body(user);
        } catch (UserAlreadyRegister e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (InvalidUser e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/User/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody User upuser, HttpServletRequest request) throws NoUsers, InvalidUser {
        String authenticatedUsername = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");

        if (!isAdmin && !authenticatedUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<User> allUsers = userServ.GetAllUsers();
        for (User u : allUsers) {
            if (u.getUsername().equals(upuser.getUsername()) && !Objects.equals(u.getId(), upuser.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de usuario '" + upuser.getUsername() + "' ya está en uso.");
            }
        }
    try {
        userServ.updateUser(username,upuser);
        return ResponseEntity.ok().build();
    } catch (UserNotFound e) {
        return ResponseEntity.notFound().build();
    } catch (InvalidUser e) {
        return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/User/{username}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String username, HttpServletRequest request) {
        String authenticatedUsername = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");


        if (username.equalsIgnoreCase("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!isAdmin && !authenticatedUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            UserDTO userDTO = userServ.toDTO(userServ.getUserbyUsername(username));
            userServ.delete(username);
            return ResponseEntity.ok().body(userDTO);
        } catch (UserNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

}

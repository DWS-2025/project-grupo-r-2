package com.spartanwrath.restController;


import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.NoUsers;
import com.spartanwrath.exceptions.UserAlreadyRegister;
import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.User;
import com.spartanwrath.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/User")
public class UserRestController {

    @Autowired
    private UserService userServ ;

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers(){
        try {
            List<User> users = userServ.GetAllUsers();
            return ResponseEntity.ok().body(users);
        } catch (NoUsers noUsers){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username){
        try {
            User user = userServ.getUserbyUsername(username);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFound userNotFound){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/new")
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


    @PutMapping("/update/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User upuser) {
    try {
        userServ.updateUser(username,upuser);
        return ResponseEntity.ok().body(upuser);
    } catch (UserNotFound e) {
        return ResponseEntity.notFound().build();
    } catch (InvalidUser e) {
        return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        try {
            User user = userServ.delete(username);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFound e){
            return ResponseEntity.notFound().build();
        }
    }
}



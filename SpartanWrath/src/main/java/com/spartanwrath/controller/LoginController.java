package com.spartanwrath.controller;

import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.UserAlreadyRegister;
import com.spartanwrath.model.User;
import com.spartanwrath.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @GetMapping("/login")
    public String loginPage() {
        // Mostrar la página de inicio de sesión
        return "login";
    }
    @PostMapping("/register")
    public String registerUser(User user) throws UserAlreadyRegister, InvalidUser {
        // Agregar el usuario al UserManager
        userService.add(user);
        return "redirect:/login";
    }
    /*
    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        // Verificar las credenciales del usuario
        if (DatabaseInitializer.authenticate(username, password)) {
            if (DatabaseInitializer.isAdmin(username)) {
                // Si el usuario es un administrador, redirigir a la página de administrador
                return "redirect:/admin";
            } else {
                // Si el usuario no es un administrador, redirigir a la página de perfil
                return "redirect:/private";
            }
        } else {
            // Si las credenciales son incorrectas, redirigir a la página de inicio de sesión con un mensaje de error
            return "redirect:/login?error";
        }
    }
    */

    @GetMapping("/admin")
    public String adminPage() {
        // Mostrar la página de administrador
        return "admin/admin";
    }

    @GetMapping("/register")
    public String registerPage() {
        // Mostrar la página de registro de usuario
        return "login";
    }

    @GetMapping("/private")
    public String privatePage() {
        // Mostrar la página privada para los usuarios autenticados
        return "private";
    }

}

package com.spartanwrath.controller;

import com.spartanwrath.exceptions.*;
import com.spartanwrath.model.Product;
import com.spartanwrath.model.User;
import com.spartanwrath.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    UserService userService;


    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());

        return "login";
    }

    @PostMapping("/register")
    public String registerUser(User user,Model model, HttpServletRequest request) throws UserAlreadyRegister, InvalidUser {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        // Agregar el usuario al UserManager
        userService.add(user);
        return "redirect:/login";
    }

    @GetMapping("/private/{id}")
    public String updateForm (@PathVariable("id") Long id, Model model,HttpServletRequest request) throws UserNotFound {
        User user = userService.getUserbyId(id);
        String name = request.getUserPrincipal().getName();
        User authenticatedUser = userService.findByName(name).orElseThrow(UserNotFound::new);

        if (!Objects.equals(user.getId(), authenticatedUser.getId())){
            return "error";
        }
        model.addAttribute("user",user);
        model.addAttribute("todayDate", LocalDate.now().toString());
        return "editarprivate";
    }

    @PostMapping("/private/{id}")
    public String updateUser(@PathVariable("id") long id, Model model, HttpServletRequest request, User user) throws UserNotFound, InvalidUser, NoUsers {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());

        String name = request.getUserPrincipal().getName();
        User authenticatedUser = userService.findByName(name).orElseThrow(UserNotFound::new);
        User usuario = userService.getUserbyId(id);
        if (!Objects.equals(usuario.getId(), authenticatedUser.getId())){
            return "error";
        }

        List<User> allUsers = userService.GetAllUsers();
        for (User u : allUsers) {
            if (u.getUsername().equals(user.getUsername()) && !Objects.equals(u.getId(), usuario.getId())) {
                model.addAttribute("error", "El nombre de usuario ya está en uso.");
                return "editarprivate"; // Vuelve a la página de edición con un mensaje de error
            }
        }

        usuario.setName(user.getName());
        usuario.setAddress(user.getAddress());
        usuario.setDni(user.getDni());
        usuario.setPhone(user.getPhone());
        usuario.setUsername(user.getUsername());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            usuario.setPassword(user.getPassword());
        }
        usuario.setBirthday(user.getBirthday());
        usuario.setPayment(user.getPayment());
        usuario.setEmail(user.getEmail());
        userService.updateUser(usuario.getUsername(),usuario);
        return "editarprivate";
    }

    @GetMapping("/admin")
    public String adminPage() {
        // Mostrar la página de administrador
        return "admin/admin";
    }



    @GetMapping("/private")
    public String privatePage(Model model, HttpServletRequest request) throws NoUsers {
        String name = request.getUserPrincipal().getName();
        User user = userService.findByName(name).orElseThrow();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("admin",request.isUserInRole("ADMIN"));
        // Mostrar la página privada para los usuarios autenticados

    if (model.containsAttribute("admin")){
        List<User> userList = userService.GetAllUsers();
        model.addAttribute("users",userList);
    }


        return "private";
    }

    @GetMapping("/private/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id,HttpServletRequest request) throws UserNotFound, AccessDeniedException {
        String name = request.getUserPrincipal().getName();
        User authenticatedUser = userService.findByName(name).orElseThrow(UserNotFound::new);
        User user = userService.getUserbyId(id);
        if ("Admin".equals(user.getUsername())){
            throw  new AccessDeniedException("You can not delete the main admin account.");
        }

        if (!request.isUserInRole("ADMIN")){
            if (!Objects.equals(user.getId(), authenticatedUser.getId())){
            throw new AccessDeniedException("You do not have permission to delete this user.");
            }
        }

        userService.delete(user.getUsername());

        if (Objects.equals(user.getId(), authenticatedUser.getId())) {
            request.getSession().invalidate();
            return "redirect:/";
        }
        return "redirect:/private";

    }



}

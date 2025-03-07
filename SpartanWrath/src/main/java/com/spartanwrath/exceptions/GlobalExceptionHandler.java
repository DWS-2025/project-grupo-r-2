package com.spartanwrath.exceptions;


import com.spartanwrath.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        model.addAttribute("error", "FORBIDDEN: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(UserNotFound.class)
    public String handleUserNotFoundException(UserNotFound ex, Model model) {
        model.addAttribute("error", "User not found: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NoSuchMem.class)
    public String handleNoSuchMemExceptionn(NoSuchMem ex, Model model){
        model.addAttribute("error","Membership: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NoSuchClass.class)
    public String handleNoSuchClassExceptionn(NoSuchClass ex, Model model){
        model.addAttribute("error","CombatClass: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidUser.class)
    public String handleInvalidUserExceptionn(InvalidUser ex, Model model){
        model.addAttribute("error","User: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NoMembership.class)
    public String handleNoMembershipExceptionn(NoMembership ex, Model model){
        model.addAttribute("error","Membership: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(UserAlreadyRegister.class)
    public String handleUserAlreadyRegisterExceptionn(UserAlreadyRegister ex, Model model){
        model.addAttribute("error","User: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NoUsers.class)
    public String handleNoUsersExceptionn(NoUsers ex, Model model){
        model.addAttribute("error","Users: " + ex.getMessage());
        return "error";
    }
}

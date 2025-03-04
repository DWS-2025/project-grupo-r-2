package com.spartanwrath.service;

import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.NoUsers;
import com.spartanwrath.exceptions.UserAlreadyRegister;
import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.User;
import com.spartanwrath.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private UserRepository UserRepo ;

   private ConcurrentHashMap<Long, User> usuarios = new ConcurrentHashMap<>();


    public  List<User> GetAllUsers() throws NoUsers {
        List<User> users = UserRepo.findAll();
        if (users.isEmpty()){
            throw new NoUsers();
        } else {
            return users;
        }
    }
    public User getUserbyId(long id) throws UserNotFound {
        return UserRepo.findById(id).orElseThrow(UserNotFound::new);
    }

    public User getUserbyUsername(String username) throws UserNotFound{
        return UserRepo.findByUsername(username).orElseThrow(UserNotFound::new);
    }


    public List<User> findByIds(List<Long> ids){
        List<User> users = new ArrayList<>();
        for(long id : ids){
            users.add(this.usuarios.get(id));
        }
        return users;
    }

    public void updateUser(String username, User user) throws UserNotFound,InvalidUser{
        User validuser = UserRepo.findByUsername(username).orElseThrow(UserNotFound::new);
        if (!User.valid(user)){
            throw new InvalidUser();
        }
        validuser.setName(user.getName());
        validuser.setEmail(user.getEmail());
        validuser.setUsername(user.getUsername());
        validuser.setAddress(user.getAddress());
        validuser.setPhone(user.getPhone());
        validuser.setDni(user.getDni());
        validuser.setPayment(user.getPayment());
        validuser.setPassword(user.getPassword());
        UserRepo.save(validuser);
    }

    public void add(User user) throws UserAlreadyRegister, InvalidUser {
        System.out.println("Intentando agregar un nuevo usuario: " + user);

        Optional<User> validuser = UserRepo.findByUsername(user.getUsername());
        if (validuser.isPresent()) {
            System.out.println("El usuario ya está registrado: " + user.getUsername());
            throw new UserAlreadyRegister();
        } else {
            if (User.valid(user)) {
                System.out.println("Guardando nuevo usuario: " + user);
                this.UserRepo.save(user);
                System.out.println("Usuario guardado exitosamente.");
            } else {
                System.out.println("El usuario no es válido: " + user);
                throw new InvalidUser();
            }
        }
    }


    public User delete(String username) throws UserNotFound {
        User user = UserRepo.findByUsername(username).orElseThrow(UserNotFound::new);
        UserRepo.delete(user);
        return user;
    }

    public int getNumberUsers(){
        return (int) UserRepo.count();
    }
    public boolean exists(String username){
        return UserRepo.existsByUsername(username);
    }
}


package com.spartanwrath.service;

import com.spartanwrath.dto.UserDTO;
import com.spartanwrath.mappers.UserMapper;
import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.NoUsers;
import com.spartanwrath.exceptions.UserAlreadyRegister;
import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.User;
import com.spartanwrath.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private UserRepository UserRepo ;

    @Qualifier("userMapperImpl")
    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

   private ConcurrentHashMap<Long, User> usuarios = new ConcurrentHashMap<>();


    public  List<User> GetAllUsers() throws NoUsers {
        List<User> users = UserRepo.findAll();
        if (users.isEmpty()){
            throw new NoUsers();
        } else {
            return users;
        }
    }
    public List<UserDTO> getAllUsersDTO() throws NoUsers {
        List<User> users = UserRepo.findAll();
        if (users.isEmpty()){
            throw new NoUsers();
        } else {
            return toDTOs(users);
        }
    }
    public User getUserbyId(long id) throws UserNotFound {
        return UserRepo.findById(id).orElseThrow(UserNotFound::new);
    }
    public UserDTO getUserDTOById(long id) throws UserNotFound {
        User user = UserRepo.findById(id).orElseThrow(UserNotFound::new);
        return toDTO(user);
    }

    public Optional<User> findByName(String name){
        return UserRepo.findByUsername(name);
    }
    public Optional<UserDTO> findByNameDTO(String name){
        Optional<User> user = UserRepo.findByUsername(name);
        return user.map(this::toDTO);
    }

    public User getUserbyUsername(String username) throws UserNotFound{
        return UserRepo.findByUsername(username).orElseThrow(UserNotFound::new);
    }
    public UserDTO getUserDTObyUsername(String username) throws UserNotFound {
        User user = UserRepo.findByUsername(username).orElseThrow(UserNotFound::new);
        return toDTO(user);
    }

    public void removeProductFromUsers(Long productId) {
        // Eliminar todas las relaciones del producto en la tabla user_product
        UserRepo.deleteProductByProductId(productId);
    }


    public List<User> findByIds(List<Long> ids){
        List<User> users = new ArrayList<>();
        for(long id : ids){
            users.add(this.usuarios.get(id));
        }
        return users;
    }
    public List<UserDTO> findByIdsDTO(List<Long> ids) {
        List<User> users = new ArrayList<>();
        for(long id : ids){
            users.add(this.usuarios.get(id));
        }
        return toDTOs(users);
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
        if (!user.getPassword().startsWith("$2a$")){
            String pToEncode = user.getPassword();
            user.setPassword(passwordEncoder.encode(pToEncode));
        } else {
            validuser.setPassword(user.getPassword());
        }

        if (user.getBirthday() == null) {
            validuser.setBirthday(validuser.getBirthday());
        } else {
            validuser.setBirthday(user.getBirthday());
        }


        if (user.getProducts() == null) {
            validuser.setProducts(validuser.getProducts());
        } else {
            validuser.setProducts(user.getProducts());
        }


        if (user.getMembership() == null) {
            validuser.setMembership(validuser.getMembership());
        } else {
            validuser.setMembership(user.getMembership());
        }


        if (user.getRoles().isEmpty()) {
            validuser.setRoles(validuser.getRoles());
        } else {
            validuser.setRoles(user.getRoles());
        }

        UserRepo.save(validuser);
    }
    public void updateUserDTO(String username, UserDTO userDTO) throws UserNotFound, InvalidUser {
        User validUser = UserRepo.findByUsername(username).orElseThrow(UserNotFound::new);
        User user = toDomain(userDTO);

        if (!User.valid(user)){
            throw new InvalidUser();
        }

        validUser.setName(user.getName());
        validUser.setEmail(user.getEmail());
        validUser.setUsername(user.getUsername());
        validUser.setAddress(user.getAddress());
        validUser.setPhone(user.getPhone());
        validUser.setDni(user.getDni());
        validUser.setPayment(user.getPayment());

        if (!user.getPassword().startsWith("$2a$")) {
            validUser.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            validUser.setPassword(user.getPassword());
        }

        if (user.getBirthday() == null) {
            validUser.setBirthday(validUser.getBirthday());
        } else {
            validUser.setBirthday(user.getBirthday());
        }

        if (user.getProducts() == null) {
            validUser.setProducts(validUser.getProducts());
        } else {
            validUser.setProducts(user.getProducts());
        }

        if (user.getMembership() == null) {
            validUser.setMembership(validUser.getMembership());
        } else {
            validUser.setMembership(user.getMembership());
        }

        if (user.getRoles().isEmpty()) {
            validUser.setRoles(validUser.getRoles());
        } else {
            validUser.setRoles(user.getRoles());
        }

        UserRepo.save(validUser);
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
                String pToEncode = user.getPassword();
                user.setPassword(passwordEncoder.encode(pToEncode));
                if (user.getRoles().isEmpty() || user.getRoles().contains("ADMIN")){
                    user.setRoles(List.of("USER"));
                }
                this.UserRepo.save(user);
                System.out.println("Usuario guardado exitosamente.");
            } else {
                System.out.println("El usuario no es válido: " + user);
                throw new InvalidUser();
            }
        }
    }
    public void addDTO(UserDTO userDTO) throws UserAlreadyRegister, InvalidUser {
        System.out.println("Intentando agregar un nuevo usuario: " + userDTO);

        User user = toDomain(userDTO);

        Optional<User> validUser = UserRepo.findByUsername(user.getUsername());
        if (validUser.isPresent()) {
            System.out.println("El usuario ya está registrado: " + user.getUsername());
            throw new UserAlreadyRegister();
        } else {
            if (User.valid(user)) {
                System.out.println("Guardando nuevo usuario: " + user);
                String pToEncode = user.getPassword();
                user.setPassword(passwordEncoder.encode(pToEncode));
                if (user.getRoles().isEmpty() || user.getRoles().contains("ADMIN")){
                    user.setRoles(List.of("USER"));
                }
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
    public UserDTO deleteDTO(String username) throws UserNotFound {
        User user = UserRepo.findByUsername(username).orElseThrow(UserNotFound::new);
        UserRepo.delete(user);
        return toDTO(user);
    }
    public int getNumberUsers(){
        return (int) UserRepo.count();
    }
    public boolean exists(String username){
        return UserRepo.existsByUsername(username);
    }
    public UserDTO toDTO(User user){
        return mapper.toDTO(user);
    }

    public User toDomain(UserDTO userDTO){
        return mapper.toDomain(userDTO);
    }

    public List<UserDTO> toDTOs(List<User> users){
        return mapper.toDTOs(users);
    }
}


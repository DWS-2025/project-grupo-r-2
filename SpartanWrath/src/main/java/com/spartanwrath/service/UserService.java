package com.spartanwrath.service;

import com.spartanwrath.dto.UserDTO;
import com.spartanwrath.mappers.UserMapper;
import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.NoUsers;
import com.spartanwrath.exceptions.UserAlreadyRegister;
import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.User;
import com.spartanwrath.repository.UserRepository;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private UserRepository UserRepo;

    @Qualifier("userMapperImpl")
    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ConcurrentHashMap<Long, User> usuarios = new ConcurrentHashMap<>();

    public List<User> GetAllUsers() throws NoUsers {
        List<User> users = UserRepo.findAll();
        if (users.isEmpty()) {
            throw new NoUsers();
        } else {
            return users;
        }
    }

    public List<UserDTO> getAllUsersDTO() throws NoUsers {
        List<User> users = UserRepo.findAll();
        if (users.isEmpty()) {
            throw new NoUsers();
        } else {
            return toDTOs(users);
        }
    }

    public User getUserbyId(long id) throws UserNotFound {
        return UserRepo.findById(id).orElseThrow(UserNotFound::new);
    }

    public Optional<User> findByName(String name) {
        return UserRepo.findByUsername(name);
    }

    public Optional<UserDTO> findByNameDTO(String name) {
        Optional<User> user = UserRepo.findByUsername(name);
        return user.map(this::toDTO);
    }

    public User getUserbyUsername(String username) throws UserNotFound {
        return UserRepo.findByUsername(username).orElseThrow(UserNotFound::new);
    }

    public void removeProductFromUsers(Long productId) {
        UserRepo.deleteProductByProductId(productId);
    }

    public void updateUser(String username, User user) throws UserNotFound, InvalidUser {
        User validuser = UserRepo.findByUsername(username).orElseThrow(UserNotFound::new);

        if (!User.valid(user)) {
            throw new InvalidUser();
        }

        user = sanitizeUser(user);

        validuser.setName(user.getName());
        validuser.setEmail(user.getEmail());
        validuser.setUsername(user.getUsername());
        validuser.setAddress(user.getAddress());
        validuser.setPhone(user.getPhone());
        validuser.setDni(user.getDni());
        validuser.setPayment(user.getPayment());

        if (!user.getPassword().startsWith("$2a$")) {
            validuser.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            validuser.setPassword(user.getPassword());
        }

        if (user.getBirthday() != null) {
            validuser.setBirthday(user.getBirthday());
        }

        if (user.getProducts() != null) {
            validuser.setProducts(user.getProducts());
        }

        if (user.getMembership() != null) {
            validuser.setMembership(user.getMembership());
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            validuser.setRoles(user.getRoles());
        }

        UserRepo.save(validuser);
    }

    public void add(User user) throws UserAlreadyRegister, InvalidUser {
        Optional<User> validuser = UserRepo.findByUsername(user.getUsername());
        if (validuser.isPresent()) {
            throw new UserAlreadyRegister();
        } else {
            if (User.valid(user)) {
                user = sanitizeUser(user);

                user.setPassword(passwordEncoder.encode(user.getPassword()));
                
                if (user.getRoles() == null || user.getRoles().isEmpty() || user.getRoles().contains("ADMIN")) {
                    user.setRoles(List.of("USER"));
                }

                this.UserRepo.save(user);
            } else {
                throw new InvalidUser();
            }
        }
    }

    public User delete(String username) throws UserNotFound {
        User user = UserRepo.findByUsername(username).orElseThrow(UserNotFound::new);
        UserRepo.delete(user);
        return user;
    }

    public boolean exists(String username) {
        return UserRepo.existsByUsername(username);
    }

    public UserDTO toDTO(User user) {
        return mapper.toDTO(user);
    }

    public User toDomain(UserDTO userDTO) {
        return mapper.toDomain(userDTO);
    }

    public List<UserDTO> toDTOs(List<User> users) {
        return mapper.toDTOs(users);
    }

    private User sanitizeUser(User user) {
        PolicyFactory policy = Sanitizers.FORMATTING
                .and(Sanitizers.BLOCKS)
                .and(Sanitizers.LINKS)
                .and(Sanitizers.STYLES)
                .and(Sanitizers.TABLES);

        user.setUsername(safeSanitize(policy, user.getUsername()));
        user.setName(safeSanitize(policy, user.getName()));
        user.setEmail(safeSanitize(policy, user.getEmail()));
        user.setAddress(safeSanitize(policy, user.getAddress()));
        user.setDni(safeSanitize(policy, user.getDni()));
        user.setPayment(safeSanitize(policy, user.getPayment()));

        return user;
    }

    private String safeSanitize(PolicyFactory policy, String input) {
        return input == null ? null : policy.sanitize(input);
    }
}

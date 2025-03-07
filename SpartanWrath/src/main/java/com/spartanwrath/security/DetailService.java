package com.spartanwrath.security;

import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.User;
import com.spartanwrath.repository.UserRepository;
import com.spartanwrath.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        if (user == null){
            throw new UsernameNotFoundException("User not found: " + username);
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : user.getRoles()){
            roles.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
    }
}

package com.mmu.ggk.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mmu.ggk.RoleRepository;
import com.mmu.ggk.UserRepository;
import com.mmu.ggk.model.User;



@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }
    public List<User> findByRole(String roleName) {
        return userRepository.findByRoles_Name(roleName);
    }
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }
    public boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
            .anyMatch(role -> role.getName().equals(roleName));
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
    
    
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    
    
    
    public User updateUser(Long id, User userDetails) {
        User user = findUserById(id);
        if (user != null) {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
          //  user.setRoles(null);.getRoles().setPassword(userDetails.getPassword());
            // Update other fields as necessary
            return userRepository.save(user);
        }
        return user;
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
   
    
    
    
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
////        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
////                .map(role -> new SimpleGrantedAuthority(role.getName()))
////                .collect(Collectors.toSet());
////        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
////    }
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
//    }
    public List <User> findAll(){
    	return userRepository.findAll();
    }
}

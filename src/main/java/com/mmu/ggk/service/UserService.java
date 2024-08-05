package com.mmu.ggk.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mmu.ggk.UserAuditRepository;
import com.mmu.ggk.UserRepository;
import com.mmu.ggk.model.Role;
import com.mmu.ggk.model.User;
import com.mmu.ggk.model.UserAudit;



@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuditRepository userAuditRepository;
   private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,  @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));     
        if(null!=user.getRoles()) {
    	   System.out.println("Roles are not Empty   .....");    	   
    	   for(Role roleSet:user.getRoles()) {
        	   System.out.println("LIst of Roles  for the user:::::: "+ roleSet.getName());  	
        	   user.setRoles(user.getRoles());       	        	   
        	 
           }  
    	   
       }
         
             
        userRepository.save(user);
        return user;
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
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), ((UserDetails) user).getAuthorities());
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

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    

    public List <User> findAll(){
    	return userRepository.findAll();
    }
    public void logAudit(Long userId, String action) {
        UserAudit audit = new UserAudit();
        audit.setUser_Id(userId);
        audit.setAction(action);        
        User us=userRepository.getById(userId);
        audit.setPerformedBy(us.getUsername()); // Replace with actual user
        audit.setPerformedDate(LocalDateTime.now());
        userAuditRepository.save(audit);
    }
}

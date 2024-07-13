package com.mmu.ggk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mmu.ggk.model.User;
import com.mmu.ggk.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.findAllUsers();
//    }
    
    
    
    
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }
//    @GetMapping("/registration")
//    public String registration(Model model, Principal principal) {
//    	System.out.println("Inside registration Method");
//        User currentUser = userService.findByUsername(principal.getName());
//        if (userService.hasRole(currentUser, "ADMIN")) {
//            model.addAttribute("userForm", new User());
//            model.addAttribute("users", userService.findAll());
//            return "registration";
//        } else {
//            return "redirect:/home";
//        }
//    }
    
    
    
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
    	System.out.println("Inside   /registration  ");
        // Add any necessary attributes to the model
        model.addAttribute("user", new User()); // Assuming you have a User class
        return "registration"; // Ensure this matches the name of your Thymeleaf template (e.g., registration.html)
    }
    
    
    
    
    
    
    
      
    
//    @GetMapping("/registration")
//    public String showRegistrationForm(Model model) {
//        // Add any necessary attributes to the model
//        return "registration"; // Ensure this matches the name of your Thymeleaf template (e.g., registration.html)
//    }
//    
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public User createUser(@RequestBody User user) {
//        return userService.saveUser(user);
//    }

//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public User updateUser(@PathVariable Long id, @RequestBody User user) {
//        return userService.updateUser(id, user);
//    }
//
//    @GetMapping("/me")
//    public User getCurrentUser(Principal principal) {
//        return userService.findByUsername(principal.getName());
//    }

//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
 
//    @GetMapping("/home")
//    public String home(Model model,Principal principal) {
//    	String username = principal.getName();
//        model.addAttribute("username", username);
//        // Add other attributes as needed
//        model.addAttribute("users", userService.findAll());
//        return "home";
//    }
    @PostMapping("/register")
    public String registerUser(User userForm, Model model) {
    	System.out.println("Inside registration  post Method");
        userForm.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        userService.save(userForm);
        model.addAttribute("userForm", new User());
        model.addAttribute("users", userService.findAll());
        return "redirect:/home";
    }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
      userService.deleteUser(id);
      return ResponseEntity.noContent().build();
  }  
  
  @GetMapping("/edit/{id}")
  public String showEditUserPage(@PathVariable Long id, Model model) {
      User user = userService.findUserById(id); // Assuming you have a UserService to fetch user by ID
      model.addAttribute("user", user);
      return "editUser"; // Ensure this matches the name of your Thymeleaf template (e.g., editUser.html)
  }
  @PostMapping("/update")
  public String updateUser(@ModelAttribute User user, Model model) {
      userService.updateUser(user.getId(), user); // Assuming you have a UserService to update users
      model.addAttribute("users", userService.findAllUsers());
      model.addAttribute("successMessage", "User updated successfully!");
      return "home"; // Ensure this matches the name of your Thymeleaf template (e.g., home.html)
  }
  
//  @PutMapping("/edit/{id}")
//  @PreAuthorize("hasRole('ADMIN')")
//  public ResponseEntity<Void>editUser(@PathVariable Long id,String  userName) {
//	  User us=new User();
//	  us.setUsername(userName);
//      userService.updateUser(id, us);
//      return ResponseEntity.noContent().build();
//  } 
//  
//  
//  @PostMapping("/update")
//  @PreAuthorize("hasRole('ADMIN')")
//  public String updateUser(@ModelAttribute User user, Model model) {
//      userService.updateUser(user.getId(), user); // Assuming you have a UserService to update users
//      model.addAttribute("users", userService.findAllUsers());
//      model.addAttribute("successMessage", "User updated successfully!");
//      return "home"; // Ensure this matches the name of your Thymeleaf template (e.g., home.html)
//  }
  
}
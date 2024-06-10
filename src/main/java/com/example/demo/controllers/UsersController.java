package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.User;
import com.example.demo.models.UserRepository;

import jakarta.servlet.http.HttpServletResponse;


@Controller
public class UsersController {
    
    @Autowired
    private UserRepository UserRepo;

    @GetMapping("/users/view")
    public String getAllUsers(Model model) {
        System.out.println("Getting all users");
        List<User> users = UserRepo.findAll();
        model.addAttribute("us", users);
        return "showAll"; 
    }
    
    @PostMapping("/users/add")
    public String addRectangle(@RequestParam Map<String,String> newUser, HttpServletResponse response) {
        System.out.println("ADD rectangle");
        String newName = newUser.get("name");
        int newWidth = Integer.parseInt(newUser.get("width"));
        int newHeight = Integer.parseInt(newUser.get("height"));
        String newColor = newUser.get("color");
        UserRepo.save(new User(newName, newWidth, newHeight, newColor));
        response.setStatus(201);
        return "addedUser";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") int userId, HttpServletResponse response) {
        System.out.println("DELETE user");
        if (UserRepo.existsById(userId)) {
            UserRepo.deleteById(userId);
        } 
        return "redirect:/users/view"; 
    }

    @GetMapping("/users/display/{id}")
    public String displayUser(@PathVariable("id") int userId, Model model) {
        User user = UserRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + userId));
        model.addAttribute("user", user);
        return "showRectangle";
    }
    
    @PostMapping("/users/update")
    public String updateUser(@RequestParam("id") int id, @RequestParam("name") String name,
                             @RequestParam("width") int width, @RequestParam("height") int height,
                             @RequestParam("color") String color, Model model) {
        User user = UserRepo.findById(id).orElse(null);
        if (user != null) {
            user.setName(name);
            user.setWidth(width);
            user.setHeight(height);
            user.setColor(color);
            UserRepo.save(user);
        }
        model.addAttribute("user", user);
        return "showRectangle";
    }
}


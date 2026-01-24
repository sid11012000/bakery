package com.example.bakery.controllers;


import com.example.bakery.component.Cart;
import com.example.bakery.repository.CategoryItemRepository;
import com.example.bakery.repository.CategoryRepository;
import com.example.bakery.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserHomeController {

    @Autowired private CategoryRepository categoryRepo;
    @Autowired private CategoryItemRepository itemRepo;
    @Autowired private SubCategoryRepository subCategoryRepo; // Inject this
    
    // Inject Cart here too
    @Autowired Cart cart;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("subCategories", subCategoryRepo.findAll()); // Add this
        model.addAttribute("items", itemRepo.findAll());
        
        // Add Cart Count for the Navbar
        model.addAttribute("cartCount", cart.getItems().size());
        return "index";
    }
}
package com.example.bakery.controllers;

import com.example.bakery.entity.Category;
import com.example.bakery.entity.CategoryItem;
import com.example.bakery.entity.SubCategory;
import com.example.bakery.repository.CategoryItemRepository;
import com.example.bakery.repository.CategoryRepository;
import com.example.bakery.repository.SubCategoryRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.io.IOException;
@Controller
@RequestMapping("/admin/items")
public class CategoryItemController {

    @Autowired
    private CategoryItemRepository repo;
    @Autowired private CategoryRepository categoryRepo;
    @Autowired private SubCategoryRepository subCategoryRepo;
    
    // 🔹 SHOW TABLE
    @GetMapping
public String list(@RequestParam(value = "keyword", required = false) String keyword,
                   Model model,
                   HttpSession session) {

    if (session.getAttribute("adminName") == null)
        return "redirect:/adminlogin";

    List<CategoryItem> items;

    if (keyword != null && !keyword.trim().isEmpty()) {
        items = repo.search(keyword);
        model.addAttribute("keyword", keyword);
    } else {
        items = repo.findAll();
    }
    model.addAttribute("adminName", session.getAttribute("adminName"));

    model.addAttribute("items", items);
    return "item-list";
}


    // 🔹 ADD PAGE
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new CategoryItem());
        model.addAttribute("categories", categoryRepo.findAll());
        return "item-form";
    }

    // 🔹 SAVE / UPDATE
    @PostMapping("/save")
public String save(@RequestParam(required = false) Long id,
                   @RequestParam Long categoryId,
                   @RequestParam(required = false) Long subCategoryId,
                   @RequestParam String itemName,
                   @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

    CategoryItem item = (id != null)
            ? repo.findById(id).orElse(new CategoryItem())
            : new CategoryItem();

    item.setItemName(itemName);

    Category category = categoryRepo.findById(categoryId).orElseThrow();
    item.setCategory(category);

    if (subCategoryId != null) {
        SubCategory sc = subCategoryRepo.findById(subCategoryId).orElse(null);
        item.setSubCategory(sc);
    } else {
        item.setSubCategory(null);
    }

    // 隼 IMAGE SAVING LOGIC
    if (!imageFile.isEmpty()) {
        String fileName = imageFile.getOriginalFilename();
        
        // Save to specific folder (Change this path if needed)
        Path fileNameAndPath = Paths.get("src/main/resources/static/images", fileName);
        
        // Actually write the file to disk
        Files.write(fileNameAndPath, imageFile.getBytes());
        
        // Save the filename in database
        item.setImageName(fileName);
    }
    repo.save(item);
    return "redirect:/admin/items";
}

    // 🔹 EDIT
    @GetMapping("/edit/{id}")
public String edit(@PathVariable Long id, Model model) {
    CategoryItem item = repo.findById(id).orElseThrow();

    model.addAttribute("item", item);
    model.addAttribute("categories", categoryRepo.findAll()); // MISSING
    return "item-form";
}


    // 🔹 DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/admin/items";
    }
    // AJAX for subcategories
    @GetMapping("/subcategories/{id}")
    @ResponseBody
    public List<SubCategory> subByCategory(@PathVariable Long id) {
        return subCategoryRepo.findByCategoryId(id);
    }
    
    /* =====================================================
       ================= USER SIDE (NEW) ===================
       ===================================================== */

    @GetMapping("/category/{id}")
    public String userCategoryItems(@PathVariable Long id, Model model) {

        model.addAttribute("categories", categoryRepo.findAll());

        Category category = categoryRepo.findById(id).orElseThrow();
        model.addAttribute("selectedCategory", category);

        model.addAttribute("items",
                repo.findByCategoryId(id));

        return "index";
    }
}

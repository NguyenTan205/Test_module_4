package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.ICategoryService;
import com.codegym.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @Autowired
    ICategoryService categoryService;

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        PageRequest pageable = PageRequest.of(page, 5);
        Page<Product> products = productService.findAll(pageable);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("categories", categoryService.findAll());
        return "product/list";
    }

    @GetMapping("/create")
    public String formAdd(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "product/create";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("product") Product product, BindingResult result,
                       Model model) {
        if (result.hasErrors()) {

            model.addAttribute("categories", categoryService.findAll());
            return "product/create";
        }
        productService.save(product);
        return "redirect:/product";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("product") Product product, BindingResult result,
                         Model model) {
        if (result.hasErrors()) {

            model.addAttribute("categories", categoryService.findAll());
            return "product/update";
        }
        productService.save(product);
        return "redirect:/product";
    }

    @GetMapping("/update/{id}")
    public String formAdd(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.findById(id).get());
        model.addAttribute("categories", categoryService.findAll());
        return "product/update";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public String searchProduct(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Category category = null;
        if (categoryId != null) {
            category = (Category) categoryService.findById(categoryId).get();
        }

        PageRequest pageable = PageRequest.of(page, 5);
        Page<Product> products = productService.findProductByNameOrCategoryOrPrice(
                name, category, price, pageable);

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());

        return "product/list";
    }

}

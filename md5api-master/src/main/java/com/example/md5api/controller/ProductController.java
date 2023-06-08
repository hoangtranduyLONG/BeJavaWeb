package com.example.md5api.controller;

import com.example.md5api.model.Product;
import com.example.md5api.service.ICategoryService;
import com.example.md5api.service.IProductService;
import com.example.md5api.validator.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ProductValidator productValidator;

    @GetMapping("/list")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> list = (List<Product>) productService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Product> getCategory(@PathVariable("id") Long id) {
        Optional<Product> product = productService.findById(id);
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@Valid @RequestBody Product product, BindingResult bindingResult) {
        productValidator.validate(product, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Product product1 = productService.save(new Product(product.getName(), product.getPrice(), product.getDescription(), product.getCategory()));
        return new ResponseEntity<>(product1, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Product> editCategory(@PathVariable("id") Long id, @RequestBody Product product) {
        Product product1 = productService.findById(id).get();
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        product1.setDescription(product.getDescription());
        product1.setCategory(product.getCategory());
        return new ResponseEntity<>(productService.save(product1), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

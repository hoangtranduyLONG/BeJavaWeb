package com.example.md5api.controller;

import com.example.md5api.model.Category;
import com.example.md5api.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAllCat(){
        List<Category> list = (List<Category>) categoryService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long id){
        Optional<Category> category = categoryService.findById(id);
        return new ResponseEntity<>(category.get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        Category category1 = categoryService.save(new Category(category.getName()));
        return new ResponseEntity<>(category1, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Category> editCategory(@PathVariable("id") Long id, @RequestBody Category category){
        Category category1 = categoryService.findById(id).get();
        category1.setName(category.getName());
        return new ResponseEntity<>(categoryService.save(category1),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id){
        categoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

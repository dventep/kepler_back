package com.kepler_apiweb.keplerapi.controller;

import com.kepler_apiweb.keplerapi.exception.ResourceExist;
import com.kepler_apiweb.keplerapi.exception.ResourceNotFoundException;
import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kepler/category")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @PostMapping("/")
    public ResponseEntity<String> createCategory(@RequestBody CategoryModel category) {
        Boolean categoryExist = categoryService.getCategoryById(category.get_id()).isPresent();
        if (categoryExist == true) {
            int nextIdInt = categoryService.getNextId();
            throw new ResourceExist(String.format("La categoría con iD %d ya existe, puedes usar el iD %d.",
                    category.get_id(),
                    nextIdInt));
        }
        Boolean categoryNameExist = categoryService.getCategoryByName(category.getName()).isPresent();
        if (categoryNameExist == true) {
            throw new ResourceExist(String.format("La categoría con nombre %s ya existe.",
                    category.getName()));
        }
        categoryService.saveCategory(category);
        return new ResponseEntity<String>(categoryService.saveCategory(category), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryModel>> showCategory() {
        return new ResponseEntity<List<CategoryModel>> (categoryService.listCategory(),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> filterCategoryById(@PathVariable int id) {
        CategoryModel category = categoryService.getCategoryById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró la categoría con el Id %s.",id)));
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategoryById(@PathVariable int id, @RequestBody CategoryModel detailsCategory) {
        CategoryModel category = categoryService.getCategoryById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró la categoría con el Id %s.", id)));
        if (detailsCategory.getName() != null && !detailsCategory.getName().isEmpty()) {
            category.setName(detailsCategory.getName());
        }
        if (detailsCategory.getDescription() != null && !detailsCategory.getDescription().isEmpty()) {
            category.setDescription(detailsCategory.getDescription());
        }
        return new ResponseEntity<String>(categoryService.updateCategory(category), HttpStatus.OK);
    }
}

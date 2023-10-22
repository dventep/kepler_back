package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.CategoryModel;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    String saveCategory(CategoryModel category);
    List<CategoryModel> listCategory();
    Optional<CategoryModel> getCategoryById(String categoryId);
    String updateCategory(CategoryModel category);
}

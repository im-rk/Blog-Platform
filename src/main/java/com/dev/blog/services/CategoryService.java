package com.dev.blog.services;

import com.dev.blog.domain.entities.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> listCategories();
}

package com.codegym.service;

import com.codegym.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProductService<T> {
    T save(T entity);

    void deleteById(Long id);

    Optional<T> findById(Long id);

    Page<T> findAll(Pageable pageable);

    Page<T> findProductByNameOrCategoryOrPrice(String name, Category categoryId, Double price, Pageable pageable);
}

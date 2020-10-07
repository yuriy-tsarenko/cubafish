package com.cubafish.repository;

import com.cubafish.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findProductById(Long id);

    List<Product> findByProductCategory(String category);

    List<Product> findByProductSubCategory(String subCategory);

    List<Product> findByProductBrand(String brand);

    List<Product> findByProductBrandAndProductSubCategory(String brand, String subcategory);

    List<Product> findByTypeOfPurpose(String typeOfPurpose);

    List<Product> findByDescription(String description);
}

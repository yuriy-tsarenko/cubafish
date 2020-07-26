package com.cubafish.repository;

import com.cubafish.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @NonNull
    Optional<Product> findById(@NonNull Long id);

    List<Product> findByProductCategory(String category);

    List<Product> findByProductSubCategory(String subCategory);

    List<Product> findByProductBrand(String brand);

    List<Product> findByProductBrandAndProductSubCategory(String brand, String subcategory);

    List<Product> findByTypeOfPurpose(String typeOfPurpose);
}

package com.cubafish.controller.guest;

import com.cubafish.dto.ProductDto;
import com.cubafish.repository.ProductRepository;
import com.cubafish.service.ProductService;
import com.cubafish.utils.CustomRequestBody;
import com.cubafish.utils.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(ProductControllerGuest.BASE_PATH)
@RequiredArgsConstructor
public class ProductControllerGuest {

    public static final String BASE_PATH = "/guest/products";
    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping()
    public ModelAndView method() {
        return new ModelAndView("redirect:" + "index.html");
    }

    @GetMapping("/all")
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @GetMapping("/get_menu_categories")
    public List<CustomResponseBody> getUniqueFromService() {
        return productService.findUniqueCategories();
    }

    @PostMapping("/get_id")
    public CustomResponseBody findMaximalId(@RequestBody CustomRequestBody customRequestBody) {
        return productService.findMaximalIdValue(customRequestBody);
    }

    @PostMapping("/get_sub_categories")
    public List<CustomResponseBody> getProductCategoriesByCategoryNameFromService(
            @RequestBody CustomRequestBody customRequestBody) {
        List<CustomResponseBody> subCategoriesFromCategories =
                productService.findUniqueSubCategoriesFromCategories(customRequestBody);
        return subCategoriesFromCategories;
    }

    @PostMapping("/get_product_brands")
    public List<CustomResponseBody> getProductBrandsBySubCategoryFromService(
            @RequestBody CustomRequestBody customRequestBody) {
        List<CustomResponseBody> brandsFromSubCategories =
                productService.findUniqueProductBrandsFromSubCategories(customRequestBody);
        return brandsFromSubCategories;
    }

    @PostMapping("/sorted_by_category")
    public List<ProductDto> findByProductCategory(@RequestBody CustomRequestBody customRequestBody) {
        return productService.findByProductCategory(customRequestBody);
    }

    @PostMapping("/sorted_by_sub_category")
    public List<ProductDto> findByProductSubCategory(@RequestBody CustomRequestBody customRequestBody) {
        return productService.findByProductSubCategory(customRequestBody);
    }

    @PostMapping("/sorted_by_brand")
    public List<ProductDto> findByProductBrandAndSubCategory(@RequestBody CustomRequestBody customRequestBody) {
        return productService.findByProductBrandAndProductSubCategory(customRequestBody);
    }

    @PostMapping("/sorted_by_type_of_purpose")
    public List<ProductDto> findProductsByTypeOfPurpose(@RequestBody CustomRequestBody customRequestBody) {
        return productService.findProductsByTypeOfPurpose(customRequestBody);
    }

    @PostMapping("/sorted_by_description")
    public List<ProductDto> findByProductDescription(@RequestBody CustomRequestBody customRequestBody) {
        return productService.findByProductDescription(customRequestBody);
    }
}
package com.cubafish.controller.super_admin;

import com.cubafish.dto.ProductDto;
import com.cubafish.entity.Product;
import com.cubafish.repository.ProductRepository;
import com.cubafish.service.ProductService;
import com.cubafish.utils.CustomRequestBody;
import com.cubafish.utils.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(ProductController.BASE_PATH)
@RequiredArgsConstructor
public class ProductController {

    @Value("${upload.path}")
    private String uploadPath;

    public static final String BASE_PATH = "/super_admin_auth/products";
    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping()
    public ModelAndView method() {
        return new ModelAndView("redirect:" + "super_admin.html");
    }

    @GetMapping("/all")
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @GetMapping("/get_menu_categories")
    public List<CustomResponseBody> getUniqueFromService() {
        return productService.findUniqueCategories();
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

    @PostMapping("/create")
    public CustomResponseBody create(
            HttpServletRequest request,
            @RequestParam(required = false, name = "productCategory") String productCategory,
            @RequestParam(required = false, name = "productSubCategory") String productSubCategory,
            @RequestParam(required = false, name = "productBrand") String productBrand,
            @RequestParam(required = false, name = "typeOfPurpose") String typeOfPurpose,
            @RequestParam(required = false, name = "description") String description,
            @RequestParam(required = false, name = "specification") String specification,
            @RequestParam(required = false, name = "totalAmount") String totalAmount,
            @RequestParam(required = false, name = "productPrice") String productPrice,
            @RequestParam(required = false, name = "file") MultipartFile file) {
        String uploadPathToDirectory = request.getServletContext().getRealPath(uploadPath);
        String status = productService.productDataValidation(productCategory, productSubCategory, productBrand,
                typeOfPurpose, description, specification, totalAmount, productPrice, file);
        if (status.equals("success")) {
            Map<String, Object> responseFromImageLoader = productService.imageLoader(file, uploadPathToDirectory);
            ProductDto dtoWithReceivedData = (ProductDto) responseFromImageLoader.get("productDto");
            status = (String) responseFromImageLoader.get("status");
            if (status.equals("success")) {
                Map<String, Object> responseFromSetterTextAndNumericData = productService
                        .setTextAndNumericDataBeforeCreate(productCategory, productSubCategory, productBrand,
                                typeOfPurpose, description, specification,
                                totalAmount, productPrice, dtoWithReceivedData);
                dtoWithReceivedData = (ProductDto) responseFromSetterTextAndNumericData.get("productDto");
                status = (String) responseFromSetterTextAndNumericData.get("status");
                if (status.equals("success")) {
                    productRepository.save(productService.create(dtoWithReceivedData));
                }
            }
        }
        return new CustomResponseBody(1L, "data load", status, "no data");
    }

    @PostMapping("/delete_product_image")
    public CustomResponseBody delete(
            HttpServletRequest request,
            @RequestParam String path) {
        String absolutePathToUploadDir = request.getServletContext().getRealPath(uploadPath);
        String status = productService.deleteFileIfExists(path, absolutePathToUploadDir);
        return new CustomResponseBody(1L, "image deleting status", status, "no data");
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Product product) {
        productRepository.delete(product);
    }

    @PostMapping("/update")
    public CustomResponseBody update(
            HttpServletRequest request,
            @RequestParam(required = false, name = "id") Long id,
            @RequestParam(required = false, name = "productCategory") String productCategory,
            @RequestParam(required = false, name = "productSubCategory") String productSubCategory,
            @RequestParam(required = false, name = "productBrand") String productBrand,
            @RequestParam(required = false, name = "typeOfPurpose") String typeOfPurpose,
            @RequestParam(required = false, name = "description") String description,
            @RequestParam(required = false, name = "specification") String specification,
            @RequestParam(required = false, name = "totalAmount") String totalAmount,
            @RequestParam(required = false, name = "productPrice") String productPrice,
            @RequestParam(required = false, name = "file") MultipartFile file) {
        String uploadPathToDirectory = request.getServletContext().getRealPath(uploadPath);
        System.out.println(uploadPathToDirectory);
        String status = productService.productDataValidation(productCategory, productSubCategory, productBrand,
                typeOfPurpose, description, specification, totalAmount, productPrice, file);
        if (status.equals("success")) {
            Map<String, Object> responseFromDb = productService.findById(id);
            Product productFromDb = (Product) responseFromDb.get("product");
            status = (String) responseFromDb.get("status");
            if (status.equals("success") && !Objects.requireNonNull(file.getOriginalFilename()).equals("no_image")) {
                status = productService.deleteFileIfExists(productFromDb.getProductImageName(), uploadPathToDirectory);
            }
            if (status.equals("File is not find") || status.equals("File not deleted, no path to file")
                    || status.equals("success")) {
                Map<String, Object> responseFromImageLoader = productService.imageLoader(file, uploadPathToDirectory);
                ProductDto dtoWithReceivedData = (ProductDto) responseFromImageLoader.get("productDto");
                status = (String) responseFromImageLoader.get("status");
                String fileName = (String) responseFromImageLoader.get("fileName");

                if (status.equals("success") || status.equals("try to set the old image")) {
                    Map<String, Object> responseFromUpdateExitingProduct = productService
                            .compensationOfMissingData(id, productCategory, productSubCategory,
                                    productBrand, typeOfPurpose, description, specification,
                                    totalAmount, productPrice, productFromDb, dtoWithReceivedData);
                    dtoWithReceivedData = (ProductDto) responseFromUpdateExitingProduct.get("productDto");
                    status = (String) responseFromUpdateExitingProduct.get("status");
                    if (status.equals("success")) {
                        BeanUtils.copyProperties(productService.create(dtoWithReceivedData), productFromDb);
                        productRepository.save(productFromDb);
                        status = "success";
                    }
                }
            }
        }
        return new CustomResponseBody(1L, "update status", status, "no data");
    }
}
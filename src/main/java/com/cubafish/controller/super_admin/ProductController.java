package com.cubafish.controller.super_admin;

import com.cubafish.dto.ProductDto;
import com.cubafish.entity.Product;
import com.cubafish.repository.ProductRepository;
import com.cubafish.service.ProductService;
import com.cubafish.utils.CustomRequestBody;
import com.cubafish.utils.CustomResponseBody;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(ProductController.BASE_PATH)
@RequiredArgsConstructor
public class ProductController {

    private static final Logger log = Logger.getLogger(ProductController.class);
    public static final String BASE_PATH = "/super_admin_auth/products";

    private final ProductService productService;
    private final ProductRepository productRepository;

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
            @RequestParam(required = false, name = "file") MultipartFile file,
            @RequestParam(required = false, name = "fileRightSide") MultipartFile fileRightSide,
            @RequestParam(required = false, name = "fileLeftSide") MultipartFile fileLeftSide,
            @RequestParam(required = false, name = "fileBackSide") MultipartFile fileBackSide) {
        ServletContext servletContext = request.getServletContext();
        int updateStepForImageLoader = 1;
        Map<String, Object> responseFromProductDataValidation = productService.productDataValidation(productCategory,
                productSubCategory, productBrand, typeOfPurpose, description, specification, totalAmount,
                productPrice, file, fileRightSide, fileLeftSide, fileBackSide);
        String status = (String) responseFromProductDataValidation.get("status");
        ProductDto dto = (ProductDto) responseFromProductDataValidation.get("productDto");
        if (status.equals("success")) {
            Map<String, Object> responseFromImageLoader = productService
                    .imageLoader(file, servletContext, dto, updateStepForImageLoader);
            ProductDto dtoWithFirstFile = (ProductDto) responseFromImageLoader.get("productDto");
            status = (String) responseFromImageLoader.get("status");
            updateStepForImageLoader = (int) responseFromImageLoader.get("update step");
            if (status.equals("try to set the old image")) {
                status = "Upload the image please";
            }
            if (status.equals("success") || status.equals("not required param, setted null to the field")) {
                Map<String, Object> responseImageLoaderTryToUploadSecondFile = productService
                        .imageLoader(fileRightSide, servletContext, dtoWithFirstFile, updateStepForImageLoader);
                ProductDto dtoWithSecondFile = (ProductDto) responseImageLoaderTryToUploadSecondFile.get("productDto");
                status = (String) responseImageLoaderTryToUploadSecondFile.get("status");
                updateStepForImageLoader = (int) responseImageLoaderTryToUploadSecondFile.get("update step");
                if (status.equals("success") || status.equals("not required param, setted null to the field")) {
                    Map<String, Object> responseImageLoaderTryToUploadThirdFile = productService
                            .imageLoader(fileLeftSide, servletContext, dtoWithSecondFile, updateStepForImageLoader);
                    ProductDto dtoWithThirdFile =
                            (ProductDto) responseImageLoaderTryToUploadThirdFile.get("productDto");
                    status = (String) responseImageLoaderTryToUploadThirdFile.get("status");
                    updateStepForImageLoader = (int) responseImageLoaderTryToUploadThirdFile.get("update step");
                    if (status.equals("success") || status.equals("not required param, setted null to the field")) {
                        Map<String, Object> responseImageLoaderTryToUploadFourthFile = productService
                                .imageLoader(fileBackSide, servletContext, dtoWithThirdFile, updateStepForImageLoader);
                        ProductDto dtoWithFourthFile =
                                (ProductDto) responseImageLoaderTryToUploadFourthFile.get("productDto");
                        status = (String) responseImageLoaderTryToUploadFourthFile.get("status");
                        if (status.equals("success") || status.equals("not required param, setted null to the field")) {

                            Map<String, Object> responseFromSetterTextAndNumericData = productService
                                    .setTextAndNumericDataBeforeCreate(productCategory, productSubCategory,
                                            productBrand, typeOfPurpose, description, specification,
                                            totalAmount, productPrice, dtoWithFourthFile);
                            ProductDto dtoWithAllData =
                                    (ProductDto) responseFromSetterTextAndNumericData.get("productDto");
                            status = (String) responseFromSetterTextAndNumericData.get("status");

                            if (status.equals("success")) {
                                productRepository.save(productService.create(dtoWithAllData));
                            }
                        }
                    }
                }
            }
        }
        log.info("created product status: " + status);
        return new CustomResponseBody("data load", status, "no data");
    }

    @PostMapping("/delete_product_image")
    public CustomResponseBody delete(
            HttpServletRequest request,
            @RequestParam(required = false, name = "pathImage") String pathImage,
            @RequestParam(required = false, name = "pathImageRight") String pathImageRight,
            @RequestParam(required = false, name = "pathImageLeft") String pathImageLeft,
            @RequestParam(required = false, name = "pathImageBack") String pathImageBack
    ) {
        ServletContext absolutePathToUploadDir = request.getServletContext();
        String status = null;
        int step = 0;
        if (!pathImage.equals("no_path")) {
            status = productService.deleteFileIfExists(absolutePathToUploadDir, pathImage);
            step++;
        }
        if (!pathImageRight.equals("no_path")) {
            status = productService.deleteFileIfExists(absolutePathToUploadDir, pathImageRight);
            step++;
        }
        if (!pathImageLeft.equals("no_path")) {
            status = productService.deleteFileIfExists(absolutePathToUploadDir, pathImageLeft);
            step++;
        }
        if (!pathImageBack.equals("no_path")) {
            status = productService.deleteFileIfExists(absolutePathToUploadDir, pathImageBack);
            step++;
        }
        log.info("image deleting status: " + status + " step: " + step);
        return new CustomResponseBody("image deleting status", status, "no data");
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
            @RequestParam(required = false, name = "file") MultipartFile file,
            @RequestParam(required = false, name = "fileRightSide") MultipartFile fileRightSide,
            @RequestParam(required = false, name = "fileLeftSide") MultipartFile fileLeftSide,
            @RequestParam(required = false, name = "fileBackSide") MultipartFile fileBackSide) {
        ServletContext servletContext = request.getServletContext();
        int updateStepForImageLoader = 1;
        Map<String, Object> responseFromProductDataValidation = productService.productDataValidation(productCategory,
                productSubCategory, productBrand, typeOfPurpose, description, specification, totalAmount,
                productPrice, file, fileRightSide, fileLeftSide, fileBackSide);
        String status = (String) responseFromProductDataValidation.get("status");
        ProductDto dto = (ProductDto) responseFromProductDataValidation.get("productDto");
        if (status.equals("success")) {
            Map<String, Object> responseFromDb = productService.findById(id);
            Product productFromDb = (Product) responseFromDb.get("product");
            status = (String) responseFromDb.get("status");

            if (status.equals("success")) {
                Map<String, Object> responseFromImageLoader = productService.imageLoader(file, servletContext,
                        dto, updateStepForImageLoader);
                ProductDto dtoWithFirstFile = (ProductDto) responseFromImageLoader.get("productDto");
                status = (String) responseFromImageLoader.get("status");
                updateStepForImageLoader = (int) responseFromImageLoader.get("update step");
                if ((status.equals("success")
                        && !Objects.requireNonNull(file.getOriginalFilename()).equals("no_image"))) {
                    status = productService.deleteFileIfExists(servletContext, productFromDb.getProductImageName());
                }

                if ((status.equals("success") || status.equals("File is not find")
                        || status.equals("File not deleted, no path to file")
                        || status.equals("not required param, setted null to the field"))
                        || Objects.requireNonNull(fileRightSide.getOriginalFilename()).equals("no_image")) {
                    Map<String, Object> responseImageLoaderTryToUploadSecondFile = productService
                            .imageLoader(fileRightSide, servletContext, dtoWithFirstFile, updateStepForImageLoader);
                    ProductDto dtoWithSecondFile =
                            (ProductDto) responseImageLoaderTryToUploadSecondFile.get("productDto");
                    status = (String) responseImageLoaderTryToUploadSecondFile.get("status");
                    updateStepForImageLoader = (int) responseImageLoaderTryToUploadSecondFile.get("update step");
                    if ((status.equals("success")
                            && !Objects.requireNonNull(fileRightSide.getOriginalFilename()).equals("no_image"))) {
                        status = productService
                                .deleteFileIfExists(servletContext, productFromDb.getProductImageRightName());
                    }

                    if ((status.equals("success") || status.equals("File is not find")
                            || status.equals("File not deleted, no path to file")
                            || status.equals("not required param, setted null to the field"))
                            || Objects.requireNonNull(fileLeftSide.getOriginalFilename()).equals("no_image")) {
                        Map<String, Object> responseImageLoaderTryToUploadThirdFile = productService
                                .imageLoader(fileLeftSide, servletContext, dtoWithSecondFile, updateStepForImageLoader);
                        ProductDto dtoWithThirdFile =
                                (ProductDto) responseImageLoaderTryToUploadThirdFile.get("productDto");
                        status = (String) responseImageLoaderTryToUploadThirdFile.get("status");
                        updateStepForImageLoader = (int) responseImageLoaderTryToUploadThirdFile.get("update step");
                        if ((status.equals("success")
                                && !Objects.requireNonNull(fileLeftSide.getOriginalFilename()).equals("no_image"))) {
                            status = productService
                                    .deleteFileIfExists(servletContext, productFromDb.getProductImageLeftName());
                        }

                        if ((status.equals("success") || status.equals("File is not find")
                                || status.equals("File not deleted, no path to file")
                                || status.equals("not required param, setted null to the field"))
                                || Objects.requireNonNull(fileBackSide.getOriginalFilename()).equals("no_image")) {
                            Map<String, Object> responseImageLoaderTryToUploadFourthFile =
                                    productService.imageLoader(fileBackSide, servletContext,
                                            dtoWithThirdFile, updateStepForImageLoader);
                            ProductDto dtoWithFourthFile =
                                    (ProductDto) responseImageLoaderTryToUploadFourthFile.get("productDto");
                            status = (String) responseImageLoaderTryToUploadFourthFile.get("status");

                            if ((status.equals("success") && !Objects.requireNonNull(fileBackSide.getOriginalFilename())
                                    .equals("no_image"))) {
                                status = productService
                                        .deleteFileIfExists(servletContext, productFromDb.getProductImageBackName());
                            }

                            if (status.equals("success") || status.equals("try to set the old image")
                                    || status.equals("not required param, setted null to the field")
                                    || status.equals("File not deleted, no path to file")) {
                                Map<String, Object> responseFromUpdateExitingProduct = productService
                                        .compensationOfMissingData(id, productCategory, productSubCategory,
                                                productBrand, typeOfPurpose, description, specification,
                                                totalAmount, productPrice, productFromDb, dtoWithFourthFile);
                                ProductDto dtoWithAllData =
                                        (ProductDto) responseFromUpdateExitingProduct.get("productDto");
                                status = (String) responseFromUpdateExitingProduct.get("status");
                                if (status.equals("success")) {
                                    BeanUtils.copyProperties(productService.create(dtoWithAllData), productFromDb);
                                    productRepository.save(productFromDb);
                                    status = "success";
                                }
                            }
                        }
                    }
                }
            }
        }
        log.info("update status: " + status + " updateStepForImageLoader: " + updateStepForImageLoader);
        return new CustomResponseBody("update status", status, "no data");
    }
}
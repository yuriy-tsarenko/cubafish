package com.cubafish.service;

import com.cubafish.dto.ProductDto;
import com.cubafish.entity.Product;
import com.cubafish.mapper.ProductMapper;
import com.cubafish.repository.ProductRepository;
import com.cubafish.utils.CustomRequestBody;
import com.cubafish.utils.CustomResponseBody;
import com.cubafish.utils.PathFinder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Data
public class ProductService {

    @Value("${file.size}")
    private Integer fileSize;

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final PathFinder pathFinder;
    private String productSubCategory;


    public List<ProductDto> findAll() {
        List<ProductDto> productDtos = productMapper.mapEntitiesToDtos(productRepository.findAll());
        productDtos.sort(new SortProductsById());
        return productDtos;
    }

    public Map<String, Object> findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        Product exiting = new Product();
        return product.map(value -> Map.of("product", value, "status", "success")).orElseGet(() ->
                Map.of("object", exiting, "status", "No value found for editing"));
    }

    public List<ProductDto> findByProductCategory(CustomRequestBody customRequestBody) {
        List<ProductDto> productDto = productMapper.mapEntitiesToDtos(productRepository
                .findByProductCategory(customRequestBody.getCommunicationKey()));
        productDto.sort(new SortProductsById());
        return productDto;
    }

    public List<ProductDto> findByProductSubCategory(CustomRequestBody customRequestBody) {
        productSubCategory = customRequestBody.getCommunicationKey();
        List<ProductDto> productDto = productMapper.mapEntitiesToDtos(productRepository
                .findByProductSubCategory(productSubCategory));
        productDto.sort(new SortProductsById());
        return productDto;
    }

    public List<ProductDto> findByProductBrandAndProductSubCategory(CustomRequestBody customRequestBody) {
        List<ProductDto> productDto = productMapper.mapEntitiesToDtos(productRepository
                .findByProductBrandAndProductSubCategory(customRequestBody.getCommunicationKey(), productSubCategory));
        productDto.sort(new SortProductsById());
        return productDto;
    }

    public List<ProductDto> findProductsByTypeOfPurpose(CustomRequestBody customRequestBody) {
        List<ProductDto> productDto = productMapper.mapEntitiesToDtos(productRepository
                .findByTypeOfPurpose(customRequestBody.getCommunicationKey()));
        productDto.sort(new SortProductsById());
        return productDto;
    }

    public List<ProductDto> findByProductBrandAll(CustomRequestBody customRequestBody) {
        List<ProductDto> productDto = productMapper.mapEntitiesToDtos(productRepository
                .findByProductBrand(customRequestBody.getCommunicationKey()));
        productDto.sort(new SortProductsById());
        return productDto;
    }

    public List<ProductDto> findByProductDescription(CustomRequestBody customRequestBody) {
        List<ProductDto> productDto = productMapper.mapEntitiesToDtos(productRepository
                .findByDescription(customRequestBody.getCommunicationKey()));
        return productDto;
    }

    public CustomResponseBody findMaximalIdValue(CustomRequestBody customRequestBody) {
        if (customRequestBody.getCommunicationKey().equals("give me the argument for basket engine")) {
            List<ProductDto> productDtos = productMapper.mapEntitiesToDtos(productRepository.findAll());
            Long maxIdValue = null;
            try {
                maxIdValue = productDtos.get(productDtos.size() - 1).getId();
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return new CustomResponseBody(1L, "required argument", "success", String.valueOf(maxIdValue), "step", 1);
        }
        return new CustomResponseBody(1L, "required argument",
                "did not accept the correct key", null, "step", 1);
    }


    public Product create(ProductDto productDto) {
        return productMapper.mapDtoToEntity(productDto);
    }

    public List<CustomResponseBody> findUniqueCategories() {
        List<ProductDto> productDtos = productMapper
                .mapEntitiesToDtos(productRepository.findAll());
        return getUniqueCategories(productDtos);
    }

    public List<CustomResponseBody> findUniqueSubCategoriesFromCategories(CustomRequestBody customRequestBody) {
        List<ProductDto> productDtos = findByProductCategory(customRequestBody);
        return getUniqueSubCategories(productDtos);
    }

    public List<CustomResponseBody> findUniqueProductBrandsFromSubCategories(CustomRequestBody customRequestBody) {
        List<ProductDto> productDtos = findByProductSubCategory(customRequestBody);
        return getUniqueProductBrands(productDtos);
    }

    public Map<String, Object> productDataValidation(String productCategory, String productSubCategory,
                                                     String productBrand, String typeOfPurpose, String description,
                                                     String specification, String totalAmount, String productPrice,
                                                     MultipartFile file, MultipartFile fileRightSide,
                                                     MultipartFile fileLeftSide, MultipartFile fileBackSide) {
        ProductDto productDto = new ProductDto();
        if ((file == null) || (fileRightSide == null) || (fileLeftSide == null) || (fileBackSide == null)) {
            return Map.of("productDto", productDto, "status", "the application did not accept any product files");
        } else if ((file.getSize() > fileSize) || (fileRightSide.getSize() > fileSize)
                || (fileLeftSide.getSize() > fileSize) || (fileBackSide.getSize() > fileSize)) {
            return Map.of("productDto", productDto, "status", "the file does not meet the requirements:"
                    + " size: less then 3.5MB, type: jpg/jpeg/png");
        } else if (productCategory == null) {
            return Map.of("productDto", productDto, "status", "the application did not accept any product category");
        } else if (productCategory.length() > 50) {
            return Map.of("productDto", productDto, "status", "the category have more than 50 characters");
        } else if (productSubCategory == null) {
            return Map.of("productDto", productDto, "status", "the application did not accept any product subcategory");
        } else if (productSubCategory.length() > 50) {
            return Map.of("productDto", productDto, "status", "the subcategory have more than 50 characters");
        } else if (productBrand == null) {
            return Map.of("productDto", productDto, "status", "the application did not accept any product brand");
        } else if (productBrand.length() > 50) {
            return Map.of("productDto", productDto, "status", "the product brand have more than 50 characters");
        } else if (typeOfPurpose == null) {
            return Map.of("productDto", productDto, "status", "the application did not accept any type of purpose");
        } else if (typeOfPurpose.length() > 50) {
            return Map.of("productDto", productDto, "status", "the type of purpose have more than 50 characters");
        } else if (description == null) {
            return Map.of("productDto", productDto, "status", "the application did not accept any product description");
        } else if (description.length() > 200) {
            return Map.of("productDto", productDto, "status", "the description have more than 200 characters");
        } else if (specification == null) {
            return Map.of("productDto", productDto, "status",
                    "the application did not accept any product specification");
        } else if (specification.length() > 1000) {
            return Map.of("productDto", productDto, "status", "the specification have more than 1000 characters");
        } else if (totalAmount == null) {
            return Map.of("productDto", productDto, "status", "the application did not accept any amount of products");
        } else if (!totalAmount.isEmpty()) {
            if (totalAmount.contains(",") || totalAmount.contains(".")) {
                return Map.of("productDto", productDto, "status", "the amount of products can not have <<,>> or <<.>>");
            } else {
                try {
                    Integer.valueOf(totalAmount);
                } catch (NumberFormatException e) {
                    return Map.of("productDto", productDto, "status",
                            "the amount of products should have a numeric value");
                }
            }
        } else if (productPrice == null) {
            return Map.of("productDto", productDto, "status", "the application did not accept any product price");
        } else if (!productPrice.isEmpty()) {
            if (productPrice.contains(",")) {
                String[] massive = productPrice.split(",");
                String validPrice = massive[0].concat(".").concat(massive[1]);
                try {
                    new BigDecimal(validPrice);
                } catch (NumberFormatException e) {
                    return Map.of("productDto", productDto, "status", "product price should have a numeric value");
                }
            } else {
                try {
                    new BigDecimal(productPrice);
                } catch (NumberFormatException e) {
                    return Map.of("productDto", productDto, "status", "product price should have a numeric value");
                }
            }
        }
        return Map.of("productDto", productDto, "status", "success");
    }

    public Map<String, Object> imageLoader(MultipartFile file,
                                           ServletContext pathFromHttpContext,
                                           ProductDto productDto, int updateStep) {

        String uuidFile = UUID.randomUUID().toString();
        Path finalUploadPath = pathFinder.getUploadPath(pathFromHttpContext);
        Path downloadPath = pathFinder.getDownloadPath();
        String fileName;
        if (!file.isEmpty() && ((Objects.requireNonNull(file.getOriginalFilename()).endsWith("jpg")
                || Objects.requireNonNull(file.getOriginalFilename()).endsWith("jpeg")
                || Objects.requireNonNull(file.getOriginalFilename()).endsWith("png"))) && file.getSize() <= fileSize) {
            boolean dir = Files.isDirectory(Paths.get(String.valueOf(finalUploadPath)));
            if (!dir) {
                try {
                    Files.createDirectories(Paths.get(String.valueOf(finalUploadPath)));
                } catch (IOException e) {
                    return Map.of("productDto", productDto, "status",
                            "IOException, can not create the directory", "update step", updateStep);
                }
            }
            fileName = uuidFile.concat("-").concat(file.getOriginalFilename());
            try {
                file.transferTo(new File(String.valueOf(finalUploadPath).concat(File.separator).concat(fileName)));
            } catch (IOException e) {
                return Map.of("productDto", productDto, "status",
                        "IOException, can not transfer file to the directory", "update step", updateStep);
            }
            String imageName = downloadPath.toString().concat(File.separator).concat(fileName);
            if (imageName.length() > 255) {
                deleteFileIfExists(pathFromHttpContext, imageName);
                return Map.of("productDto", productDto, "fileName", fileName, "status",
                        "please reduce the length of the file name", "update step", updateStep);
            } else {
                if (updateStep == 1) {
                    productDto.setProductImageName(imageName);
                    updateStep = updateStep + 1;
                } else if (updateStep == 2) {
                    updateStep = updateStep + 1;
                    productDto.setProductImageRightName(imageName);
                } else if (updateStep == 3) {
                    productDto.setProductImageLeftName(imageName);
                    updateStep = updateStep + 1;
                } else if (updateStep == 4) {
                    productDto.setProductImageBackName(imageName);
                }
            }

            return Map.of("productDto", productDto, "fileName", fileName,
                    "status", "success", "update step", updateStep);
        } else if (Objects.requireNonNull(file.getOriginalFilename()).equals("no_image")) {

            if (productDto.getProductImageRightName() == null) {
                updateStep = updateStep + 1;
                productDto.setProductImageRightName(null);
                return Map.of("productDto", productDto, "fileName", "no_image", "status",
                        "not required param, setted null to the field", "update step", updateStep);
            } else if (productDto.getProductImageLeftName() == null) {
                updateStep = updateStep + 1;
                productDto.setProductImageLeftName(null);
                return Map.of("productDto", productDto, "fileName", "no_image", "status",
                        "not required param, setted null to the field", "update step", updateStep);
            } else if (productDto.getProductImageBackName() == null) {
                updateStep = updateStep + 1;
                productDto.setProductImageBackName(null);
                return Map.of("productDto", productDto, "fileName", "no_image", "status",
                        "not required param, setted null to the field", "update step", updateStep);
            } else {
                updateStep = updateStep + 1;
                return Map.of("productDto", productDto, "fileName", "no_image", "status",
                        "try to set the old image", "update step", updateStep);
            }

        } else {
            return Map.of("productDto", productDto, "status", "the file does not meet the requirements:"
                    + " size: less then 3.5MB, type: jpg/jpeg/png", "update step", updateStep);
        }
    }

    public Map<String, Object> compensationOfMissingData(
            Long id, String productCategory, String productSubCategory, String productBrand, String typeOfPurpose,
            String description, String specification, String totalAmount, String productPrice, Product exiting,
            ProductDto productDto) {

        if (id != null) {
            productDto.setId(id);
            if (productCategory.isEmpty()) {
                productDto.setProductCategory(exiting.getProductCategory());
            } else {
                productDto.setProductCategory(productCategory.trim());
            }
            if (productSubCategory.isEmpty()) {
                productDto.setProductSubCategory(exiting.getProductSubCategory());
            } else {
                productDto.setProductSubCategory(productSubCategory.trim());
            }
            if (productBrand.isEmpty()) {
                productDto.setProductBrand(exiting.getProductBrand());
            } else {
                productDto.setProductBrand(productBrand.trim());
            }
            if (typeOfPurpose.isEmpty()) {
                productDto.setTypeOfPurpose(exiting.getTypeOfPurpose());
            } else {
                productDto.setTypeOfPurpose(typeOfPurpose.trim());
            }
            if (description.isEmpty()) {
                productDto.setDescription(exiting.getDescription());
            } else {
                productDto.setDescription(description.trim());
            }
            if (specification.isEmpty()) {
                productDto.setSpecification(exiting.getSpecification());
            } else {
                productDto.setSpecification(specification.trim());
            }
            if (totalAmount.isEmpty()) {
                productDto.setTotalAmount(exiting.getTotalAmount());
            } else {
                try {
                    Integer totalAmountConverted = Integer.valueOf(totalAmount.trim());
                    productDto.setTotalAmount(totalAmountConverted);
                } catch (NumberFormatException e) {
                    return Map.of("productDto", productDto, "status",
                            "the amount of products should have a numeric value");
                }
            }
            if (productDto.getProductImageName() == null) {
                productDto.setProductImageName(exiting.getProductImageName());
            }
            if (productDto.getProductImageRightName() == null) {
                productDto.setProductImageRightName(exiting.getProductImageRightName());
            }
            if (productDto.getProductImageLeftName() == null) {
                productDto.setProductImageLeftName(exiting.getProductImageLeftName());
            }
            if (productDto.getProductImageBackName() == null) {
                productDto.setProductImageBackName(exiting.getProductImageBackName());
            }

            if (productPrice.isEmpty()) {
                productDto.setProductPrice(exiting.getProductPrice());
            } else if (productPrice.contains(",")) {
                String[] massive = productPrice.split(",");
                String validPrice = massive[0].concat(".").concat(massive[1]);
                BigDecimal convertToBigDecimal = new BigDecimal(validPrice);
                try {
                    productDto.setProductPrice(convertToBigDecimal);
                } catch (NumberFormatException e) {
                    return Map.of("productDto", productDto, "status", "product price should have a numeric value");
                }
            } else {
                try {
                    productDto.setProductPrice(new BigDecimal(productPrice));
                } catch (NumberFormatException e) {
                    return Map.of("productDto", productDto, "status", "product price should have a numeric value");
                }
            }
            return Map.of("productDto", productDto, "status", "success");
        } else {
            return Map.of("productDto", productDto, "status", "ID of edited product not received!");
        }
    }

    public Map<String, Object> setTextAndNumericDataBeforeCreate(
            String productCategory, String productSubCategory, String productBrand, String typeOfPurpose,
            String description, String specification, String totalAmount, String productPrice, ProductDto productDto) {
        productDto.setProductCategory(productCategory.trim());
        productDto.setProductSubCategory(productSubCategory.trim());
        productDto.setProductBrand(productBrand.trim());
        productDto.setTypeOfPurpose(typeOfPurpose.trim());
        productDto.setDescription(description.trim());
        productDto.setSpecification(specification.trim());
        if (totalAmount.isEmpty()) {
            productDto.setTotalAmount(Integer.valueOf("0"));
        } else {
            try {
                Integer totalAmountConverted = Integer.valueOf(totalAmount.trim());
                productDto.setTotalAmount(totalAmountConverted);
            } catch (NumberFormatException e) {
                return Map.of("productDto", productDto,
                        "status", "the amount of products should have a numeric value");
            }
        }

        if (productPrice.isEmpty()) {
            productDto.setProductPrice(new BigDecimal("0"));
        } else if (productPrice.contains(",")) {
            String[] massive = productPrice.split(",");
            String validPrice = massive[0].concat(".").concat(massive[1]);
            try {
                BigDecimal convertToBigDecimal = new BigDecimal(validPrice);
                productDto.setProductPrice(convertToBigDecimal);
            } catch (NumberFormatException e) {
                return Map.of("productDto", productDto, "status",
                        "product price should have a numeric value");
            }
        } else {
            try {
                BigDecimal convertToBigDecimal = new BigDecimal(productPrice);
                productDto.setProductPrice(convertToBigDecimal);
            } catch (NumberFormatException e) {
                return Map.of("productDto", productDto, "status", "product price should have a numeric value");
            }
        }
        return Map.of("productDto", productDto, "status", "success");
    }

    public String deleteFileIfExists(ServletContext pathFromHttpContext, String path) {
        if (path == null) {
            path = "";
        }
        if (!path.isEmpty()) {
            try {
                if (Files.exists(Paths.get(pathFinder.getPathBeforeDelete(pathFromHttpContext, path).toString()))) {
                    Files.deleteIfExists(Paths.get(pathFinder
                            .getPathBeforeDelete(pathFromHttpContext, path).toString()));
                } else {
                    return "File is not find";
                }
                return "success";
            } catch (IOException e) {
                return "IOException";
            }
        } else {
            return "File not deleted, no path to file";
        }
    }

    public List<CustomResponseBody> getUniqueCategories(List<ProductDto> productDtoFromDb) {
        if (productDtoFromDb == null) {
            return null;
        }
        Long id = 0L;
        Set<String> uniqueItems = new HashSet<>();
        for (ProductDto productDto : productDtoFromDb) {
            uniqueItems.add(productDto.getProductCategory());
        }
        List<CustomResponseBody> list = new ArrayList<>(uniqueItems.size());
        for (String item : uniqueItems) {
            id += 1L;
            list.add(new CustomResponseBody(id, "productCategoriesForMenu", "success", item, "step", 1));
        }
        return list;
    }

    public List<CustomResponseBody> getUniqueSubCategories(List<ProductDto> productDtoFromDb) {
        if (productDtoFromDb == null) {
            return null;
        }
        Long id = 0L;
        Set<String> uniqueItems = new HashSet<>();
        for (ProductDto productDto : productDtoFromDb) {
            uniqueItems.add(productDto.getProductSubCategory());
        }
        List<CustomResponseBody> list = new ArrayList<>(uniqueItems.size());
        for (String item : uniqueItems) {
            id += 1L;
            list.add(new CustomResponseBody(id, "productSubCategoriesForMenu", "success", item, "step", 1));
        }
        return list;
    }

    public List<CustomResponseBody> getUniqueProductBrands(List<ProductDto> productDtoFromDb) {
        if (productDtoFromDb == null) {
            return null;
        }
        Long id = 0L;
        Set<String> uniqueItems = new HashSet<>();
        for (ProductDto productDto : productDtoFromDb) {
            uniqueItems.add(productDto.getProductBrand());
        }
        List<CustomResponseBody> list = new ArrayList<>(uniqueItems.size());
        for (String item : uniqueItems) {
            id += 1L;
            list.add(new CustomResponseBody(id, "productBrandsForMenu", "success", item, "step", 1));
        }
        return list;
    }

    static class SortProductsById implements Comparator<ProductDto> {
        @Override
        public int compare(ProductDto productDto, ProductDto productDto2) {
            return productDto.getId().compareTo(productDto2.getId());
        }
    }
}

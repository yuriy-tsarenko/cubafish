package com.cubafish.service;

import com.cubafish.dto.ProductDto;
import com.cubafish.entity.Product;
import com.cubafish.mapper.ProductMapper;
import com.cubafish.repository.ProductRepository;
import com.cubafish.utils.CustomRequestBody;
import com.cubafish.utils.CustomResponseBody;
import com.cubafish.utils.PathFinder;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final PathFinder pathFinder;
    private String productSubCategory;
    private final Integer fileSize = 3670016;

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
        return productDto;
    }

    public List<ProductDto> findByProductSubCategory(CustomRequestBody customRequestBody) {
        productSubCategory = customRequestBody.getCommunicationKey();
        List<ProductDto> productDto = productMapper.mapEntitiesToDtos(productRepository
                .findByProductSubCategory(productSubCategory));
        return productDto;
    }

    public List<ProductDto> findByProductBrandAndProductSubCategory(CustomRequestBody customRequestBody) {
        List<ProductDto> productDto = productMapper.mapEntitiesToDtos(productRepository
                .findByProductBrandAndProductSubCategory(customRequestBody.getCommunicationKey(), productSubCategory));
        return productDto;
    }

    public List<ProductDto> findProductsByTypeOfPurpose(CustomRequestBody customRequestBody) {
        List<ProductDto> productDto = productMapper.mapEntitiesToDtos(productRepository
                .findByTypeOfPurpose(customRequestBody.getCommunicationKey()));
        return productDto;
    }

    public List<ProductDto> findByProductBrandAll(CustomRequestBody customRequestBody) {
        List<ProductDto> productDto = productMapper.mapEntitiesToDtos(productRepository
                .findByProductBrand(customRequestBody.getCommunicationKey()));
        return productDto;
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

    public String productDataValidation(String productCategory, String productSubCategory, String productBrand,
                                        String typeOfPurpose, String description, String specification,
                                        String totalAmount, String productPrice, MultipartFile file) {

        if (file == null) {
            return "the application did not accept any product files";
        } else if (productCategory == null) {
            return "the application did not accept any product category";
        } else if (productCategory.length() > 50) {
            return "the category have more than 50 characters";
        } else if (productSubCategory == null) {
            return "the application did not accept any product subcategory";
        } else if (productSubCategory.length() > 50) {
            return "the subcategory have more than 50 characters";
        } else if (productBrand == null) {
            return "the application did not accept any product brand";
        } else if (productBrand.length() > 50) {
            return "the product brand have more than 50 characters";
        } else if (typeOfPurpose == null) {
            return "the application did not accept any type of purpose";
        } else if (typeOfPurpose.length() > 50) {
            return "the type of purpose have more than 50 characters";
        } else if (description == null) {
            return "the application did not accept any product description";
        } else if (description.length() > 100) {
            return "the description have more than 100 characters";
        } else if (specification == null) {
            return "the application did not accept any product specification";
        } else if (specification.length() > 400) {
            return "the specification have more than 400 characters";
        } else if (totalAmount == null) {
            return "the application did not accept any amount of products";
        } else if (!totalAmount.isEmpty()) {
            if (totalAmount.contains(",") || totalAmount.contains(".")) {
                return "the amount of products can not have <<,>> or <<.>>";
            } else {
                try {
                    Integer.valueOf(totalAmount);
                } catch (NumberFormatException e) {
                    return "the amount of products should have a numeric value";
                }
            }
        } else if (productPrice == null) {
            return "the application did not accept any product price";
        } else if (!productPrice.isEmpty()) {
            if (productPrice.contains(",")) {
                String[] massive = productPrice.split(",");
                String validPrice = massive[0].concat(".").concat(massive[1]);
                try {
                    new BigDecimal(validPrice);
                } catch (NumberFormatException e) {
                    return "product price should have a numeric value";
                }
            } else {
                try {
                    new BigDecimal(productPrice);
                } catch (NumberFormatException e) {
                    return "product price should have a numeric value";
                }
            }
        }
        return "success";
    }

    public Map<String, Object> imageLoader(MultipartFile file) {
        ProductDto productDto = new ProductDto();
        String uuidFile = UUID.randomUUID().toString();
        Path uploadPath = pathFinder.getPath();
        Path downloadPath = pathFinder.getDownloadPath();

        if (!file.isEmpty() && ((Objects.requireNonNull(file.getOriginalFilename()).endsWith("jpg")
                || Objects.requireNonNull(file.getOriginalFilename()).endsWith("jpeg")
                || Objects.requireNonNull(file.getOriginalFilename()).endsWith("png"))) && file.getSize() <= fileSize) {
            boolean dir = Files.isDirectory(Paths.get(String.valueOf(uploadPath)));
            if (!dir) {
                try {
                    Files.createDirectories(Paths.get(String.valueOf(uploadPath)));
                } catch (IOException e) {
                    return Map.of("productDto", productDto, "status",
                            "IOException, can not create the directory");
                }
            }
            String fileName = uuidFile.concat("-").concat(file.getOriginalFilename());
            try {
                file.transferTo(new File(uploadPath.toString().concat(File.separator).concat(fileName)));
            } catch (IOException e) {
                return Map.of("productDto", productDto, "status",
                        "IOException, can not transfer file to the directory");
            }
            String productImageName = downloadPath.toString().concat(File.separator).concat(fileName);
            if (productImageName.length() > 300) {
                deleteFileIfExists(productImageName);
                return Map.of("productDto", productDto, "fileName", fileName, "status",
                        "please reduce the length of the file name");
            } else {
                productDto.setProductImageName(productImageName);
            }
            return Map.of("productDto", productDto, "fileName", fileName, "status", "success");
        } else if (Objects.requireNonNull(file.getOriginalFilename()).equals("no_image")) {
            return Map.of("productDto", productDto, "fileName", "no_image", "status",
                    "try to set the old image");
        } else {
            return Map.of("productDto", productDto, "status", "the file does not meet the requirements:"
                    + " size: less then 3.5MB, type: jpg/jpeg/png");
        }
    }

    public Map<String, Object> compensationOfMissingData(Long id, String productCategory, String productSubCategory,
                                                         String productBrand, String typeOfPurpose, String description,
                                                         String specification, String totalAmount, String productPrice,
                                                         Product exiting, ProductDto productDto) {

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

    public Map<String, Object> setTextAndNumericDataBeforeCreate(String productCategory, String productSubCategory,
                                                                 String productBrand, String typeOfPurpose,
                                                                 String description, String specification,
                                                                 String totalAmount, String productPrice,
                                                                 ProductDto productDto) {
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
            BigDecimal convertToBigDecimal = new BigDecimal(validPrice);
            try {
                productDto.setProductPrice(convertToBigDecimal);
            } catch (NumberFormatException e) {
                return Map.of("productDto", productDto, "status",
                        "product price should have a numeric value");
            }
        } else {
            BigDecimal convertToBigDecimal = new BigDecimal(productPrice);
            try {
                productDto.setProductPrice(convertToBigDecimal);
            } catch (NumberFormatException e) {
                return Map.of("productDto", productDto, "status", "product price should have a numeric value");
            }
        }
        return Map.of("productDto", productDto, "status", "success");
    }

    public String deleteFileIfExists(String path) {
        if (path == null) {
            path = "";
        }
        if (!path.isEmpty()) {
            try {
                if (Files.exists(Paths.get(pathFinder.getPathBeforeDelete().toString().concat(path)))) {
                    Files.deleteIfExists(Paths.get(pathFinder.getPathBeforeDelete().toString().concat(path)));
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
            list.add(new CustomResponseBody(id, "productCategoriesForMenu", "success", item));
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
            list.add(new CustomResponseBody(id, "productSubCategoriesForMenu", "success", item));
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
            list.add(new CustomResponseBody(id, "productBrandsForMenu", "success", item));
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

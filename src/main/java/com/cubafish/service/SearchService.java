package com.cubafish.service;

import com.cubafish.dto.DictionaryDto;
import com.cubafish.entity.Product;
import com.cubafish.mapper.DictionaryMapper;
import com.cubafish.repository.DictionaryRepository;
import com.cubafish.repository.ProductRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class SearchService {

    private final ProductRepository productRepository;
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryMapper dictionaryMapper;

    public List<Product> searchEngine(String searchTag) {
        List<Product> all = productRepository.findAll();
        List<Product> searchResponse = new ArrayList<>(all.size());
        String[] splitTag = searchTag.split(" ");
        int lengthOfSearchTag = splitTag.length;
        int dictionaryStep = 1;
        int wordSelectStep = 0;

        for (Product product : all) {
            for (int i = 0; i < 1; i++) {
                String description = product.getDescription();
                if (description.toLowerCase().contains(searchTag.trim().toLowerCase())) {
                    searchResponse.add(product);
                }
                if (!searchResponse.contains(product)) {
                    String category = product.getProductCategory();
                    if (category.toLowerCase().contains(searchTag.trim().toLowerCase())) {
                        searchResponse.add(product);
                    }
                    if (!searchResponse.contains(product)) {
                        String subCategory = product.getProductSubCategory();
                        if (subCategory.toLowerCase().contains(searchTag.trim().toLowerCase())) {
                            searchResponse.add(product);
                        }
                        if (!searchResponse.contains(product)) {
                            String brand = product.getProductBrand();
                            if (brand.toLowerCase().contains(searchTag.trim().toLowerCase())) {
                                searchResponse.add(product);
                            }
                            if (!searchResponse.contains(product)) {
                                String typeOfPurpose = product.getTypeOfPurpose();
                                if (typeOfPurpose.toLowerCase().contains(searchTag.trim().toLowerCase())) {
                                    searchResponse.add(product);
                                }
                                if (!searchResponse.contains(product)) {
                                    String specification = product.getSpecification();
                                    if (specification.toLowerCase().contains(searchTag.trim().toLowerCase())) {
                                        searchResponse.add(product);
                                    }
                                }
                            }
                        }

                    }
                }
                if ((!searchResponse.contains(product))) {
                    for (; wordSelectStep < lengthOfSearchTag; wordSelectStep++) {
                        searchTag = splitTag[wordSelectStep];
                        i--;
                        dictionaryStep--;
                    }
                }

                if ((!searchResponse.contains(product)) && (dictionaryStep < 1)) {
                    List<DictionaryDto> dictionaryDtos = dictionaryMapper
                            .mapEntitiesToDtos(dictionaryRepository.findAll());
                    for (DictionaryDto item : dictionaryDtos) {
                        if (searchTag.trim().toLowerCase().equals(item.getEnWord().toLowerCase())) {
                            searchTag = item.getOriginalWord();
                        } else if (searchTag.trim().toLowerCase().equals(item.getUkWord().toLowerCase())) {
                            searchTag = item.getOriginalWord();
                        }
                    }
                    dictionaryStep++;
                }
            }
        }

        return searchResponse;
    }
}


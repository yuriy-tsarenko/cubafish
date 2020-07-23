package com.yumarket.controller;

import com.yumarket.controller.super_admin.ProductController;
import com.yumarket.dto.ProductDto;
import com.yumarket.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

//    @Mock
//    private ProductService productService;
//    @InjectMocks
//    private ProductController productController;
//
//    @Test
//    void findAllDtoShouldBeSuccessful() {
//
//        ProductDto productDto = new ProductDto(1L, "Піца ", "Піца Пеперчі ",
//                "Ковбаса Папероні, Перець консервований Халапеньйо, Оливки, Соус Барбекю, Сир Моцарела ",
//                new BigDecimal("125.0"), null);
//        ProductDto productDto1 = new ProductDto(2L, "Піца ", "Піца Тіаро ", "Шинка," +
//                " Печериці, Помідори Чері, Руккола, Сир Моцарела, Соус Альфредо ", new BigDecimal("145"), null);
//
//        List<ProductDto> expected = List.of(productDto, productDto1);
//        when(productService.findAll()).thenReturn(expected);
//        List<ProductDto> actual = productController.findAll();
//        assertEquals(expected, actual);
//    }
}
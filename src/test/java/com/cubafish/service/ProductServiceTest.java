package com.cubafish.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
//    @Mock
//    private ProductRepository productRepository;
//    @Mock
//    private ProductMapper productMapper;
//    @InjectMocks
//    private ProductService productService;
//
//
//    @Test
//    void findAllEntitiesShouldBeSuccessful() {
//        ProductDto productDto = new ProductDto(1L, "Піца ", "Піца Пеперчі ",
//                "Ковбаса Папероні, Перець консервований Халапеньйо, Оливки, Соус Барбекю, Сир Моцарела ",
//                new BigDecimal("125.0"), null);
//        ProductDto productDto1 = new ProductDto(2L, "Піца ", "Піца Тіаро ", "Шинка," +
//                " Печериці, Помідори Чері, Руккола, Сир Моцарела, Соус Альфредо ", new BigDecimal("145"), null);
//
//        List<ProductDto> expected = List.of(productDto, productDto1);
//        when(productMapper.mapEntitiesToDtos(productRepository.findAll())).thenReturn(expected);
//        List<ProductDto> actual = productService.findAll();
//        assertEquals(expected, actual);
//    }
}
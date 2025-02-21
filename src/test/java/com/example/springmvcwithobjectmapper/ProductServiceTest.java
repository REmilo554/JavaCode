package com.example.springmvcwithobjectmapper;

import com.example.springmvcwithobjectmapper.DTO.ProductDTO;
import com.example.springmvcwithobjectmapper.DTO.ProductResponseDTO;
import com.example.springmvcwithobjectmapper.Entity.Product;
import com.example.springmvcwithobjectmapper.Exceptions.ProductNotFoundException;
import com.example.springmvcwithobjectmapper.Repository.ProductRepository;
import com.example.springmvcwithobjectmapper.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductDTO testProductDTO;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .productId(1L)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(100.00))
                .quantityInStock(10)
                .build();

        testProductDTO = ProductDTO.builder()
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(100.00))
                .quantityInStock(10)
                .build();
    }

    @Test
    void getAllProducts_ReturnsListOfProductResponseDTO() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(testProduct));

        List<ProductResponseDTO> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(testProduct.getProductId(), products.get(0).getProductId());
    }

    @Test
    void getProductById_ExistingProductId_ReturnsProductResponseDTO() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        ProductResponseDTO productResponseDTO = productService.getProductById(1L);

        assertNotNull(productResponseDTO);
        assertEquals(testProduct.getProductId(), productResponseDTO.getProductId());
    }

    @Test
    void getProductById_NonExistingProductId_ThrowsProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void createProduct_ValidProductDTO_ReturnsProductResponseDTO() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductResponseDTO productResponseDTO = productService.createProduct(testProductDTO);

        assertNotNull(productResponseDTO);
        assertEquals(testProduct.getProductId(), productResponseDTO.getProductId());
    }

    @Test
    void updateProduct_ExistingProductIdAndValidProductDTO_ReturnsProductResponseDTO() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductResponseDTO productResponseDTO = productService.updateProduct(1L, testProductDTO);

        assertNotNull(productResponseDTO);
        assertEquals(testProduct.getProductId(), productResponseDTO.getProductId());
    }

    @Test
    void updateProduct_NonExistingProductId_ThrowsProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, testProductDTO));
    }

    @Test
    void deleteProduct_ExistingProductId_DeletesProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
    }

    @Test
    void deleteProduct_NonExistingProductId_ThrowsProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
    }
}

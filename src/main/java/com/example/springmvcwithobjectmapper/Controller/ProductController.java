package com.example.springmvcwithobjectmapper.Controller;

import com.example.springmvcwithobjectmapper.DTO.ProductDTO;
import com.example.springmvcwithobjectmapper.Service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllProducts() throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper
                .writeValueAsString(productService
                        .getAllProducts()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProductById(@PathVariable Long id) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper
                .writeValueAsString(productService
                        .getProductById(id)), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProduct(@Valid @RequestBody String productDTO) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper
                .writeValueAsString(productService
                        .createProduct(objectMapper
                                .readValue(productDTO, ProductDTO.class))), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @Valid @RequestBody String productDTO) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper
                .writeValueAsString(productService
                        .updateProduct(id, objectMapper
                                .readValue(productDTO, ProductDTO.class))), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

package com.spartanwrath.service;

import com.spartanwrath.model.Product;
import com.spartanwrath.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    private ImageService imageService;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Método para crear un nuevo producto
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Método para obtener todos los productos
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Método para obtener un producto por su ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    // Método para actualizar un producto
    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    // Método para eliminar un producto por su ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public void deleteAllProduct(){
        productRepository.deleteAll();
    }

    public List<Product> getProductsByIds(List<Long> productIds){
        List<Product> products = new ArrayList<>();
        for (Long productId : productIds){
            Optional<Product> optionalProduct = productRepository.findById(productId);
            optionalProduct.ifPresent(products::add);
        }
        return products;
    }
}
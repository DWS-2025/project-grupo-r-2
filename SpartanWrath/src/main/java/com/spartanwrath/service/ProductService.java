package com.spartanwrath.service;

import com.spartanwrath.dto.ProductDTO;
import com.spartanwrath.dto.ProductMapper;
import com.spartanwrath.model.Product;
import com.spartanwrath.repository.ProductRepository;
import com.spartanwrath.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Qualifier("productMapperImpl")
    @Autowired
    private ProductMapper mapper;

    @Value("${image.upload.dir}")
    private String uploadDir;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Método para crear un nuevo producto
    public Product createProduct(Product product) throws IOException {

        product = sanitizeProduct(product);
        if(product.getImagen() == null || product.getImagen().length == 0){
            byte[] defaultImageData = imageService.getDefault();
            product.setImagen(defaultImageData);
            product.setOriginalImageName(imageService.getDefaultName());
        }else {
            byte[] imageData = product.getImagen();
            product.setImagen(imageData);

        }
        return productRepository.save(product);
    }
    public ProductDTO createProductDTO(ProductDTO productDTO) throws IOException {
        Product product = toDomain(productDTO);
        product = sanitizeProduct(product);

        if (product.getImagen() == null || product.getImagen().length == 0) {
            byte[] defaultImageData = imageService.getDefault();
            product.setImagen(defaultImageData);
            product.setOriginalImageName(imageService.getDefaultName());
        } else {
            byte[] imageData = product.getImagen();
            product.setImagen(imageData);
        }

        product = productRepository.save(product);
        return toDTO(product);
    }

    // Método para obtener todos los productos
    public List<Product> getAllProductsNormal() {
        return productRepository.findAll();
    }
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return toDTOs(products);
    }
    // Método para obtener un producto por su ID

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    public Optional<ProductDTO> getProductByIdDTO(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(this::toDTO);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    public List<ProductDTO> getProductsByCategoryDTO(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return toDTOs(products);
    }
    // Método para actualizar un producto
    public void updateProduct(Product product) {
        product = sanitizeProduct(product);
        productRepository.save(product);
    }
    public void updateProductDTO(ProductDTO productDTO) {
        Product product = toDomain(productDTO);
        product = sanitizeProduct(product);
        productRepository.save(product);
    }

    // Método para eliminar un producto por su ID
    public void deleteProduct(Long id) {
        // Llamamos a UserService para eliminar las relaciones del producto con los usuarios
        userService.removeProductFromUsers(id);
        // Luego eliminamos el producto
        productRepository.deleteById(id);
    }
    public void deleteAllProduct(){
        productRepository.deleteAll();
    }


    public List<Product> findProductsNormal(Integer from, Integer to, String category){
        if (from != null || to != null || (category != null && !category.isEmpty())){
            if (from != null && to != null  && (category != null && !category.isEmpty())){
                return productRepository.findByPrecioAndCategory(from,to,category);
            } else if (from != null && to != null ) {
                return productRepository.findByPrecioBetween(from,to);
            } else if (category != null && !category.isEmpty()) {
                return productRepository.findByCategory(category);
            } else {
                return getAllProductsNormal();
            }
        }else {
            return getAllProductsNormal();
        }

    }
    public List<ProductDTO> findProducts(Integer from, Integer to, String category) {
        List<Product> products;
        if (from != null || to != null || (category != null && !category.isEmpty())) {
            if (from != null && to != null && (category != null && !category.isEmpty())) {
                products = productRepository.findByPrecioAndCategory(from, to, category);
            } else if (from != null && to != null) {
                products = productRepository.findByPrecioBetween(from, to);
            } else if (category != null && !category.isEmpty()) {
                products = productRepository.findByCategory(category);
            } else {
                products = productRepository.findAll();
            }
        } else {
            products = productRepository.findAll();
        }
        return toDTOs(products);
    }
    /*public List<Product> getProductsByIds(List<Long> productIds){
        List<Product> products = new ArrayList<>();
        for (Long productId : productIds){
            Optional<Product> optionalProduct = productRepository.findById(productId);
            optionalProduct.ifPresent(products::add);
        }
        return products;
    }*/
    public List<ProductDTO> getProductsByIds(List<Long> productIds) {
        List<Product> products = new ArrayList<>();
        for (Long productId : productIds) {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            optionalProduct.ifPresent(products::add);
        }
        return toDTOs(products);
    }

    public Product sanitizeProduct(Product product){
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.BLOCKS);
        String nombreSanitized = policy.sanitize(product.getNombre());
        product.setNombre(nombreSanitized);
        String descripcionSanitized = policy.sanitize(product.getDescripcion());
        product.setDescripcion(descripcionSanitized);
        String categorySanitized = policy.sanitize(product.getCategory());
        product.setCategory(categorySanitized);
        double precioMaximo = 10000;
        int cantidadMaxima = 1000;
        if (product.getPrecio() < 0 || product.getPrecio() > precioMaximo){
            product.setPrecio(0);
        }
        if (product.getCantidad() < 0 || product.getCantidad() > cantidadMaxima){
            product.setCantidad(0);
        }

        return product;
    }

    public ProductDTO toDTO(Product product){
        return mapper.toDTO(product);
    }

    public Product toDomain(ProductDTO productDTO){
        return mapper.toDomain(productDTO);
    }

    public List<ProductDTO> toDTOs(List<Product> products){
        return mapper.toDTOs(products);
    }
}
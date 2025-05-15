package com.spartanwrath.service;

import com.spartanwrath.dto.ProductDTO;
import com.spartanwrath.mappers.ProductMapper;
import com.spartanwrath.model.Product;
import com.spartanwrath.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.io.IOException;
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

    public List<Product> getAllProductsNormal() {
        return productRepository.findAll();
    }


    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void updateProduct(Product product) {
        product = sanitizeProduct(product);
        productRepository.save(product);
    }
    public void updateProductDTO(ProductDTO productDTO) {
        Product product = toDomain(productDTO);
        product = sanitizeProduct(product);
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        userService.removeProductFromUsers(id);
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
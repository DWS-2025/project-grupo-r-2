package com.spartanwrath.restController;


import com.fasterxml.jackson.annotation.JsonView;
import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.Product;
import com.spartanwrath.model.User;
import com.spartanwrath.service.ImageService;
import com.spartanwrath.service.ProductService;
import com.spartanwrath.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.spartanwrath.dto.ProductDTO;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

//import static com.spartanwrath.service.ImageService.FILES_FOLDER;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api")
public class ProductRestController {

    /*private static final String PRODUCTS_FOLDER = "products";*/

    @Autowired
    private UserService userServ;

    @Autowired
    private ProductService productServ;

    @Autowired
    private ImageService imageServ;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(required = false) Integer from, @RequestParam(required = false) Integer to,@RequestParam(required = false) String category) {
        List<ProductDTO> productDTOs;
        if (category != null || from != null || to != null) {
            productDTOs = productServ.findProductsNormal(from, to, category).stream()
                    .map(productServ::toDTO)
                    .toList();
            if (!productDTOs.isEmpty()) {
                return ResponseEntity.ok().body(productDTOs);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            productDTOs = productServ.getAllProductsNormal().stream()
                    .map(productServ::toDTO)
                    .toList();
            return ResponseEntity.ok().body(productDTOs);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable long id) {
        Optional<Product> product = productServ.getProductById(id);
        if (product.isPresent()) {
            ProductDTO productDTO = productServ.toDTO(product.get());
            return ResponseEntity.ok().body(productDTO);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProductDTO(@RequestBody ProductDTO productDTO) throws IOException {
        Product product = productServ.toDomain(productDTO);
        productServ.createProduct(product);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(product.getId()).toUri();

        ProductDTO createdProductDTO = productServ.toDTO(product);
        return ResponseEntity.created(location).body(createdProductDTO);
    }

    @PostMapping("/products/{id}/imagen")
    public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException{
        Optional<Product> productOptional = productServ.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (imageFile != null && !imageFile.isEmpty()){
                byte[] imageData = imageFile.getBytes();
                product.setImagen(imageData);
                product.setOriginalImageName(imageServ.sanitizeFileName(imageFile.getOriginalFilename()) );
                imageServ.saveImage(imageData, imageFile.getOriginalFilename());
                productServ.updateProduct(product);

                return ResponseEntity.ok().build();
            }else {
                return ResponseEntity.badRequest().body("No se proporciona imagen");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/products/{id}/imagen")
    public ResponseEntity<byte[]> downloadImage(@PathVariable long id) throws MalformedURLException {
        Optional<Product> productOptional = productServ.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            byte[] imageData = product.getImagen();

            if (imageData == null || imageData.length == 0) {
                return ResponseEntity.notFound().build();
            }

            String filename = product.getOriginalImageName();
            MediaType mediaType = MediaType.IMAGE_JPEG;

            if (filename != null) {
                if (filename.endsWith(".png")) {
                    mediaType = MediaType.IMAGE_PNG;
                } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                    mediaType = MediaType.IMAGE_JPEG;
                } else if (filename.endsWith(".gif")) {
                    mediaType = MediaType.IMAGE_GIF;
                }
            }

            return ResponseEntity.ok().contentType(mediaType).body(imageData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProductDTO(@PathVariable long id, @RequestBody ProductDTO productDTO) {
        Optional<Product> productOptional = productServ.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setNombre(productDTO.nombre());
            product.setPrecio(productDTO.precio());

            productServ.updateProduct(product);
            ProductDTO updatedProductDTO = productServ.toDTO(product);
            return ResponseEntity.ok().body(updatedProductDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/products")
    public ResponseEntity<Product> deleteAll() {
        productServ.deleteAllProduct();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) throws IOException {
        Optional<Product> productOptional = productServ.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            productServ.deleteProduct(id);
            if (product.getImagen() != null) {
                this.imageServ.deleteImage(product.getOriginalImageName());
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/products/{id}/imagen")
    public ResponseEntity<Object> deleteImage(@PathVariable long id) throws IOException{
        Optional<Product> productOptional = productServ.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            String imageData = product.getOriginalImageName();
            if (imageData != null){
                this.imageServ.deleteImage(imageData);
            }
            byte[] defaultImage = imageServ.getDefault();
            product.setImagen(defaultImage);
            product.setOriginalImageName(imageServ.getDefaultName());
            productServ.updateProduct(product);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/products/purchase")
    public ResponseEntity<String> purchaseProducts(@RequestBody Map<Long, Integer> productQuantityMap, HttpServletRequest request) throws UserNotFound, InvalidUser {
        try {
            String authenticatedUsername = request.getUserPrincipal().getName();
            User user = userServ.getUserbyUsername(authenticatedUsername);
            if (user == null) {
                return ResponseEntity.badRequest().body("El usuario no fue encontrado");
            }

            double totalAmount = 0.0;

            for (Map.Entry<Long, Integer> entry : productQuantityMap.entrySet()) {
                Long productId = entry.getKey();
                Integer quantity = entry.getValue();

                Optional<Product> productOptional = productServ.getProductById(productId);
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    int availableQuantity = product.getCantidad();
                    if (availableQuantity < quantity) {
                        return ResponseEntity.badRequest().body("La cantidad para el producto con ID " + productId + " es superior al stock disponible");
                    }
                    if (quantity == 0 || quantity < 0){
                        return ResponseEntity.badRequest().body("Añade productos al carrito antes de continuar");
                    }
                    totalAmount += product.getPrecio() * quantity;
                    product.setCantidad(availableQuantity - quantity);
                    productServ.updateProduct(product);

                    user.getProducts().add(product);
                } else {
                    return ResponseEntity.badRequest().body("El producto con ID " + productId + " no se encuentra");
                }
            }

            totalAmount = Math.round(totalAmount * 100.0) / 100.0;
            userServ.updateUser(user.getUsername(), user);

            return ResponseEntity.ok().body("Compra realizada. Precio total: $" + String.format("%.2f", totalAmount));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario no fue encontrado");
        } catch (InvalidUser e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario no es válido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }



}

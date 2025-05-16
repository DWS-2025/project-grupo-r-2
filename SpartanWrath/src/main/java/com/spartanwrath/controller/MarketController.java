package com.spartanwrath.controller;

import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.Product;
import com.spartanwrath.model.User;
import com.spartanwrath.service.ImageService;
import com.spartanwrath.service.ProductService;
import com.spartanwrath.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
public class MarketController {

    @Autowired
    private UserService userServ;

    @Autowired
    private ImageService imageServ;
    private final ProductService productService;
    @Autowired
    public MarketController(ProductService productService) {
        this.productService = productService;
    }

    @Value("${image.upload.dir}")
    private String uploadDir;

    @GetMapping("/Market")
    public String showMarket(){
        return "market";
    }
    @GetMapping("/Market/products/formproducto")
    public String showForm(){
        return "formproducto";
    }

    @GetMapping("/Market/products/editarproducto/{id}")
    public String editProduct(@PathVariable("id") Long id,Model model){
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            String base64Image = Base64.getEncoder().encodeToString(product.getImagen());
            model.addAttribute("base64Image", base64Image);
            model.addAttribute("product", product);
            return "editarproducto";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/Market/products")
    public String showProducts(Model model, HttpServletRequest request,
                               @RequestParam(name = "from", required = false) Integer from,
                               @RequestParam(name = "to", required = false) Integer to,
                               @RequestParam(name = "category", required = false) String category) {

        // Verificar si el usuario es administrador
        boolean isAdmin = request.isUserInRole("ADMIN");
        model.addAttribute("admin", isAdmin);

        List<Product> productList = productService.findProductsNormal(from, to, category);
        productList.forEach(product -> {
            String base64Image = Base64.getEncoder().encodeToString(product.getImagen());
            product.setBase64Image(base64Image);
        });

        model.addAttribute("products", productList);
        return "products";
    }


    @GetMapping("/Market/products/{id}")
    public String showProduct(@PathVariable("id") Long id,HttpServletRequest request, Model model) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            String base64Image = Base64.getEncoder().encodeToString(product.getImagen());
            model.addAttribute("product", product);
            model.addAttribute("base64Image", base64Image);
            return "product";
        } else {
            return "error";
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("id") Long id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            try {
                Product product = productOptional.get();
                Path imageRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
                String filename = Paths.get(product.getImagePath()).getFileName().toString(); // Ej: "/uploads/images/taladro.jpg"
                Path filePath = imageRoot.resolve(filename).normalize();
                if (!filePath.startsWith(imageRoot)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                Resource resource = new UrlResource(filePath.toUri());

                if (resource.exists() && resource.isReadable()) {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                            .contentType(MediaType.IMAGE_JPEG)
                            .contentLength(Files.size(filePath))
                            .body(resource);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/nuevoproducto")
    public String newProducto(Product product,Model model, HttpServletRequest request, @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageData = imageFile.getBytes();
            product.setImagen(imageData);
            product.setOriginalImageName(imageFile.getOriginalFilename());
            imageServ.saveImage(imageData, imageFile.getOriginalFilename());
        } else {
            product.setImagen(imageServ.getDefault());
            product.setOriginalImageName(imageServ.getDefaultName());
        }
        Product newProduct = productService.createProduct(product);
        return "redirect:/Market/products/" + newProduct.getId();
    }
    @PostMapping("/Market/products/{id}")
    public String updateProduct(@PathVariable("id") Long id,Model model, HttpServletRequest request, Product product, @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        Optional<Product> productData = productService.getProductById(id);
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        if (productData.isPresent()) {
            Product _product = productData.get();
            _product.setNombre(product.getNombre());
            _product.setDescripcion(product.getDescripcion());
            _product.setPrecio(product.getPrecio());
            _product.setCantidad(product.getCantidad());
            _product.setCategory(product.getCategory());

            if (imageFile != null && !imageFile.isEmpty()) {
                byte[] imageData = imageFile.getBytes();
                _product.setImagen(imageData);
                _product.setOriginalImageName(imageFile.getOriginalFilename());
                imageServ.saveImage(imageData, imageFile.getOriginalFilename());
            }

            productService.updateProduct(_product);
            return "redirect:/Market/products";
        } else {
            return "redirect:/error";
        }
    }


    @GetMapping("/Market/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) throws IOException {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            String imageName = product.getOriginalImageName();
            imageServ.deleteImage(imageName);
            productService.deleteProduct(id);
            return "redirect:/Market/products";
        } else {
            return "redirect:/error";
        }
    }


    @PostMapping("/products/purchase")
    public String purchaseProducts(HttpServletRequest request, Model model) {
        try {
            String name = request.getUserPrincipal().getName();
            User user = userServ.findByName(name).orElseThrow();
            if (user == null) {
                model.addAttribute("error", "El usuario no fue encontrado");
                return "error";
            }

            String[] productIds = request.getParameterValues("productIds");
            String[] quantities = request.getParameterValues("quantities");

            double totalAmount = 0.0;

            for (int i = 0; i < productIds.length; i++) {
                Long productId = Long.parseLong(productIds[i]);
                Integer quantity = Integer.parseInt(quantities[i]);

                Optional<Product> productOptional = productService.getProductById(productId);
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    int availableQuantity = product.getCantidad();
                    if (availableQuantity < quantity) {
                        model.addAttribute("error", "La cantidad para el producto con ID " + productId + " es superior al stock disponible");
                        return "error";
                    }
                    if (quantity == 0 || quantity < 0){
                        model.addAttribute("error", "Añade productos al carrito antes de continuar");
                        return "error";
                    }
                    totalAmount += product.getPrecio() * quantity;
                    product.setCantidad(availableQuantity - quantity);
                    productService.updateProduct(product);

                    // Agregar el producto a la lista del usuario
                    user.getProducts().add(product);
                } else {
                    model.addAttribute("error", "El producto con ID " + productId + " no se encuentra");
                    return "error";
                }
            }

            totalAmount = Math.round(totalAmount * 100.0) / 100.0;
            userServ.updateUser(user.getUsername(), user);

            model.addAttribute("successMessage", "Compra realizada. Precio total: $" + String.format("%.2f", totalAmount));
            return "successPage";
        } catch (UserNotFound e) {
            model.addAttribute("error", "El usuario no fue encontrado");
            return "error";
        } catch (InvalidUser e) {
            model.addAttribute("error", "El usuario no es válido");
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Error interno del servidor");
            return "error";
        }
    }

}

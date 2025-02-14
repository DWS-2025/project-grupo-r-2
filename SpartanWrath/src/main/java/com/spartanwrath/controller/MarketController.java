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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    @GetMapping("/Market")
    public String showMarket(){
        return "market";
    }
    @GetMapping("/Market/products/formproducto")
    public String showForm(){
        return "formproducto";
    }

    @GetMapping("/Market/products/{id}/editarproducto")
    public String editProduct(@PathVariable("id") Long id,Model model){
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            return "editarproducto";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/Market/products")
    public String showProducts(Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("products", productList);
        return "products";
    }

    @GetMapping("/Market/products/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model) {

        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            return "product";
        } else {
            return "error";
        }
    }



    @PostMapping("/nuevoproducto")
    public String newProducto(Product product, @RequestParam(required = false) MultipartFile imageFile, Model model) throws IOException {
        if (product.getNombre() == null || !product.getNombre().matches(".*[a-zA-Z].*")) {
            model.addAttribute("errorMessage", "El nombre debe contener al menos una letra.");
            return "errorNombre"; // Retorna la misma vista del formulario
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            product.setImagen("../../images/" + imageFile.getOriginalFilename());
            imageServ.saveImage(product.getImagen(), imageFile);
        } else {
            product.setImagen("../../images/DefaultProduct.jpg");
        }
        Product newProduct = productService.createProduct(product);
        return "redirect:/Market/products/" + newProduct.getId();
    }
    @PostMapping("/Market/products/{id}")
    public String updateProduct(@PathVariable("id") Long id, Product product, @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        Optional<Product> productData = productService.getProductById(id);
        if (productData.isPresent()) {
            Product _product = productData.get();
            _product.setNombre(product.getNombre());
            _product.setDescripcion(product.getDescripcion());
            _product.setPrecio(product.getPrecio());
            _product.setCantidad(product.getCantidad());
            _product.setCategory(product.getCategory());

            if (imageFile != null && !imageFile.isEmpty()) {
                _product.setImagen("../../images/" + imageFile.getOriginalFilename());
                imageServ.saveImage(_product.getImagen(), imageFile);
            }

            productService.updateProduct(_product);
            return "redirect:/Market/products";
        } else {
            return "redirect:/error";
        }
    }


    @GetMapping("/Market/products/{id}/delete")
    public String deleteProduct(@PathVariable("id") Long id) throws IOException {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            String imageUrl = product.getImagen();
            imageServ.deleteImage(imageUrl);
            productService.deleteProduct(id);
            return "redirect:/Market/products";
        } else {
            return "redirect:/error";
        }
    }


    @PostMapping("/products/purchase")
    public String purchaseProducts(HttpServletRequest request, Model model) {
        try {
            User user = userServ.getUserbyUsername("usuario1");
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

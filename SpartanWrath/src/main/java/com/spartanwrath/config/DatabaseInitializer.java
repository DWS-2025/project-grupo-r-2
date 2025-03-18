package com.spartanwrath.config;

import com.spartanwrath.exceptions.InvalidUser;
import com.spartanwrath.exceptions.UserAlreadyRegister;
import com.spartanwrath.exceptions.UserNotFound;
import com.spartanwrath.model.CombatClass;
import com.spartanwrath.model.Membership;
import com.spartanwrath.model.Product;
import com.spartanwrath.model.User;
import com.spartanwrath.service.CombatClassService;
import com.spartanwrath.service.MembershipService;
import com.spartanwrath.service.ProductService;
import com.spartanwrath.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



@Configuration
public class DatabaseInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService usersService;
    @Autowired
    private MembershipService membershipService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CombatClassService combatClassService;
    @PostConstruct
    public void init() throws IOException, UserAlreadyRegister, InvalidUser, UserNotFound {
        //NUEVOS USUARIOS
        try {
        User user1 = new User( "Nombre1", "usuario1", "email1@example.com", "Dirección1", 123456789, "password1", LocalDate.of(2015, 5,25) ,  "12345678M", "pago1");
        usersService.add(user1);
        User user2 = new User("Alice Johnson", "alice", "alice@example.com", "123 Main St, Springfield", 123456789, "123", LocalDate.of(1990, 8, 15), "12345678A", "visa");
        usersService.add(user2);
        User user3 = new User("Bob Smith", "bob789", "bob@example.com", "456 Elm St, Springfield", 987654321, "securepassword", LocalDate.of(1985, 4, 30), "87654321B", "mastercard");
        usersService.add(user3);
        User user4 = new User("Eva Martinez", "eva", "eva@example.com", "789 Oak St, Springfield", 456789123, "eva123", LocalDate.of(1978, 12, 10), "76543210C", "paypal");
        usersService.add(user4);
        User user5 = new User("John Doe", "johnnyD", "john@example.com", "321 Pine St, Springfield", 654987321, "password123", LocalDate.of(2000, 2, 28), "65432109D", "amex");
        usersService.add(user5);
        User user6 = new User("Lucy Williams", "lucy_w", "lucy@example.com", "654 Cedar St, Springfield", 321654987, "lucy456", LocalDate.of(1996, 6, 5), "54321098E", "discover");
        usersService.add(user6);
        User user7 = new User("Michael Brown", "mike_b", "mike@example.com", "987 Walnut St, Springfield", 159753468, "mike789", LocalDate.of(1982, 10, 20), "43210987F", "venmo");
        usersService.add(user7);
        User user8 = new User("admin","Admin","spartanadmin@example.com","DireccionAdmin",911223344,"P@ssword",LocalDate.of(1999,5,25),"4321299M","pago1");
        usersService.add(user8);

        //ROLES
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        user1.setRoles(roles);user2.setRoles(roles);user3.setRoles(roles);user4.setRoles(roles);user5.setRoles(roles);user6.setRoles(roles);user7.setRoles(roles);user8.setRoles(new ArrayList<>());user8.getRoles().add("USER");user8.getRoles().add("ADMIN");
        usersService.updateUser(user8.getUsername(), user8);



            Path basePath = Paths.get("src/main/resources/static/images/");

            String cascoImageName = "casco.jpeg";
            String espinillerasImageName = "espinilleras.jpg";
            String guantesImageName = "guantes.png";
            String vendasImageName = "vendas.jpg";
            String bucalImageName = "bucal.jpg";
            String proteinasImageName = "proteinas.jpg";

// Ruta relativa de cada imagen
            Path cascoImagePath = basePath.resolve("casco.jpeg");
            Path espinillerasImagePath = basePath.resolve("espinilleras.jpg");
            Path guantesImagePath = basePath.resolve("guantes.png");
            Path vendasImagePath = basePath.resolve("vendas.jpg");
            Path bucalImagePath = basePath.resolve("bucal.jpg");
            Path proteinasImagePath = basePath.resolve("proteinas.jpg");

// Lee los bytes de cada imagen
            byte[] cascoImageBytes = Files.readAllBytes(cascoImagePath);
            byte[] espinillerasImageBytes = Files.readAllBytes(espinillerasImagePath);
            byte[] guantesImageBytes = Files.readAllBytes(guantesImagePath);
            byte[] vendasImageBytes = Files.readAllBytes(vendasImagePath);
            byte[] bucalImageBytes = Files.readAllBytes(bucalImagePath);
            byte[] proteinasImageBytes = Files.readAllBytes(proteinasImagePath);

        //NuEVOS PRODUCTOS
        Product product1 = new Product("Casco", "Casco de proteccion para sparring", cascoImageBytes, 10.00, 20, "Cascos");
        product1.setOriginalImageName(cascoImageName);
        Product product2 = new Product("Espinilleras", "Espinilleras de proteccion para Kick Boxing/Muai Thai", espinillerasImageBytes, 12.00, 35, "Espinilleras");
        product2.setOriginalImageName(espinillerasImageName);
        Product product3 = new Product("Guantes", "Guantes de boxeo 16 Oz de piel sintetica", guantesImageBytes, 49.99, 100, "Guantes");
        product3.setOriginalImageName(guantesImageName);
        Product product4 = new Product("Vendas", "Vendas 4.5 metros", vendasImageBytes, 5.99, 20, "Accesorios");
        product4.setOriginalImageName(vendasImageName);
        Product product5 = new Product("Bucal", "Bucal de proteccion para sparring", bucalImageBytes, 3.00, 50, "Accesorios");
        product5.setOriginalImageName(bucalImageName);
        Product product6 = new Product("Proteina", "Whey Protein facilita el proceso", proteinasImageBytes, 24.99, 30, "Suplementos");
        product6.setOriginalImageName(proteinasImageName);

        productService.createProduct(product1);
        productService.createProduct(product2);
        productService.createProduct(product3);
        productService.createProduct(product4);
        productService.createProduct(product5);
        productService.createProduct(product6);

        //NUEVAS SUSCRIPCIONES
        //LocalDate date1 = LocalDate.of(2025, 1, 1); // 1 de enero de 2025
        //LocalDate date2 = LocalDate.of(2025, 2, 1); // 1 de febrero de 2025
        Membership membership1 = new Membership("1 mes", "Acceso a todas las clases durante 1 mes", 50.00, null, null, true);
        membershipService.save(membership1);

        //NUEVAS CLASES
        CombatClass clase1 = new CombatClass("Boxeo","Clase de boxeo para principiantes, necesario guantes y vendas", "Lunes por la mañana");
        CombatClass clase2 = new CombatClass("K1","Clase de K1 para competidores, se requiere experiencia previa,espinilleras necesarias", "Lunes por la tarde");
        CombatClass clase3 = new CombatClass("Muay Thai","Clase de Muay thai para principiantes, espinilleras no necesarias", "Martes por la mañana");

        combatClassService.save(clase1);
        combatClassService.save(clase2);
        combatClassService.save(clase3);


        Membership membership2 = new Membership("Boxeo", "Acceso a Boxeo durante 1 mes", 25.00, null, null, true);
        membership2.setCombatClass(clase1);
        membershipService.save(membership2);
        user1.setMembership(membership2);
        user2.setMembership(membership2);
        membership2.setUser(List.of(user1,user2));
        membershipService.save(membership2);

        Membership membership3 = new Membership("K1", "Acceso a K1 durante 1 mes", 25.00, null, null, true);
        membership3.setCombatClass(clase2);
        membershipService.save(membership3);
        user3.setMembership(membership3);
        user4.setMembership(membership3);
        membership3.setUser(List.of(user3,user4));
        membershipService.save(membership3);

        Membership membership4 = new Membership("Muay thai", "Acceso a Muay thai durante 1 mes", 25.00, null, null, true);
        membership4.setCombatClass(clase3);
        membershipService.save(membership4);
        user5.setMembership(membership4);
        user6.setMembership(membership4);
        user7.setMembership(membership4);
        membership4.setUser(List.of(user5,user6,user7));
        membershipService.save(membership4);


        usersService.updateUser(user1.getUsername(), user1);
        usersService.updateUser(user2.getUsername(), user2);
        usersService.updateUser(user3.getUsername(), user3);
        usersService.updateUser(user4.getUsername(), user4);
        usersService.updateUser(user5.getUsername(), user5);
        usersService.updateUser(user6.getUsername(), user6);
        usersService.updateUser(user7.getUsername(), user7);
        usersService.updateUser(user8.getUsername(), user8);

        clase1.setMemberships(List.of(membership2));
        clase2.setMemberships(List.of(membership3));
        clase3.setMemberships(List.of(membership4));

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product4);
        user1.setProducts(products);
        product1.setUsuarios(List.of(user1));
        productService.updateProduct(product1);
        usersService.updateUser(user1.getUsername(), user1);

        List<Product> products2 = new ArrayList<>();
        products2.add(product3);
        products2.add(product5);
        user2.setProducts(products2);
        usersService.updateUser(user2.getUsername(), user2);

    } catch (UserAlreadyRegister | InvalidUser ex){
            throw ex;
        }

    }
}



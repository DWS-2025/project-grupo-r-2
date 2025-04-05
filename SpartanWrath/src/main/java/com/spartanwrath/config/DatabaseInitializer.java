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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.io.IOException;
import java.util.ArrayList;
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
        // Nuevos usuarios
        try {
            User user1 = new User("Nombre1", "usuario1", "email1@example.com", "Dirección1", 123456789, "password1", LocalDate.of(2015, 5, 25), "12345678M", "pago1");
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
            User user8 = new User("admin", "Admin", "spartanadmin@example.com", "DireccionAdmin", 911223344, "P@ssword", LocalDate.of(1999, 5, 25), "4321299M", "pago1");
            usersService.add(user8);

            // Roles
            List<String> roles = new ArrayList<>();
            roles.add("USER");
            user1.setRoles(roles);
            user2.setRoles(roles);
            user3.setRoles(roles);
            user4.setRoles(roles);
            user5.setRoles(roles);
            user6.setRoles(roles);
            user7.setRoles(roles);
            user8.setRoles(new ArrayList<>());
            user8.getRoles().add("USER");
            user8.getRoles().add("ADMIN");
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
            Path kimonoImagePath = basePath.resolve("kimono.jpg");

            // Lee los bytes de cada imagen
            byte[] cascoImageBytes = Files.readAllBytes(cascoImagePath);
            byte[] espinillerasImageBytes = Files.readAllBytes(espinillerasImagePath);
            byte[] guantesImageBytes = Files.readAllBytes(guantesImagePath);
            byte[] vendasImageBytes = Files.readAllBytes(vendasImagePath);
            byte[] bucalImageBytes = Files.readAllBytes(bucalImagePath);
            byte[] proteinasImageBytes = Files.readAllBytes(proteinasImagePath);
            byte[] kimonoImageBytes = Files.readAllBytes(kimonoImagePath);

            // Nuevos productos
            Product product1 = new Product("Casco", "Casco de proteccion para sparring", cascoImageBytes, 10.00, 20, "Proteccion");
            product1.setOriginalImageName(cascoImageName);
            Product product2 = new Product("Espinilleras", "Espinilleras de proteccion para Kick Boxing/Muai Thai", espinillerasImageBytes, 12.00, 35, "Proteccion");
            product2.setOriginalImageName(espinillerasImageName);
            Product product3 = new Product("Guantes", "Guantes de boxeo 16 Oz de piel sintetica", guantesImageBytes, 49.99, 100, "Proteccion");
            product3.setOriginalImageName(guantesImageName);
            Product product4 = new Product("Vendas", "Vendas 4.5 metros", vendasImageBytes, 5.99, 20, "Proteccion");
            product4.setOriginalImageName(vendasImageName);
            Product product5 = new Product("Bucal", "Bucal de proteccion para sparring", bucalImageBytes, 3.00, 50, "Proteccion");
            product5.setOriginalImageName(bucalImageName);
            Product product6 = new Product("Proteina", "Whey Protein facilita el proceso", proteinasImageBytes, 24.99, 30, "Suplementos");
            product6.setOriginalImageName(proteinasImageName);
            Product product7 = new Product("Kimono", "Gi (kimono) para BJJ o Karate", kimonoImageBytes, 37.55, 40, "Uniforme");
            product7.setOriginalImageName(proteinasImageName);

            productService.createProduct(product1);
            productService.createProduct(product2);
            productService.createProduct(product3);
            productService.createProduct(product4);
            productService.createProduct(product5);
            productService.createProduct(product6);
            productService.createProduct(product7);

            // Nuevas clases de combate
            CombatClass karate = new CombatClass("Karate", "Clase de Karate tradicional, se requiere gi (kimono)", "Miércoles por la mañana");
            CombatClass mma = new CombatClass("MMA", "Clase de MMA para niveles intermedios y avanzados, se requiere experiencia previa", "Jueves por la tarde");
            CombatClass jjb = new CombatClass("Jiu-Jitsu Brasileño", "Clase de Jiu-Jitsu para principiantes, se recomienda gi(kimono)", "Viernes por la mañana");
            CombatClass taekwondo = new CombatClass("Taekwondo", "Clase de Taekwondo, se requiere uniforme adecuado", "Sábado por la mañana");
            CombatClass kickboxing = new CombatClass("Kickboxing", "Clase de Kickboxing para todos los niveles, se requiere guantes y vendas", "Domingo por la tarde");
            CombatClass boxeo = new CombatClass("Boxeo", "Clase de Boxeo con Topuria, se requiere guantes y casco", "Lunes por la tarde");

            // Guardamos las clases de combate
            combatClassService.save(karate);
            combatClassService.save(mma);
            combatClassService.save(jjb);
            combatClassService.save(taekwondo);
            combatClassService.save(kickboxing);
            combatClassService.save(boxeo);

            // Crear las membresías correspondientes
            Membership membershipKarate1 = new Membership("Karate", "Acceso a Karate durante 1 mes", 20.00, null, null, true);
            membershipKarate1.setCombatClass(karate);
            membershipService.save(membershipKarate1);

            Membership membershipKarate3 = new Membership("Karate-3", "Acceso a Karate durante 3 meses", 50.00, null, null, true);
            membershipKarate3.setCombatClass(karate);
            membershipService.save(membershipKarate3);

            Membership membershipMMA1 = new Membership("MMA", "Acceso a MMA durante 1 mes", 30.00, null, null, true);
            membershipMMA1.setCombatClass(mma);
            membershipService.save(membershipMMA1);

            Membership membershipMMA3 = new Membership("MMA-3", "Acceso a MMA durante 3 meses", 75.00, null, null, true);
            membershipMMA3.setCombatClass(mma);
            membershipService.save(membershipMMA3);

            Membership membershipJJB1 = new Membership("Jiu-Jitsu Brasileño", "Acceso a Jiu-Jitsu Brasileño durante 1 mes", 25.00, null, null, true);
            membershipJJB1.setCombatClass(jjb);
            membershipService.save(membershipJJB1);

            Membership membershipJJB3 = new Membership("Jiu-Jitsu Brasileño-3", "Acceso a Jiu-Jitsu Brasileño durante 3 meses", 60.00, null, null, true);
            membershipJJB3.setCombatClass(jjb);
            membershipService.save(membershipJJB3);

            Membership membershipTaekwondo1 = new Membership("Taekwondo", "Acceso a Taekwondo durante 1 mes", 20.00, null, null, true);
            membershipTaekwondo1.setCombatClass(taekwondo);
            membershipService.save(membershipTaekwondo1);

            Membership membershipTaekwondo3 = new Membership("Taekwondo-3", "Acceso a Taekwondo durante 3 meses", 50.00, null, null, true);
            membershipTaekwondo3.setCombatClass(taekwondo);
            membershipService.save(membershipTaekwondo3);

            Membership membershipKickboxing1 = new Membership("Kickboxing", "Acceso a Kickboxing durante 1 mes", 22.00, null, null, true);
            membershipKickboxing1.setCombatClass(kickboxing);
            membershipService.save(membershipKickboxing1);

            Membership membershipKickboxing3 = new Membership("Kickboxing-3", "Acceso a Kickboxing durante 3 meses", 55.00, null, null, true);
            membershipKickboxing3.setCombatClass(kickboxing);
            membershipService.save(membershipKickboxing3);

            Membership membershipBoxeo = new Membership("Boxeo", "Acceso a Karate durante 1 mes", 20.00, null, null, true);
            membershipBoxeo.setCombatClass(boxeo);
            membershipService.save(membershipBoxeo);

            Membership membershipBoxeo3 = new Membership("Boxeo-3", "Acceso a Karate durante 3 meses", 50.00, null, null, true);
            membershipBoxeo3.setCombatClass(boxeo);
            membershipService.save(membershipBoxeo3);

            // Asociar membresías a usuarios (Ejemplo: los usuarios 1, 2 y 3 se suscriben a Karate, MMA y Jiu-Jitsu)
            user1.setMembership(membershipKarate1);
            user2.setMembership(membershipMMA1);
            user3.setMembership(membershipJJB1);
            usersService.updateUser(user1.getUsername(), user1);
            usersService.updateUser(user2.getUsername(), user2);
            usersService.updateUser(user3.getUsername(), user3);

            // Actualizar clases de combate con las membresías asociadas
            karate.setMemberships(List.of(membershipKarate1, membershipKarate3));
            mma.setMemberships(List.of(membershipMMA1, membershipMMA3));
            jjb.setMemberships(List.of(membershipJJB1, membershipJJB3));
            taekwondo.setMemberships(List.of(membershipTaekwondo1, membershipTaekwondo3));
            kickboxing.setMemberships(List.of(membershipKickboxing1, membershipKickboxing3));

            // Guardamos los cambios en las clases
            combatClassService.save(karate);
            combatClassService.save(mma);
            combatClassService.save(jjb);
            combatClassService.save(taekwondo);
            combatClassService.save(kickboxing);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

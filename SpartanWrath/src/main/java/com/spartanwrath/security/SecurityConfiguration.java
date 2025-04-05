package com.spartanwrath.security;

import com.spartanwrath.security.jwt.JwtRequestFilter;
import com.spartanwrath.security.jwt.UnauthorizedHandlerJwt;
import com.spartanwrath.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.security.SecureRandom;
import jakarta.servlet.http.HttpSessionIdListener;
import java.util.Map;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    DetailService detailService;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(detailService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10,new SecureRandom());
    }


    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());
        http.securityMatcher("/api/**").exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
        http.authorizeHttpRequests(authorize -> authorize
                //PRODUCTS
                .requestMatchers(HttpMethod.POST,"/api/products/{id}/imagen").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/products").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/products/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/products").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/products/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/products/{id}/imagen").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/products/purchase").hasAnyRole("USER","ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/products").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/products/{id}").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/products/{id}/imagen").permitAll()
                //USER
                .requestMatchers(HttpMethod.GET,"/api/User").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/User/{username}").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.PUT,"/api/User/{username}").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.DELETE,"/api/User/{username}").hasAnyRole("ADMIN","USER")
                //COMBATCLASS
                .requestMatchers(HttpMethod.GET,"/api/combatclass").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.GET,"/api/combatclass/{id}").hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.POST,"/api/combatclass").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/combatclass/{id}").hasRole("ADMIN")
                //MEMBERSHIP
                .requestMatchers(HttpMethod.GET,"/api/Membership").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/Membership/{id}").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/Membership").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/Membership/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/Membership/{id}").hasAnyRole("ADMIN","USER")
                .anyRequest().permitAll());

        http.formLogin(formLogin -> formLogin.disable());
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();


    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers.xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                .contentSecurityPolicy(cps -> cps.policyDirectives("script-src 'self' 'sha256-7CrbOuHXC5pcX5/WZqrJ1wsaWibsKsVvGKmDbneg1MQ=' 'sha256-BTH2FzjOBXCsNvMS+hWWCHxKtvCcc/Ovg/Shn33kWMY=' 'sha256-C98k/qM+0Xy4tZaAWU/1R9eZfBtncjlwuEau4/pdI8Q=' 'sha256-tua2yQk4cCecxvmQxji+9jVpU6xQcvq3T/Rnbb8XeU4=' 'sha256-XaldY76yZI63NOAG5oCBJ+/Pb4/CgvRAMRc+Fxz7KZE=' https://cdn.jsdelivr.net/npm/quill@2.0.0-rc.2/dist/quill.js https://cdn.jsdelivr.net")));

        http.authenticationProvider(authenticationProvider());

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PUBLIC PAGES
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/Market").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/AboutUs").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/Membership").permitAll()
                        .requestMatchers("/Membership/{id}").permitAll()
                        .requestMatchers("/v3/api-docs**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // STATIC RESOURCES
                        //.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // PRIVATE PAGES
                        .requestMatchers("/products/purchase").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/Market/products/formproducto").hasAnyRole("ADMIN")
                        .requestMatchers("/Market/products/editarproducto/*").hasRole("ADMIN")
                        .requestMatchers("/Market/products/delete/*").hasRole("ADMIN")
                        .requestMatchers("/Admin/combatclass/**").hasRole("ADMIN")
                        .requestMatchers("/Mymemberships").hasRole("ADMIN")
                        .requestMatchers("/Membership/formsuscripcion").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/Membership/{id}").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/nuevasuscripcion").hasRole("ADMIN")
                        .requestMatchers("/Membership/{id}/delete").hasRole("ADMIN")
                        .requestMatchers("/Market").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/successPage").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/download/{id}").hasAnyRole("ADMIN","USER")
                        .requestMatchers("/nuevoproducto").hasRole("ADMIN")
                        .requestMatchers("/Market/products/*").hasAnyRole("ADMIN","USER")
                        .requestMatchers("/Market/products").hasAnyRole("ADMIN","USER")
                        .requestMatchers("/Market/product/**").hasRole("ADMIN")
                        .requestMatchers("/private/delete/*").hasAnyRole("ADMIN","USER")
                        .requestMatchers("/private/{id}").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/private").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/Admin").hasRole("ADMIN")
                        .requestMatchers("/Admin/combatclass").hasRole("ADMIN")
                        .requestMatchers("/Admin/combatclass/{id}").hasRole("ADMIN")
                        .requestMatchers("/nuevaclase").hasRole("ADMIN")
                        .requestMatchers("/Admin/combatclass/formcombatclass").hasRole("ADMIN")
                        .requestMatchers("/Admin/combatclass/{id}/delete").hasRole("ADMIN")
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/error")
                        .defaultSuccessUrl("/private",true)
                        .permitAll()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .expiredUrl("/login")
                        .maxSessionsPreventsLogin(true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES)))
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

}


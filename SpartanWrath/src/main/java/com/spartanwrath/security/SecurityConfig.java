package com.spartanwrath.security;

import com.spartanwrath.security.jwt.JwtRequestFilter;
import com.spartanwrath.security.jwt.UnauthorizedHandlerJwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	RepositoryUserDetailsService detailService;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
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
		return new BCryptPasswordEncoder(10, new SecureRandom());
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http.securityMatcher("/api/**")
				.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

		http.authorizeHttpRequests(authorize -> authorize
				// PRODUCTOS
				.requestMatchers(HttpMethod.POST, "/api/products/{id}/imagen").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/api/products/{id}").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/products").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/products/{id}").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/products/{id}/imagen").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/api/products/purchase").hasAnyRole("USER", "ADMIN")
				.requestMatchers(HttpMethod.GET, "/api/products").hasAnyRole("USER", "ADMIN")
				.requestMatchers(HttpMethod.GET, "/api/products/{id}").hasAnyRole("USER", "ADMIN")
				.requestMatchers(HttpMethod.GET, "/api/products/{id}/imagen").hasAnyRole("USER", "ADMIN")
				// USUARIOS
				.requestMatchers(HttpMethod.GET, "/api/User").hasRole("ADMIN")
				.requestMatchers(HttpMethod.GET, "/api/User/{username}").hasAnyRole("ADMIN", "USER")
				.requestMatchers(HttpMethod.PUT, "/api/User/{username}").hasAnyRole("ADMIN", "USER")
				.requestMatchers(HttpMethod.DELETE, "/api/User/{username}").hasAnyRole("ADMIN", "USER")
				// COMBATCLASS
				.requestMatchers(HttpMethod.GET, "/api/combatclass").hasAnyRole("ADMIN", "USER")
				.requestMatchers(HttpMethod.GET, "/api/combatclass/{id}").hasAnyRole("ADMIN", "USER")
				.requestMatchers(HttpMethod.POST, "/api/combatclass").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/combatclass/{id}").hasRole("ADMIN")
				// MEMBERSHIP
				.requestMatchers(HttpMethod.GET, "/api/Membership").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/Membership/{id}").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/Membership").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/Membership/{id}").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/Membership/{id}").hasAnyRole("ADMIN", "USER")
				.anyRequest().permitAll());

		http.formLogin(formLogin -> formLogin.disable());
		http.csrf(csrf -> csrf.disable());
		http.httpBasic(httpBasic -> httpBasic.disable());
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		// Añadir cabeceras de seguridad
		http.headers(headers -> headers
				.xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
				.frameOptions(frameOptions -> frameOptions.deny())
				.httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).preload(true).maxAgeInSeconds(31536000))
				.contentTypeOptions(Customizer.withDefaults())
		);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http.headers(headers -> headers
				.xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
				.frameOptions(frameOptions -> frameOptions.deny())
				.httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).preload(true).maxAgeInSeconds(31536000))
				.contentTypeOptions(Customizer.withDefaults())
				.contentSecurityPolicy(csp -> csp.policyDirectives(
						"default-src 'none'; " +
								"script-src 'self' " +
								"'sha256-7CrbOuHXC5pcX5/WZqrJ1wsaWibsKsVvGKmDbneg1MQ=' " +
								"'sha256-BTH2FzjOBXCsNvMS+hWWCHxKtvCcc/Ovg/Shn33kWMY=' " +
								"'sha256-C98k/qM+0Xy4tZaAWU/1R9eZfBtncjlwuEau4/pdI8Q=' " +
								"'sha256-tua2yQk4cCecxvmQxji+9jVpU6xQcvq3T/Rnbb8XeU4=' " +
								"'sha256-XaldY76yZI63NOAG5oCBJ+/Pb4/CgvRAMRc+Fxz7KZE=' " +
								"https://cdn.jsdelivr.net/npm/quill@2.0.0-rc.2/dist/quill.js " +
								"https://cdn.jsdelivr.net; " +
								"style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net; " +
								"font-src 'self' https://cdn.jsdelivr.net; " +
								"img-src 'self' data:; " +
								"connect-src 'self'; " +
								"media-src 'none'; " +
								"frame-src 'none'; " +
								"manifest-src 'none'; " +
								"worker-src 'none'; " +
								"object-src 'none'; " +
								"frame-ancestors 'none';"
				))
		);

		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/Membership/{id}").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/", "/error", "/Market", "/login", "/AboutUs", "/register", "/Membership", "/Membership/{id}").permitAll()
						.requestMatchers("/v3/api-docs**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						.requestMatchers("/products/purchase").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/Market/products/formproducto").hasRole("ADMIN")
						.requestMatchers("/Market/products/editarproducto/*").hasRole("ADMIN")
						.requestMatchers("/Market/products/delete/*").hasRole("ADMIN")
						.requestMatchers("/Admin/combatclass/**").hasRole("ADMIN")
						.requestMatchers("/Mymemberships", "/Membership/formsuscripcion", "/nuevasuscripcion", "/Membership/{id}/delete").hasRole("ADMIN")
						.requestMatchers("/Market").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/successPage", "/download/{id}", "/nuevoproducto").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/Market/products/*", "/Market/products").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/Market/product/**").hasRole("ADMIN")
						.requestMatchers("/private/delete/*", "/private/{id}", "/private").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/Admin", "/Admin/combatclass", "/Admin/combatclass/{id}", "/nuevaclase", "/Admin/combatclass/formcombatclass", "/Admin/combatclass/{id}/delete").hasRole("ADMIN")
				)
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.failureUrl("/error")
						.defaultSuccessUrl("/private", true)
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
						.invalidateHttpSession(true)
						.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES)))
						.deleteCookies("JSESSIONID")
						.permitAll()
				);

		return http.build();
	}
}

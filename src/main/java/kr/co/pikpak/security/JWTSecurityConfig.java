package kr.co.pikpak.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.pikpak.device.SHA256Encoder;
import kr.co.pikpak.service.LoginService;

@Configuration
@EnableWebSecurity
public class JWTSecurityConfig {
	@Autowired
	private LoginService LoginService;
	
	@Autowired 
	private JWTRequestFilter JWTRequestFilter;
	
	@Autowired
	private SHA256Encoder Encoder;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors-> cors.disable())
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(
					(auth) -> auth
					.requestMatchers("/login", "/login/**").permitAll()
					.requestMatchers("/logout", "/logout/**").permitAll()
					.requestMatchers("/auth/**").permitAll()
					.requestMatchers("/resources/**").permitAll()
					.requestMatchers("/favicon.ico").permitAll()
					// HBK
					.requestMatchers("/client/**").hasAuthority("operator")
					.requestMatchers("/product/**").hasAuthority("operator")
					.requestMatchers("/stocklogrecord/**").hasAuthority("admin")
					.requestMatchers("/notice/regnotice").hasAuthority("admin")
					.requestMatchers("/notice/write").hasAuthority("admin")
					.requestMatchers("/notice/update").hasAuthority("admin")
					.requestMatchers("/notice/updateNotice").hasAuthority("admin")
					.requestMatchers("/notice/**").hasAnyAuthority("admin","operator","supplier","vendor")
					// LHH
					.requestMatchers("/order/order_aplist").hasAuthority("operator")
					.requestMatchers("/order/**").hasAnyAuthority("operator","admin","vendor")
					.requestMatchers("/return/return_aplist").hasAnyAuthority("operator")
					.requestMatchers("/return/**").hasAnyAuthority("operator","admin","vendor")
					// KMJ
					.requestMatchers("/inventory/**").hasAuthority("operator")
					// LHW
					.requestMatchers("/inoutbound/**").hasAuthority("operator")
					.requestMatchers("/delivery/**").hasAuthority("supplier")
					// PSH
					.requestMatchers("/admin/**").hasAuthority("admin")
					.anyRequest().authenticated())
			.exceptionHandling(auth -> auth.accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authEntryPoint()))
			.formLogin(auth -> auth.loginPage("/login").permitAll())
			.httpBasic(auth -> auth.disable())
			.authenticationProvider(authenticationProvider())
			.logout(config -> config.logoutSuccessUrl("/logout/end").deleteCookies("accessToken").invalidateHttpSession(true))
			.addFilterBefore(JWTRequestFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(LoginService);
		authenticationProvider.setPasswordEncoder(Encoder);
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	@Bean
	public AuthenticationEntryPoint authEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}
	
}

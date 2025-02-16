package kr.co.pikpak.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res,
			AuthenticationException authException) throws IOException, ServletException {
		try {
			Boolean isTokenExpired = (Boolean) req.getAttribute("expired");
			if (isTokenExpired) {
				res.sendRedirect("/auth/session-expired");
			}
			else {
				res.sendRedirect("/auth/auth-error");
			}
		} catch (Exception e) {
			res.sendRedirect("/auth/auth-error");
		}
	}
}

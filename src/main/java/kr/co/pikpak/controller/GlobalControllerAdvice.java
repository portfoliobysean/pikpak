package kr.co.pikpak.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.pikpak.device.CookieUtility;
import kr.co.pikpak.security.CustomUserDetailsService;
import kr.co.pikpak.security.JWTUtility;



@ControllerAdvice
public class GlobalControllerAdvice {
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@ModelAttribute("activeUserName")
	public String getNameFromContext() {
		String result = userDetailsService.userNameFromContext();
		return result;
	};
	
	@ModelAttribute("activeUserType")
	public String getTypeFromContext() {
		String result = userDetailsService.userTypeFromContext();
		return result;
	}
}

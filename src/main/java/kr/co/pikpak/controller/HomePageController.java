package kr.co.pikpak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomePageController {
	
	@GetMapping("/home")
	public String homePage() {
		//return "/home/home";
		return "/notice/noticeTable"; 
	}
	
	@GetMapping("/home/user/cred") 
	public String passwordChangePage() {
		return "/user/cred_change";
	}
	
	
	@GetMapping("/auth/access-denied")
	public String accessDeniedPage() {
		return "/home/access_denied";
	}
	
	@GetMapping("/auth/auth-error")
	public String loginErrorPage() {
		return "/home/access_auth_error";
	}
	
	@GetMapping("/auth/session-expired")
	public String sessionExpiredPage() {
		return "/home/access_session_expired";
	}
}

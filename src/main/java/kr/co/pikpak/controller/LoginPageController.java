package kr.co.pikpak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginPageController {
	@GetMapping("/login")
	public String loginPage() {
		return "/home/login";
	}
	
	@GetMapping("/logout/end")
	public String logoutPage() {
		return "/home/logout";
	}
	
	@GetMapping("/login/find")
	public String credResetPage() {
		return "/home/find";
	}
}

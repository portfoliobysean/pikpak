package kr.co.pikpak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.pikpak.device.SHA256Encoder;
import kr.co.pikpak.dto.CredChangeDTO;
import kr.co.pikpak.dto.LoginDTO;
import kr.co.pikpak.service.UserService;

@RestController
public class HomeController {
	@Autowired
	private UserService us;
	
	@Autowired
	private SHA256Encoder stringEncoder;
	
	@PostMapping("/login/find/verify")
	public String verifyUserForCredReset(LoginDTO ldto) {
		String responseText = "";
		
		try {
			int userCount = us.ctnEmailFromView(ldto.getUser_id(), ldto.getUser_mail());
			if (userCount == 0) {
				responseText = "N";
			}
			else {
				responseText = "Y";
			}
		} catch (Exception e) {
			responseText = "N";
		}
		return responseText;
	}
	
	@PostMapping("/login/find/reset")
	public String userCredReset(CredChangeDTO cdto) {
		String responseText = "";
		
		if (cdto.getUser_type().equals("admin") || cdto.getUser_type().equals("operator")) {
			cdto.setTarget_table("login_operator");
			cdto.setTarget_id("operator_id");
			cdto.setTarget_pw("operator_pw");
		}
		else {
			cdto.setTarget_table("login_trader");
			cdto.setTarget_id("trader_id");
			cdto.setTarget_pw("trader_pw");
		}
		
		int updateResult = us.resetCredInTable(cdto);
		if (updateResult == 0) {
			responseText = "N";
		}
		else {
			responseText = "Y";
		}
		return responseText;
	}
	
	@PostMapping("/home/user/verify")
	public String verifyPasswordForCredChange(@RequestParam(value="userPw", required = false) String userPw, HttpSession sess) {
		String userId = (String) sess.getAttribute("activeUserID");
		String encodedPw = stringEncoder.encode(userPw);
		String responseText = "";
		
		try {
			int userCount = us.ctnPwFromView(userId, encodedPw);
			if (userCount == 0) {
				responseText = "N";
			}
			else {
				responseText = "Y";
			}
		} catch (Exception e) {
			responseText = "N";
		}
		return responseText;
	}
	
	@PostMapping("/home/user/cred/mod")
	public String updateCredentials(CredChangeDTO cdto, HttpSession sess) {
		String userId = (String) sess.getAttribute("activeUserID");
		String encodedPw = stringEncoder.encode(cdto.getNew_user_pw());
		cdto.setUser_id(userId);
		cdto.setNew_user_pw(encodedPw);
		String responseText = "";
		
		
		if (cdto.getUser_type().equals("admin") || cdto.getUser_type().equals("operator")) {
			cdto.setTarget_table("login_operator");
			cdto.setTarget_id("operator_id");
			cdto.setTarget_pw("operator_pw");
		}
		else {
			cdto.setTarget_table("login_trader");
			cdto.setTarget_id("trader_id");
			cdto.setTarget_pw("trader_pw");
		}
		
		
		int updateResult = us.modCredInTable(cdto);
		if (updateResult == 0) {
			responseText = "N";
		}
		else {
			responseText = "Y";
		}
		
		return responseText;
	}
}

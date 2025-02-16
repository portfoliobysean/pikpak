package kr.co.pikpak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.pikpak.device.SHA256Encoder;
import kr.co.pikpak.dto.UserAddDTO;
import kr.co.pikpak.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService us;
	
	@Autowired
	private SHA256Encoder stringEncoder;
	
	@PostMapping("/admin/check/id")
	public String checkUserId(@RequestParam(value="user_id", required = false) String userId) {
		String responseText = "";
		
		try {
			int userCount = us.ctnFromView(userId);
			if (userCount == 0) {
				responseText = "Y";
			}
			else {
				responseText = "N";
			}
		} catch (Exception e) {
			responseText = "N";
		}
		return responseText;
	}
	
	@PostMapping("/admin/check/tel")
	public String checkUserTel (
			@RequestParam(value="user_tel", required = false) String userTel,
			@RequestParam(value="user_type", required = false) String userType) {
		String responseText = "";
		
		try {
			int userCount = us.ctnFromViewFilterBy(userTel, userType);
			//int userCount = 0;
			if (userCount == 0) {
				responseText = "Y";
			}
			else {
				responseText = "N";
			}
		} catch (Exception e) {
			responseText = "N";
		}
		return responseText;
	}
	
	@PostMapping("/admin/users/add")
	public String addUser(UserAddDTO userAddDto) {
		String responseText = "";
		
		String encodedPw = stringEncoder.encode(userAddDto.getUser_pw());
		userAddDto.setUser_pw(encodedPw);
		
		if (userAddDto.getUser_type().equals("operator") || userAddDto.getUser_type().equals("admin")) {
			userAddDto.setTarget_table("login_operator");
		}
		else {
			userAddDto.setTarget_table("login_trader");
		}
		
		int insertResult = us.addUserToTable(userAddDto);
		if (insertResult == 0) {
			responseText = "N";
		}
		else {
			responseText = "Y";
		}
		
		return responseText;
	}
	
	@PostMapping("/admin/users/mod")
	public String modUser(UserAddDTO userModDto) {
		String responseText = "";
		
		if (!userModDto.getUser_pw().equals("")) {
			String encodedPw = stringEncoder.encode(userModDto.getUser_pw());
			userModDto.setUser_pw(encodedPw);
		}
		
		if (userModDto.getUser_type().equals("operator") || userModDto.getUser_type().equals("admin")) {
			userModDto.setTarget_table("login_operator");
			userModDto.setTarget_id("operator_id");
			userModDto.setTarget_pw("operator_pw");
			userModDto.setTarget_tel("operator_tel");
			userModDto.setTarget_mail("operator_mail");
			userModDto.setTarget_lv("operator_lv");
			
		}
		else {
			userModDto.setTarget_table("login_trader");
			userModDto.setTarget_id("trader_id"); 
			userModDto.setTarget_pw("trader_pw");
			userModDto.setTarget_tel("trader_tel");
			userModDto.setTarget_mail("trader_mail");
		}
		
		int updateResult = us.modUserInTable(userModDto);
		if (updateResult == 0) {
			responseText = "N";
		}
		else {
			responseText = "Y";
		}
		
		return responseText;
	}

}

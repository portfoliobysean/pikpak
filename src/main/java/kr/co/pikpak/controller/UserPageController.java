package kr.co.pikpak.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.co.pikpak.dto.InfoTraderSimpleDTO;
import kr.co.pikpak.dto.LoginDTO;
import kr.co.pikpak.dto.UserDetailsDTO;
import kr.co.pikpak.dto.UserOperatorDTO;
import kr.co.pikpak.dto.UserTraderDTO;
import kr.co.pikpak.service.UserService;

@Controller
public class UserPageController {
	@Autowired
	private UserService us;
	
	@GetMapping("/admin/users")
	public String userListPage(Model m) {
		List<LoginDTO> userList = us.userListFromView();
		List<InfoTraderSimpleDTO> companyList = us.companyListFromView();
		m.addAttribute("userList",userList);
		m.addAttribute("userListSize",userList.size());
		//m.addAttribute("companyList",companyList);
		
		List<InfoTraderSimpleDTO> supplierList = new ArrayList<>();
		List<InfoTraderSimpleDTO> vendorList = new ArrayList<>();
		
		for (InfoTraderSimpleDTO dto : companyList) {
			if (dto.getTrader_cd().startsWith("C")) {
				supplierList.add(dto);
			}
			else {
				vendorList.add(dto);
			}
		}
		m.addAttribute("supplierList",supplierList);
		m.addAttribute("vendorList",vendorList);
		
		return "/user/users";
	}
	
	@GetMapping("/admin/user/view/{user_id}")
	public String userViewPage(Model m, @PathVariable(name="user_id", required=false) String userId) {
		LoginDTO userDetails = us.userDetailsFromView(userId);
		
		// 뷰에서 회원 유형 호출
		String userType = us.userTypeFromView(userId);
		
		// 회원 유형에따라 테이블 상세 정보 호출
		if (userType.equals("admin") || userType.equals("operator")) {
			String userLv = us.userLvFromOperator(userId);
			userDetails.setUser_lv(userLv);
		}
		else {
			userDetails.setUser_lv("");

		}
		
		m.addAttribute("userDetails", userDetails);
		
		return "/user/user_details";
	}
}

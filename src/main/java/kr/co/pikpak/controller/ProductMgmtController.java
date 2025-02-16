package kr.co.pikpak.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.pikpak.service.ProductMgmtService;

@Controller
@RequestMapping("/product")
public class ProductMgmtController {

	@Autowired
	private ProductMgmtService productMgmtService;
	
	@Autowired
	private HttpSession session;  // HttpSession 주입

	@GetMapping("/list")
	public String home() {
		return "/product/productmgmt";
	}

	@PostMapping(value = "/searchcomname", consumes = "application/json")
	public @ResponseBody List<HashMap<String, Object>> searchCompanyName(@RequestBody HashMap<String, Object> param) {

		return productMgmtService.searchcompanyname(param);
	}

	@PostMapping(value = "/regproduct", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> insertProduct(@RequestBody HashMap<String, Object> param) {

		
		String userId = (String) session.getAttribute("activeUserID");
			
		String operatorNm = productMgmtService.getOperatorNameByUserId(userId);
		
		param.put("userId", userId);
		param.put("operatorNm", operatorNm);
		
		HashMap<String, Object> returnMap = productMgmtService.insertProduct(param);

		return returnMap;

	}

	@PostMapping(value = "/list", consumes = "application/json")
	public @ResponseBody List<HashMap<String, Object>> getAllList() {

		return productMgmtService.selectAllList();
	}

	@PostMapping(value = "/getproduct", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> selectProductInfo(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> returnMap = productMgmtService.selectProductInfo(param);

		return returnMap;
	}

	@PostMapping(value = "/updateproduct", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> updateProduct(@RequestBody HashMap<String, Object> param) {
		
		String userId = (String) session.getAttribute("activeUserID");
		
		String operatorNm = productMgmtService.getOperatorNameByUserId(userId);
		
		param.put("userId", userId);
		param.put("operatorNm", operatorNm);
		
		
		
		HashMap<String, Object> returnMap = productMgmtService.updateProduct(param);

		return returnMap;
	}

	@PostMapping(value = "/search", consumes = "application/json")
	public @ResponseBody List<HashMap<String, Object>> searchProduct(@RequestBody HashMap<String, Object> param) {

		List<HashMap<String, Object>> returnMap = productMgmtService.selectSearchProduct(param);

		return returnMap;
	}

	@PostMapping(value = "/getLastCode", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> getLastCode(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		String newCode = productMgmtService.generateNewCode(param);

		returnMap.put("newcode", newCode);

		return returnMap;
	}
}

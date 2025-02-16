package kr.co.pikpak.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.pikpak.dto.InventoryListDTO;
import kr.co.pikpak.dto.WarehouseInspection_dto;
import kr.co.pikpak.dto.WarehouseInventory_dto;
import kr.co.pikpak.service.WarehouseInventoryService;
@Controller
@RequestMapping("/inventory")
public class InventoryPageController {
	@Autowired
	private WarehouseInventoryService wis;
	
	
	@GetMapping("/getSessionInfo")
	@ResponseBody
	public Map<String, String> getSessionInfo(HttpSession session) {
	    Map<String, String> sessionInfo = new HashMap<>();
	    sessionInfo.put("activeUserID", (String) session.getAttribute("activeUserID"));
	    return sessionInfo;
	}
	
	//재고 현황 페이지 - 재고 리스트 페이지 출력
	@GetMapping("/inventorylist")
	public String loginPage(Model m) {
		//supplier_info 조회
		int keyType =3;
		List<Map<String, Object>> supplierInfo = wis.getSupplierInfo(keyType, null);
		
		//warehouse 조회
		keyType = 2;
		List<WarehouseInventory_dto> all = (List<WarehouseInventory_dto>) wis.getWarehouseInfo(null,keyType);
		//supplier_info 조회
		List<Map<String, Object>> supplier_nm = wis.getSupplierInfo(keyType, null);
		//최초 페이지 로드시 데이터 리스트 출력
		List<InventoryListDTO> inventoryData = wis.getCombinedInventoryData(null, null, null, null, null, null, null);
		
	    // inventoryData에서 상품코드와 상품명을 추출
	    Set<String> product_cd = inventoryData.stream().map(InventoryListDTO::getProduct_cd).collect(Collectors.toSet());
	    Set<String> product_nm = inventoryData.stream().map(InventoryListDTO::getProduct_nm).collect(Collectors.toSet());

		m.addAttribute("inventoryData", inventoryData);
		m.addAttribute("product_cd",product_cd);	//검색 옵션
		m.addAttribute("product_nm",product_nm);	//검색 옵션
		m.addAttribute("supplier_nm",supplier_nm);	//검색 옵션
		m.addAttribute("getAllsupplier_cd",supplierInfo);	//검색 옵션
		
		return "/Inventory/inventorylist";
	}

	//재고 현황 페이지 - 관리 버튼 팝업창
	@GetMapping("/inventory_popup")
	public String inventory_popup(@RequestParam("wh_warehouse_idx") String wh_warehouse_idx, Model m) {
		return "/Inventory/inventory_popup";
	}
	
	//창고별 재고 현황 페이지 출력
	@GetMapping("/warehouse_inventory")
	public String warehouse_inventory() {
		return "/Inventory/warehouse_inventory";
	}
	
	//창고 관리 페이지 - 창고 점검 리스트 출력
	@GetMapping("/warehouse_management")
	public String warehouse_management(Model m) {
		List<WarehouseInspection_dto> all = wis.getCheckData();
		Map<String, List<WarehouseInspection_dto>> area =
				all.stream().collect(Collectors.groupingBy(WarehouseInspection_dto::getArea_cd));
		m.addAttribute("area",area);
		return "/Inventory/warehouse_management";
	}
	
	
	//재고 현황 페이지 - 상품 검색 팝업창
	@GetMapping("/product_search") 		
	public String product_search() {
		return "/Inventory/product_search";
	}
	
	//재고 현황 페이지 - 회사 검색 팝업창
	@GetMapping("/corp_search")			
	public String corp_search () {
		return "/Inventory/corp_search";
	}
	
	//재고 창고 위치 관리 페이지
	@GetMapping("/warehouse_location")
	public String warehouse_location(Model m) {
		int keyType = 3;
		List<Map<String, Object>> result = wis.getSupplierInfo(keyType, null);
		m.addAttribute("suppliers",result);
		return "/Inventory/warehouse_grid";
	}
}

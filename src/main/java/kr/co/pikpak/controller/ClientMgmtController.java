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

import kr.co.pikpak.service.ClientMgmtService;

@Controller
@RequestMapping("/client")
public class ClientMgmtController {

	@Autowired
	private ClientMgmtService clientMgmtService;

	@GetMapping("/list")
	public String home() {
		return "/client/clientmgmt";
	}

	@PostMapping(value = "/regsupplier", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> insertClient(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> returnMap = clientMgmtService.insertSupplier(param);

		return returnMap;
	}

	@PostMapping(value = "/regvendor", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> insertVendor(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> returnMap = clientMgmtService.insertVendor(param);

		return returnMap;
	}

	@PostMapping(value = "/list", consumes = "application/json")
	public @ResponseBody List<HashMap<String, Object>> selectList() {

		return clientMgmtService.getAllList();
	}

	@PostMapping(value = "/filter", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> filterClients(@RequestBody HashMap<String, Object> param) {

		String filterType = (String) param.get("filterType");
		HashMap<String, Object> returnMap = new HashMap<>();

		if ("supplier".equals(filterType)) {
			// 제조사만 반환
			List<HashMap<String, Object>> supplierList = clientMgmtService.getSupplierList();
			returnMap.put("supplierList", supplierList);
		} else if ("vendor".equals(filterType)) {
			// 고객사만 반환
			List<HashMap<String, Object>> vendorList = clientMgmtService.getVendorList();
			returnMap.put("vendorList", vendorList);
		} else {
			// 전체 데이터를 반환
			List<HashMap<String, Object>> supplierList = clientMgmtService.getSupplierList();
			List<HashMap<String, Object>> vendorList = clientMgmtService.getVendorList();
			returnMap.put("supplierList", supplierList);
			returnMap.put("vendorList", vendorList);
		}

		return returnMap;
	}

	@PostMapping(value = "/getclient", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> getClient(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> returnMap = new HashMap<>();
		String clientType = param.get("clientType").toString();

		// 전달된 clientType 값 비교
		if ("supplier".equals(clientType)) {
			returnMap = clientMgmtService.getSupplierById(param.get("clientId").toString());
		} else if ("vendor".equals(clientType)) {
			returnMap = clientMgmtService.getVendorById(param.get("clientId").toString());
		}

		return returnMap;
	}

	@PostMapping(value = "/updatesupplier", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> updateSupplier(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		returnMap = clientMgmtService.updateSupplier(param);

		return returnMap;
	}

	@PostMapping(value = "/updatevendor", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> updateVendor(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		returnMap = clientMgmtService.updateVendor(param);

		return returnMap;
	}

	@PostMapping(value = "/searchcompanyname", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> searchCompanyName(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> returnMap = new HashMap<>();

		List<HashMap<String, Object>> supplierList = clientMgmtService.searchSupplierByName(param);
		List<HashMap<String, Object>> vendorList = clientMgmtService.searchVendorByName(param);

		returnMap.put("supplierList", supplierList);
		returnMap.put("vendorList", vendorList);

		return returnMap;
	}

	@PostMapping(value = "/search", consumes = "application/json")
	public @ResponseBody List<HashMap<String, Object>> searchClient(@RequestBody HashMap<String, Object> param) {

		List<HashMap<String, Object>> returnMap = clientMgmtService.selectSearchClient(param);

		return returnMap;
	}

	@PostMapping(value = "/getLastCompanyCode", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> getLastCode(@RequestBody HashMap<String, Object> param) {

		String clientType = param.get("clientType").toString();

		HashMap<String, Object> returnMap = new HashMap<>();

		String lastCode = clientMgmtService.getLastCode(clientType);

		String newCode = clientMgmtService.getNewCode(lastCode);

		returnMap.put("newCode", newCode);

		return returnMap;
	}

	@PostMapping(value = "/dupChk", consumes = "application/json")
	public @ResponseBody HashMap<String, Object> countDupChk(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> returnMap = clientMgmtService.countDupChk(param);

		return returnMap;
	}
}

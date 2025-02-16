package kr.co.pikpak.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.pikpak.dto.InventoryListDTO;
import kr.co.pikpak.dto.LocationDTO;
import kr.co.pikpak.dto.WarehouseInspection_dto;
import kr.co.pikpak.dto.WarehouseInventory_dto;
import kr.co.pikpak.repo.WarehouseRepo;
import kr.co.pikpak.service.WarehouseInventoryService;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
	@Autowired
	private WarehouseInventoryService wis;
	
	
	//창고 점검 페이지 - 구역 정보 조회
	
    @GetMapping("/getZoneManager")
    @ResponseBody
    public Map<String, Object> getZoneManager(@RequestParam("zoneId") String zoneId) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> managerInfo = wis.getAreaAndOperatorData(zoneId);
        if (managerInfo != null) {
            response.put("manager_nm", managerInfo.get("manager_nm"));
            response.put("operator_id", managerInfo.get("operator_id"));
        } else {
            response.put("error", "구역 또는 운영자 정보를 찾을 수 없습니다.");
        }
        return response;
    }	
    
	//창고 점검 페이지 - 점검 등록 
	@PostMapping("/insertInspectData")
	@ResponseBody
	public ResponseEntity<String> insertInspectData(@RequestParam Map<String, String> request){
		int result = wis.insertCheckData2(request);
		if(result>0) {
			return ResponseEntity.ok("정상적으로 등록 되었습니다.");
		}else {
			return ResponseEntity.ok("오류가 발생하여 등록에 실패했습니다.");
		}
	}
	
	//창고 점검 페이지 - 상세보기
	@GetMapping("/getCheckRecordDetails/{a_check_idx}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getCheckRecordDetails(@PathVariable("a_check_idx")Integer a_check_idx){
		Map<String, Object> recordDetails = wis.getCheckRecordDetailsByIdx(a_check_idx);
		if(recordDetails != null) {
			return ResponseEntity.ok(recordDetails);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	//창고 점검 페이지 - 재고 상태 조회
	@PostMapping("/getZoneStockData")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getZoneStockData(@RequestBody Map<String, String> request, Model m) {
		String zoneId = request.get("zoneId");
		List<Map<String, Object>> stockData = wis.getStockDataByZone(zoneId);
		return ResponseEntity.ok(stockData);
	}

	
	//창고 위치 관리 페이지 - 위치 삭제
	@PostMapping("/deleteLocation")
	@ResponseBody
	public ResponseEntity<Map<String, Object>>deleteLocation(@RequestBody Map<String, Object>data,
			@SessionAttribute(name = "activeUserID", required = false) String operator_id){
		String location_cd = (String) data.get("location_cd");
		int result = wis.deleteLocationByCode(location_cd);
		
		Map<String, Object> response = new HashMap<>();
		
		if(result>0) {
			wis.deleteLocationsLog(operator_id, location_cd ,"위치 삭제");
			
			response.put("success", true);
			response.put("message", "위치가 삭제되었습니다.");
			
		}else {
			response.put("success", false);
			response.put("message", "위치삭제에 실패했습니다.");
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	//창고 위치 관리 페이지 - 위치 지정
	@PostMapping("/assignLocation")
	@ResponseBody
	public Map<String, String> assignLocation(@RequestBody Map<String, Object> data,
			@SessionAttribute(name = "activeUserID", required = false) String operator_id){
		String location_cd = (String) data.get("location_cd");
		String supplier_cd = (String)data.get("supplier_cd");
		Integer max_capacity = 0;
		if(location_cd.contains("L1")) {
			max_capacity = 3;
		}else if(location_cd.contains("L2")||location_cd.contains("L3")) {
			max_capacity = 6;
		}else if(location_cd.contains("L4")) {
			max_capacity = 12;
		}
		
		int result = wis.insertLocation(location_cd, supplier_cd,max_capacity);
		Map<String, String> response = new HashMap<>();
		if(result>0) {
			wis.deleteLocationsLog(operator_id, location_cd ,"위치 등록");
			
			response.put("status", "success");
			response.put("message", "위치지정이 완료되었습니다.");
		}else {
			response.put("status", "error");
			response.put("message", "위치지정에 실패했습니다.");			
		}
		return response;
	}
	
	//창고 위치 관리 페이지 - 위치 조회 
	@PostMapping("/checkLocation")
	@ResponseBody
	public Map<String, Object> checkLocation(@RequestBody Map<String, String>request){
		String locationCd = request.get("location_cd");
		 LocationDTO location = wis.findLocationByCode(locationCd);
		 Map<String, Object> response = new HashMap<>();
		    // 조회 결과가 null일 때만 위치 지정 가능
		    if (location == null) {
		        response.put("status", "available");  // 위치 사용 가능
		    } else {
		        response.put("status", "occupied");   // 위치 사용 불가능
		        response.put("has_stock", location.getHasStock());  // 해당 위치에 재고가 있는지 여부
		    }	 
		return response;
	}
	
	//재고 현황 페이지 - 조회버튼
	@PostMapping("/getSearchData")
	@ResponseBody
	public List<InventoryListDTO> searchInventory(@RequestBody Map<String,String> searchData ){
		String area_cd = searchData.get("area_cd");
		String rack_number = searchData.get("rack_number");
		String level = searchData.get("level");
		String part = searchData.get("part");
		String product_cd = searchData.get("product_cd");
		String product_nm = searchData.get("product_nm");
		String supplier_nm = searchData.get("supplier_nm");
		return  wis.getCombinedInventoryData(area_cd, rack_number, level, part, product_cd, product_nm, supplier_nm);
	}
	
	
	//재고 현황 페이지 - 관리 버튼 클릭 시 상세 페이지 데이터 출력
	@GetMapping("/getInventoryDetails")
	@ResponseBody
	public Map<String, Object> getInventoryDetails(@RequestParam("wh_warehouse_idx") Integer wh_warehouse_idx,
			@RequestParam("k") int keyType){
		List<WarehouseInventory_dto> getDetailsByIdx = wis.getWarehouseInfo(wh_warehouse_idx,keyType);
		//상품코드 별도 추출 후 상품 테이블에서 상품정보 가져오기
		Integer getSafetyInventory_qty = wis.getSafetyInventory_qty(getDetailsByIdx.get(0).getProduct_cd());
		Integer incomingStock = wis.getIncomingStock(getDetailsByIdx.get(0).getProduct_cd());
		Integer outgoingStock = wis.getOutgoingStock(getDetailsByIdx.get(0).getProduct_cd());
		Map<String, Object> result = new HashMap<>();
		result.put("details",getDetailsByIdx.get(0));
		result.put("safety_inventory", getSafetyInventory_qty);
	    result.put("incoming_stock", incomingStock != null ? incomingStock : 0);  // null일 경우 0으로 처리
	    result.put("outgoing_stock", outgoingStock != null ? outgoingStock : 0);  		
		return result;
	}
	
	// 재고 현황 페이지 - 관리 모달창 /재고 폐기 
	@PostMapping("/deleteWarehouse")
	@ResponseBody
	public Map<String, Object> deleteWarehouse(@RequestBody Map<String, Object> requestData,
			@SessionAttribute(name = "activeUserID", required = false) String operator_id) {
		int keyType = 3;
		//폐기사요
	    String disposeReason = (String) requestData.get("disposeReason");
        Integer wh_warehouse_idx = Integer.parseInt(requestData.get("wh_warehouse_idx").toString());
        //현재 재고량 가져오기
	    WarehouseInventory_dto currentStock = wis.getProductInfo(keyType, wh_warehouse_idx.toString());
	 
	    Map<String, Object> result = new HashMap<>();
	    try {
	        Integer disposeQuantity = Integer.parseInt(requestData.get("disposeQuantity").toString());
	        Integer currentQty = Integer.parseInt(currentStock.getProduct_qty());
	        
	        if (disposeQuantity.equals(currentQty)) {
	            int deletedRows = wis.deleteWarehouseByIdx(wh_warehouse_idx);
	            if (deletedRows > 0) {
	            	//재고 로그에 폐기사유 업데이트
	            	wis.updateDisposeReason(wh_warehouse_idx, disposeReason, operator_id);
	                result.put("success", true);
	                result.put("action", "delete");
	                result.put("deletedRows", deletedRows);
	            } else {
	                result.put("success", false);
	                result.put("message", "폐기 실패");
	            }
	        } 
	        // 일부 수량만 폐기하는 경우
	        else {
	            int updatedRows = wis.updateWarehouseQuantity(wh_warehouse_idx, disposeQuantity,operator_id);
	            if (updatedRows > 0) {
	            	//재고 로그에 폐기사유 업데이트
	            	wis.updateDisposeReason(wh_warehouse_idx, disposeReason, operator_id);	            	
	                result.put("success", true);
	                result.put("action", "update");
	                result.put("updatedRows", updatedRows);
	            } else {
	                result.put("success", false);
	                result.put("message", "업데이트 실패");
	            }
	        }
	    } catch (Exception e) {
	        result.put("success", false);
	        result.put("message", "처리 중 오류가 발생했습니다.");
	        e.printStackTrace();
	    }
	    return result;
	}
	
	//재고 현황 페이지 - 관리 모달창 / 수정
	@PostMapping("/updateWarehouseNotes")
	@ResponseBody
	public Map<String, Object> updateWarehouseNotes(@RequestBody Map<String, Object> requestData,
			@SessionAttribute(name = "activeUserID", required = false) String operator_id) {
	    Map<String, Object> result = new HashMap<>();
	    try {
	        Integer wh_warehouse_idx = Integer.parseInt(requestData.get("wh_warehouse_idx").toString());
	        String notes = (String) requestData.get("notes");

	        // 서비스 호출하여 비고란 업데이트
	        int updatedRows = wis.updateWarehouseNotes(wh_warehouse_idx,operator_id, notes);

	        if (updatedRows > 0) {
	            result.put("success", true);
	        } else {
	            result.put("success", false);
	        }
	    } catch (Exception e) {
	        result.put("success", false);
	        e.printStackTrace();
	    }
	    return result;
	}	
	
	
	//supplier_info 데이터 조회
	@PostMapping("/getSupplierInfo")
	@ResponseBody
	public Map<String, Object> getSupplierInfo(@RequestBody Map<String, Object> request){
		int keyType = (int) request.get("keyType");
		String data = (String) request.get("data");
		
	    List<Map<String, Object>> result = wis.getSupplierInfo(keyType, data);
	    if (!result.isEmpty()) {
	        return result.get(0);  // 단일 값 반환
	    }
	    return Collections.emptyMap();
	}
	
	//product 테이블 조회
	@PostMapping("/getProductInfo")
	@ResponseBody
	public Map<String, Object> getProductInfo(@RequestBody Map<String, Object> request){
	    int keyType = (int) request.get("keyType");
	    String data = (String) request.get("data");
	    
	    WarehouseInventory_dto product = wis.getProductInfo(keyType, data);
	    Map<String, Object> response = new HashMap<>();
	    response.put("data", product);
		return response;
	}
	
	
	//창고 위치 관리 - warehouse_grid.html (구역 전체 재고 출력)
	@PostMapping("/getZoneData")
	@ResponseBody
	public Map<String, Object> getZoneData(@RequestBody Map<String, String> request){
		String area_cd = request.get("area_cd");
		List<Map<String, Object>> getAreaStockData = wis.getLocationWithStockData(area_cd);		
		Map<String, Object> response = new HashMap<>();
		response.put("getAreaStockData", getAreaStockData);
		return response;
	}
	
	
	
	//창고별 재고 현황 페이지 - 코드 간결화
	@PostMapping("/getMultiAreaData")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getMultiAreaData(@RequestBody Map<String, Object> request){
	    String total_location = (String) request.get("total_location");
	    int keyType = (Integer) request.get("keyType");

	    List<Map<String, Object>> locationData = wis.getMultiAreaStockData(total_location, keyType);
	    
	    Map<String, Object> response = new HashMap<>();
	    if(keyType == 1) {
	    	Map<String, Object> getAreaData = wis.getAreaData(total_location,keyType);
	    	response.put("getAreaData", getAreaData);
	    	response.put("getAreaStockData", locationData);
	    }else if (keyType == 2) {
	        response.put("getAreaRackData", locationData);
	    } else if (keyType == 3) {
	        response.put("getTotalLocationData", locationData);
	    }	
		return ResponseEntity.ok(response);
	}
}

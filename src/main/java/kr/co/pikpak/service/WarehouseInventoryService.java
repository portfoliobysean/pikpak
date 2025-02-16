package kr.co.pikpak.service;

import java.util.List;
import java.util.Map;

import kr.co.pikpak.dto.InventoryListDTO;
import kr.co.pikpak.dto.LocationDTO;
import kr.co.pikpak.dto.WarehouseInspection_dto;
import kr.co.pikpak.dto.WarehouseInventory_dto;

public interface WarehouseInventoryService {
	
	public Map<String, Object> getAreaData(String total_location, int keyType);
	
	public List<Map<String, Object>> getMultiAreaStockData(String total_location, int keyType);
	
	/* product 조회 */
	public WarehouseInventory_dto getProductInfo(int keyType, String data);
	
	/* supplier_info 조회*/
	public List<Map<String, Object>> getSupplierInfo (int keyType, String data);
	
	
	//재고 리스트 출력 페이지 - 관리 버튼 / idx 값으로 재고 데이터 조회
	public List<WarehouseInventory_dto> getWarehouseInfo(Integer wh_warehouse_idx,int keyType);
	
	//재고 리스트 출력 페이지 - 최초 리스트 출력 (안전재고 , ...)
	public List<InventoryListDTO> getCombinedInventoryData (String area_cd, String rack_number, String level, String part, String product_cd, String product_nm, String supplier_nm);
	
	//재고 리스트 출력 페이지 - 관리 버튼 팝업 / 상품코드로 상품테이블에서 안전재고 조회
	public Integer getSafetyInventory_qty(String product_cd);
	
	//재고 리스트 출력 페이지 - 관리 버튼 팝업 / 재고 전체 폐기
	public int deleteWarehouseByIdx(Integer wh_warehouse_idx);
	
	
	//재고 리스트 출력 페이지 - 관리 버튼 팝업 / 재고 일부 폐기
	public int updateWarehouseQuantity(Integer wh_warehouse_idx, Integer disposeQuantity,String operator_id);
	
	//재고 리스트 출력 페이지 - 관리 버튼 팝업 / 비고란 수정
	public int updateWarehouseNotes(Integer wh_warehouse_idx,String operator_id, String notes);
	
	
	//폐기사유 업데이트
	public int updateDisposeReason(Integer wh_warehouse_idx,String disposeReason,String operator_id);	
	
	
	//입고 예정 수량
	public Integer getIncomingStock(String product_cd);
	
	//출고 예정 수량
	public Integer getOutgoingStock(String product_cd);
	
	//창고 위치 관리 페이지 - 
	public List<Map<String, Object>> getLocationWithStockData (String area_cd);
	
	//창고 위치 관리 페이지 - 창고 위치 조회
	public LocationDTO findLocationByCode(String location_cd);
	
	
	//창고 위치 관리 페이지 - 위치 지정
	int insertLocation(String location_cd, String supplier_cd,Integer max_capacity);	
	
	//창고 위치 관리 페이지 - 위치 삭제
	int deleteLocationByCode(String location_cd);	
	
	//창고 위치 관리 페이지 - 위치 등록시 로그 기록
	void insertLocationsLog(String operatorId,String locationCd,String operationType);	
	
	//창고 위치 관리 페이지 - 위치 삭제시 로그 기록
	void deleteLocationsLog(String operatorId,String locationCd,String operationType);
	
	//창고 관리 페이지 - 점검 등록/ 재고 수량 일치여부 확인
	public List<WarehouseInspection_dto> getCheckData();
	
	//창고 관리 페이지 - 점검 등록/ 재고 수량 일치여부 확인
	public List<Map<String, Object>> getStockDataByZone(String zoneId);
	
	//창고 관리 페이지 - 점검 등록
	int insertCheckData2(Map<String, String> data);
	
	//창고 관리 페이지 - 상세 보기
	public Map<String, Object> getCheckRecordDetailsByIdx(Integer a_check_idx);
	
	public Map<String, Object> getAreaAndOperatorData(String zoneId);
}

package kr.co.pikpak.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pikpak.dto.InventoryListDTO;
import kr.co.pikpak.dto.LocationDTO;
import kr.co.pikpak.dto.WarehouseInspection_dto;
import kr.co.pikpak.dto.WarehouseInventory_dto;
import kr.co.pikpak.repo.WarehouseRepo;

@Service
public class WarehouseInventoryServiceImpl implements WarehouseInventoryService{
	@Autowired
	private WarehouseRepo wr;
	
	//창고 관리 페이지 - 구역 점검 기록 출력
	@Override
	public List<WarehouseInspection_dto> getCheckData() {
		return wr.getCheckData();
	}
	
	//창고별 재고 현황 페이지 - 구역 정보 출력
	@Override
	public Map<String, Object> getAreaData(String total_location, int keyType) {
		
		return wr.getAreaData(total_location,keyType);
	}
	
	//창고별 재고 현황 페이지 - 
	@Override
	public List<Map<String, Object>> getMultiAreaStockData(String total_location, int keyType) {
		return wr.getMultiAreaStockData(total_location, keyType);
	}
	
	//재고 리스트 출력 페이지 - 최초 리스트 출력 (안전재고 , ...)
	@Override
	public List<InventoryListDTO> getCombinedInventoryData(String area_cd, String rack_number, String level, String part, String product_cd, String product_nm, String supplier_nm) {
		return wr.getCombinedInventoryData(area_cd, rack_number, level, part, product_cd, product_nm, supplier_nm);
	}
	
	//재고 현황 페이지 - 관리 버튼 / idx 값으로 재고 데이터 조회
	@Override
	public List<WarehouseInventory_dto> getWarehouseInfo(Integer wh_warehouse_idx,int keyType) {
		return wr.getWarehouseInfo(wh_warehouse_idx,keyType);
	}
	//재고 현황 페이지 - 관리 버튼 팝업 / 상품코드로 상품테이블에서 안전재고 조회
	@Override
	public Integer getSafetyInventory_qty(String product_cd) {
		
		return wr.getSafetyInventory_qty(product_cd);
	}

	//재고 리스트 출력 페이지 - 관리 버튼 팝업 / 재고 전체 폐기
	@Override
	public int deleteWarehouseByIdx(Integer wh_warehouse_idx) {
		
		return wr.deleteWarehouseByIdx(wh_warehouse_idx);
	}
	
	
	//재고 리스트 출력 페이지 - 관리 버튼 팝업 / 재고 일부 폐기
	@Override
	public int updateWarehouseQuantity(Integer wh_warehouse_idx, Integer disposeQuantity,String operator_id) {
		return wr.updateWarehouseQuantity(wh_warehouse_idx, disposeQuantity,operator_id);
	}
	
	//재고 리스트 출력 페이지 - 관리 버튼 팝업 / 비고란 수정
	@Override
	public int updateWarehouseNotes(Integer wh_warehouse_idx, String operator_id,String notes) {
		return wr.updateWarehouseNotes(wh_warehouse_idx,operator_id, notes);
	}

	
	//폐기사유 업데이트
	@Override
	public int updateDisposeReason(Integer wh_warehouse_idx, String disposeReason,String operator_id) {
		return wr.updateDisposeReason(wh_warehouse_idx, disposeReason,operator_id);
	}
	
	
	//입고 예정 수량
	@Override
	public Integer getIncomingStock(String product_cd) {
		return wr.getIncomingStock(product_cd);
	}
	
	//출고 예정 수량
	@Override
	public Integer getOutgoingStock(String product_cd) {
		return wr.getOutgoingStock(product_cd);
	}
	
	/* product 조회 */
	@Override
	public WarehouseInventory_dto getProductInfo(int keyType, String data) {
		return wr.getProductInfo(keyType, data);
	}
	
	/* supplier_info 조회*/
	@Override
	public List<Map<String, Object>> getSupplierInfo(int keyType, String data) {
		return wr.getSupplierInfo(keyType, data);
	}
	
	
	//창고 위치 관리 페이지
	@Override
	public List<Map<String, Object>> getLocationWithStockData(String area_cd) {
		
		return wr.getLocationWithStockData(area_cd);
	}
	
	//창고 위치 관리 페이지 - 위치 정보 조회
	@Override
	public LocationDTO findLocationByCode(String location_cd) {
		return wr.findLocationByCode(location_cd);
	}
	
	//창고 위치 관리 페이지 - 위치 지정
	@Override
	public int insertLocation(String location_cd, String supplier_cd,Integer max_capacity) {

		return wr.insertLocation(location_cd, supplier_cd,max_capacity);
	}
	
	//창고 위치 관리 페이지 - 위치 삭제
	@Override
	public int deleteLocationByCode(String location_cd) {
		
		return wr.deleteLocationByCode(location_cd);
	}
	//창고 위치 관리 페이지 - 위치 등록시 로그 기록
	@Override
	public void insertLocationsLog(String operatorId, String locationCd, String operationType) {
		wr.insertLocationsLog(operatorId, locationCd, operationType);
	}
	
	//창고 위치 관리 페이지 - 위치 삭제시 로그 기록
	@Override
	public void deleteLocationsLog(String operatorId, String locationCd, String operationType) {
		wr.insertLocationsLog(operatorId, locationCd, operationType);
	}
	
	
	//창고 관리 페이지 - 점검 등록/ 재고 수량 일치여부 확인
	@Override
	public List<Map<String, Object>> getStockDataByZone(String zoneId) {
		return wr.getStockDataByZone(zoneId);
	}
	
	//창고 관리 페이지 - 점검 등록
	@Override
	public int insertCheckData2(Map<String, String> data) {

		return wr.insertCheckData2(data);
	}
	//창고 관리 페이지 - 상세보기
	@Override
	public Map<String, Object> getCheckRecordDetailsByIdx(Integer a_check_idx) {
		return wr.getCheckRecordDetailsByIdx(a_check_idx);
	}
	
	//창고 관리 페이지 - 점검구역 담당자 정보 
	@Override
	public Map<String, Object> getAreaAndOperatorData(String zoneId) {
		return wr.getAreaAndOperatorData(zoneId);
	}
}

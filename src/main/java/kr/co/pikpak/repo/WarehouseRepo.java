package kr.co.pikpak.repo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.pikpak.dto.InventoryListDTO;
import kr.co.pikpak.dto.LocationDTO;
import kr.co.pikpak.dto.WarehouseInspection_dto;
import kr.co.pikpak.dto.WarehouseInventory_dto;

@Repository
@Mapper
public interface WarehouseRepo {

	
	//창고별 재고 현황 페이지 - 구역 정보 출력
	Map<String, Object> getAreaData(String total_location, int keyType);
		
	List<Map<String, Object>> getMultiAreaStockData(String total_location, int keyType);
	
	//재고 리스트 출력 페이지 - 관리 버튼 클릭 시 idx값으로 재고 상세 조회
	List<WarehouseInventory_dto> getWarehouseInfo(Integer wh_warehouse_idx,int keyType);
	
	//창고 재고 현황 페이지 - 최초 리스트 출력
	List<InventoryListDTO> getCombinedInventoryData (String area_cd, String rack_number, String level, String part, String product_cd, String product_nm, String supplier_nm);
	
	//재고 리스트 출력 페이지 - 관리 버튼 팝업 / 상품코드로 상품테이블에서 안전재고 조회
	Integer getSafetyInventory_qty(String product_cd);
	
	//입고 예정 수량
	Integer getIncomingStock(String product_cd);
	
	//출고 예정 수량
	Integer getOutgoingStock(String product_cd);
	
	//재고 리스트 출력 페이지 - 관리 모달창 재고 폐기(재고 수량 전체 폐기)
	int deleteWarehouseByIdx(Integer wh_warehouse_idx);
	
	// 전체폐기 전 update_by를 업데이트
	int deleteWarehouseByIdx(int whWarehouseIdx, String operatorId);
	
	//재고 리스트 출력 페이지 - 관리 모달창 재고 폐기(재고 수량 일부 폐기)
	int updateWarehouseQuantity(Integer wh_warehouse_idx, Integer disposeQuantity, String operator_id);
	
	//재고 리스트 출력 페이지 - 관리 모달창 수정
	int updateWarehouseNotes(Integer wh_warehouse_idx, String operator_id,String notes);

	//폐기사유 업데이트
	int updateDisposeReason(Integer wh_warehouse_idx,String disposeReason,String operator_id);
	
	/* warehouse 조회 */
	WarehouseInventory_dto getProductInfo (int keyType, String data);
	
	/* supplier_info 조회*/
	List<Map<String, Object>> getSupplierInfo (int keyType, String data);
	
	
	//창고 위치 관리 페이지 - 
	List<Map<String, Object>> getLocationWithStockData (String area_cd);
	
	//창고 위치 관리 페이지
	LocationDTO findLocationByCode (String location_cd);
	
	
	//창고 위치 관리 페이지 - 위치 등록
	int insertLocation(String location_cd, String supplier_cd,Integer max_capacity);
	
	//창고 위치 관리 페이지 - 위치 삭제
	int deleteLocationByCode(String location_cd);
	
	//창고 위치 관리 페이지 - 위치 삭제시 로그 기록
	void insertLocationsLog(String operatorId,String locationCd,String operationType);
	
	
	//창고 관리 페이지 - 구역 점검 기록 리스트
	List<WarehouseInspection_dto> getCheckData();
	
	//창고 관리 페이지 - 점검 등록/ 재고 수량 일치여부 확인
	List<Map<String, Object>> getStockDataByZone(String zoneId);
	
	//창고 관리 페이지 - 점검 등록
	int insertCheckData2(Map<String, String> data);

	//창고 관리 페이지 - 상세 보기
	Map<String, Object> getCheckRecordDetailsByIdx(Integer a_check_idx);
	
	Map<String, Object> getAreaAndOperatorData(String zoneId);
	
}

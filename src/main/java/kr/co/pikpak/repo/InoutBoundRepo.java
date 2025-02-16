package kr.co.pikpak.repo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pikpak.dto.accepted_order_enroll_dto;
import kr.co.pikpak.dto.deliver_return_dto;
import kr.co.pikpak.dto.ex_receiving_dto;
import kr.co.pikpak.dto.ex_receiving_joined_dto;
import kr.co.pikpak.dto.input_request_dto;
import kr.co.pikpak.dto.order_enroll_dto_lhwtemp;
import kr.co.pikpak.dto.outgoing_enroll_dto;
import kr.co.pikpak.dto.outgoing_info_joined_dto;
import kr.co.pikpak.dto.outgoing_select_view_dto;
import kr.co.pikpak.dto.product_dto_lhwtemp;
import kr.co.pikpak.dto.receiving_dto;
import kr.co.pikpak.dto.supplier_info_dto_lhwtemp;
import kr.co.pikpak.dto.warehouse_dto_lhwtemp;
import kr.co.pikpak.dto.warehouse_locations_dto_lhwtemp;

@Mapper
public interface InoutBoundRepo {
	//서버시간
	String get_time();
	
	//로트번호 중복 방지 count
	String select_lot_count(String lot_no);
	
	//운영자 이름으로 id검색(사용자 조회용)
	List<String> search_operator_nm(String operator_nm);
	
	//운영자 id로 이름 검색(리스트 출력용)
	String search_one_id(String operator_id);
	
	//상품정보
	//List<product_dto_lhwtemp> selectProduct();
	List<Map<String, Object>> select_product();
	
	
	//입고요청 등록 
	int input_req_insert(input_request_dto dto);
	
	//입고요청 리스트
	List<input_request_dto> select_inreq();
	
	//입고요청 삭제
	int delete_inreq(String request_idx);
	
	//매입처 회사명 리스트 개수
	Integer select_supplier_total(String comp_nm, String comp_cd);
	
	//매입처 페이징
	List<supplier_info_dto_lhwtemp> select_supplier_limit(Map<String, Object> supplier);
	
	//매입처 회사 검색
	//List<supplier_info_dto_lhwtemp> select_supplier_search(String comp_nm, String comp_cd);

	//상품명 리스트 개수(검색 포함)
	Integer select_product_total(String pd_nm, String pd_cd);
	
	//상품명 페이징
	List<product_dto_lhwtemp> select_product_limit(Map<String, Object> product);
	
	
	//입고요청 리스트 수정
	int update_inreq(Map<String, Object> inrequest);
	
	//입고요청 리스트 조회
	List<input_request_dto> select_inreq_search(Map<String, Object> data_arr);
	
	//가입고 테이블 select -> 입고등록 페이지
	List<ex_receiving_joined_dto> select_ex_receiving(Map<String, Object> data_arr);
	
	//가입고 반송등록
	int insert_deliver_return(deliver_return_dto dto);
	
	//반송등록시 가입고 테이블 업데이트
	int update_exrecv_return(String return_qty, String exreceiving_cd);
	
	//주문현황 확인
	List<accepted_order_enroll_dto> select_order_enroll(Map<String, Object> data_arr);
	
	//위치코드 정보 가지고 오기
	List<warehouse_locations_dto_lhwtemp> select_locations(String supplier_cd);
	
	//입고등록하기 receiving테이블
	int insert_receiving(receiving_dto dto);
	
	//warehouse insert
	int insert_warehouse(Map<String, Object> wh_dto);
	
	//warehouse_locations update
	int update_warehouse_locations(String location_cd);
	
	//출고수량 지정을 위한 위치정보 끌고오기
	List<outgoing_select_view_dto> select_stock(String product_cd);
	
	//출고정보등록
	int insert_outgoing_enroll(outgoing_enroll_dto dto);
	
	//출고피킹등록
	int insert_outgoing_picking(List<Map<String, Object>> picking);
	
	//출고등록시 주문승인 상태업데이트
	int update_acceptedorder_st(String operator_id, String order_cd);
	
	//출고정보 가져오기
	List<outgoing_enroll_dto> select_outgoing();
	
	//출고상세정보 가져오기
	List<outgoing_info_joined_dto> select_outgoing_view(Map<String, Object> data_arr);
	
	//출고등록 테이블 업데이트
	int update_outenroll(String outenroll_cd);
	
	//출고등록 시 로그테이블 타입 '출고' 지정
	int update_stock_log_out(String wh_warehouse_idx);
	
	//출고등록과 동시에 데이터 차감
	int update_warehouse_out(String subtractive_qty, String update_by, String wh_warehouse_idx);
	
	//출고등록 데이터 차감시 전체 제고 빠진 경우 idx 삭제
	int delete_warehouse_out(String wh_warehouse_idx);
	
	//출고 확정
	int update_outenroll_decide(String outenroll_cd, String update_id);
	
	//출고확정 시 원래 주문 상태 완료 변경
	int update_odstate_ended(String order_cd);
	
	//출고등록정보 삭제
	int delete_outenroll(String outenroll_cd);
	
	//출고피킹정보 삭제
	int delete_outpiking(String outenroll_cd);
	
	//출고정보 삭제시 주문승인 상태 원복
	int update_accepted_back(String order_cd);

	/*출고정보 삭제할 떄 재고에서 차감된것 & 삭제된 것 다 시 원복시켜야함*/
}

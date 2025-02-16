package kr.co.pikpak.service;

import java.util.List;
import java.util.Map;

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

public interface InoutBoundService {
	//상품코드 관련 정보 가져오기
	//public List<product_dto_lhwtemp> select_pdlist();
	public List<Map<String, Object>> select_product();
	
	//입고요청 데이터 insert
	public int input_req_insert(input_request_dto dto);
	
	//mysql 서버시간 불러오기
	public String get_time();
	
	//로트번호 중복 방지 count
	public String select_lot_count(String lot_no);
	
	
	//운영자 조회용 아이디 검색하기
	public List<String> search_operator_nm(String operator_nm);
	
	//운영자 출력용 이름검색하기
	public String search_one_id(String operator_id);
	
	//입고요청 리스트
	public List<input_request_dto> select_inreq();

	//입고요청 삭제
	public int delete_inreq(String request_idx);
	
	//매입처 회사명 검색 리스트 총 개수
	public Integer select_supplier_total(String comp_nm, String comp_cd);
	
	//매입처 페이징
	public List<supplier_info_dto_lhwtemp> select_supplier_limit(Map<String, Object> supplier);

	//매입처 검색
	//public List<supplier_info_dto_lhwtemp> select_supplier_search(String comp_nm, String comp_cd);

	//상품명 검색 리스트 총 개수
	public Integer select_product_total(String pd_nm, String pd_cd);
	
	//상품명 페이징 + 검색
	public List<product_dto_lhwtemp> select_product_limit(Map<String, Object> product);
	
	//입고요청 리스트 수정
	public int update_inreq(Map<String, Object> inrequest);
	
	//입고요청 리스트 조회
	public List<input_request_dto> select_inreq_search(Map<String, Object> data_arr);
	
	//가입고 리스트 select
	public List<ex_receiving_joined_dto> select_ex_receiving(Map<String, Object> data_arr);
	
	//가입고 반송
	public int insert_deliver_return(deliver_return_dto dto);
	
	//가입고 반송시 수량 업데이트
	public int update_exrecv_return(String return_qty, String exreceiving_cd);
	
	//주문현황 보여주기
	public List<accepted_order_enroll_dto> select_order_enroll(Map<String, Object> data_arr);
	
	//위치코드
	public List<warehouse_locations_dto_lhwtemp> select_locations(String supplier_cd);
	
	//입고등록 (receiving 테이블)
	public int insert_receiving(receiving_dto dto);
	
	//warehouse insert
	public int insert_warehouse(Map<String, Object> wh_dto);
	
	//warehouse_locations update
	public int update_warehouse_locations(String location_cd);
	
	//출고수량 등록 
	public List<outgoing_select_view_dto> select_stock(String product_cd);
	
	//출고정보 등록
	public int insert_outgoing_enroll(outgoing_enroll_dto dto);
	
	//출고정보 등록 시 재고기록테이블 타입 지정
	public int update_stock_log_out(String wh_warehouse_idx);
	
	//출고피킹 정보 등록
	public int insert_outgoing_picking(List<Map<String, Object>> picking);

	//출고등록시 주문승인 상태 변경
	public int update_acceptedorder_st(String operator_id, String order_cd);
	
	//출고정보 가져오기
	public List<outgoing_enroll_dto> select_outgoing();
	
	//출고 상세 정보
	public List<outgoing_info_joined_dto> select_outgoing_view(Map<String, Object> data_arr);
	
	//출고등록 테이블 업데이트
	public int update_outenroll(String outenroll_cd);
	
	//출고등록과 동시에 데이터 차감
	public int update_warehouse_out(String subtractive_qty, String update_by, String wh_warehouse_idx);
	
	//출고 확정
	public int update_outenroll_decide(String outenroll_cd, String update_id);
		
	//출고확정 시 원래 주문 상태 완료 변경
	public int update_odstate_ended(String order_cd);
	
	//출고확정 시 해당 idx 삭제
	public int delete_warehouse_out(String wh_warehouse_idx);
	
	//출고등록정보 삭제
	public int delete_outenroll(String outenroll_cd);
		
	//출고피킹정보 삭제
	public int delete_outpiking(String outenroll_cd);
	
	//주문승인 상태 원복
	public int update_accepted_back(String order_cd);
	
	
}

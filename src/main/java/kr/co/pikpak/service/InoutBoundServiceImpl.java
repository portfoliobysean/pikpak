package kr.co.pikpak.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import kr.co.pikpak.repo.InoutBoundRepo;

@Service
public class InoutBoundServiceImpl implements InoutBoundService{
	
	@Autowired
	InoutBoundRepo iorepo;
	
	//주문 승인 상태 원복
	@Override
	public int update_accepted_back(String order_cd) {
		int result = iorepo.update_accepted_back(order_cd);
		return result;
	}
	 
	
	//출고피킹 정보 삭제
	@Override
	public int delete_outpiking(String outenroll_cd) {
		int result = iorepo.delete_outpiking(outenroll_cd);
		return result;
	}
	
	//출고등록 정보 삭제
	@Override
	public int delete_outenroll(String outenroll_cd) {
		int result = iorepo.delete_outenroll(outenroll_cd);
		return result;
	}
	
	//출고 확정 시 전체 재고 뺸 것에 대해 capacity 자동감소를 위해 delete
	@Override
	public int delete_warehouse_out(String wh_warehouse_idx) {
		int result = iorepo.delete_warehouse_out(wh_warehouse_idx); //iorepo를 빠뜨리고 그냥 하면 무한루프
		return result;
	}
	
	
	//출고 확정 시 주문 상태 완료 변경
	@Override
	public int update_odstate_ended(String order_cd) {
		int result = iorepo.update_odstate_ended(order_cd);
		return result;
	}
	
	
	//출고 확정
	@Override
	public int update_outenroll_decide(String outenroll_cd, String update_id) {
		int result = iorepo.update_outenroll_decide(outenroll_cd, update_id);
		return result;
	}
	
	
	//재고차감
	@Override
	public int update_warehouse_out(String subtractive_qty, String update_by, String wh_warehouse_idx) {
		int result = iorepo.update_warehouse_out(subtractive_qty, update_by, wh_warehouse_idx);
		return result;
	}
	
	
	//출고등록 시 재고기록 '출고' 타입 지정
	@Override
	public int update_stock_log_out(String wh_warehouse_idx) {
		int result = iorepo.update_stock_log_out(wh_warehouse_idx);
		return result;
	}
	
	
	//출고등록 테이블 업데이트
	@Override
	public int update_outenroll(String outenroll_cd) {
		int result = iorepo.update_outenroll(outenroll_cd);
		return result;
	}
	
	
	//출고상세정보 가져오기
	@Override
	public List<outgoing_info_joined_dto> select_outgoing_view(Map<String, Object> data_arr) {
		if((data_arr.get("start_date") != "") && (data_arr.get("end_date") != "")) {
			String startdt = (String) data_arr.get("start_date");
			startdt += " 00:00:00";
			data_arr.put("start_date", startdt); 
			
			String enddt = (String) data_arr.get("end_date");
			enddt += " 23:59:59";
			data_arr.put("end_date", enddt);
		}
		
		List<outgoing_info_joined_dto> outgoing_info = iorepo.select_outgoing_view(data_arr);
		return outgoing_info;
	}
	
	
	//출고정보 가져오기
	@Override
	public List<outgoing_enroll_dto> select_outgoing() {
		List<outgoing_enroll_dto> outgoing = iorepo.select_outgoing();
		return outgoing;
	}
	
	//출고등록시 주문승인 상태변경
	@Override
	public int update_acceptedorder_st(String operator_id, String order_cd) {
		int result = iorepo.update_acceptedorder_st(operator_id, order_cd); //인자명이 달라도 되나..??
		return result;
	}
	
	
	
	//출고피킹 정보 등록
	@Override
	public int insert_outgoing_picking(List<Map<String, Object>> picking) {
		int result = iorepo.insert_outgoing_picking(picking);
		return result;
	}
	
	//출고정보 등록
	@Override
	public int insert_outgoing_enroll(outgoing_enroll_dto dto) {
		//dto.setOutenroll_cd(this.make_outenrollcode());
		
		dto.setOutenroll_st("대기");
		
		int result = iorepo.insert_outgoing_enroll(dto);
		
		return result;
	}
	
	
	//출고수량 등록
	@Override
	public List<outgoing_select_view_dto> select_stock(String product_cd) {
		List<outgoing_select_view_dto> stock_info = iorepo.select_stock(product_cd);
		return stock_info;
	}
	
	
	//warehouse_locations update
	@Override
	public int update_warehouse_locations(String location_cd) {
		int result = iorepo.update_warehouse_locations(location_cd);
		return result;
	}
	
	
	//warehouse insert
	@Override
	public int insert_warehouse(Map<String, Object> wh_dto) {
		int result = iorepo.insert_warehouse(wh_dto);
		return result;
	}

	//입고등록 receiving
	@Override
	public int insert_receiving(receiving_dto dto) {
		int final_result = 0;
		
		//lot_no 생성 => 반품과 납품이 달라야함
		/*
		  상품코드 + 제조일자 + 입고일자 
		 */
		String lot_no = this.make_lotno(dto.getProduct_cd(), dto.getMake_dt(), dto.getInventory_dt(),dto.getExreceiving_type());
		dto.setLot_no(lot_no);
		
		//고유번호 넣기
		dto.setReceiving_cd(this.make_recvcode());
		
		int result = iorepo.insert_receiving(dto);
		if(result > 0) { //입고 먼저 하고
			/* 
			[warehouse_locations 테이블에서 업데이트]
			해당 위치코드의 currunt_capacity 증가
			*/
		    int update_locations_result = this.update_warehouse_locations(dto.getLocation_cd());
		    if(update_locations_result > 0) {
		    	/*
				[warehouse 테이블에서 업데이트]
				테이블 안에 해당 위치에 상품코드가 없으면 그대로 insert
				*/
		    	//insert	
				//Map 만들기
				Map<String, Object> wh_dto = new HashMap<String, Object>();
				wh_dto.put("location_cd", dto.getLocation_cd());
				wh_dto.put("product_cd", dto.getProduct_cd());
				wh_dto.put("product_nm", dto.getProduct_nm());
				wh_dto.put("supplier_nm", dto.getSupplier_nm());
				wh_dto.put("supplier_cd", dto.getSupplier_cd());
				wh_dto.put("product_qty", dto.getReceiving_qty());
				wh_dto.put("inventory_log", dto.getReceiving_log());
				wh_dto.put("update_dt", dto.getReceiving_dt());
				wh_dto.put("update_by", dto.getOperator_id());
				
			    int insert_result = this.insert_warehouse(wh_dto);   
			    
			    if(insert_result > 0) {
			    	final_result = 1;
			    }	
		    	
			}
		    else {
		    	//System.out.println("위치코드 업데이트 실패");
		    }
		}
		else {
			final_result = -1;
			//System.out.println("입고 실패");
		}
		
		/*	
		3. stock_log_record
		Trigger 걸어놓기 ok
		*/
		
		return final_result;
	}
	
	
	//위치코드 정보 가지고 오기
	@Override
	public List<warehouse_locations_dto_lhwtemp> select_locations(String supplier_cd) {
		List<warehouse_locations_dto_lhwtemp> locations = iorepo.select_locations(supplier_cd);
		return locations;
	}
	
	//출고등록에서 주문현황 보여주기
	@Override
	public List<accepted_order_enroll_dto> select_order_enroll(Map<String, Object> data_arr) {
		if((data_arr.get("start_date") != "") && (data_arr.get("end_date") != "")) {
			String startdt = (String) data_arr.get("start_date");
			startdt += " 00:00:00";
			data_arr.put("start_date", startdt); 
			
			String enddt = (String) data_arr.get("end_date");
			enddt += " 23:59:59";
			data_arr.put("end_date", enddt);
		}
		
		List<accepted_order_enroll_dto> orderlist = iorepo.select_order_enroll(data_arr);
		return orderlist;
	}
	
	
	//가입고 반송 시 수량 업데이트
	@Override
	public int update_exrecv_return(String return_qty, String exreceiving_cd) {
		int result = iorepo.update_exrecv_return(return_qty, exreceiving_cd);
		return result;
	}
	
	//가입고 반송
	@Override
	public int insert_deliver_return(deliver_return_dto dto) {
		//고유번호 랜덤 생성
		dto.setD_return_cd(this.make_returncode());
		
		//가입고 반품수량업데이트도 해야함
		int final_result = 0;
		
		int result = iorepo.insert_deliver_return(dto);
		
		if(result > 0) { //반품 등록이 되면
			
			int update_return = this.update_exrecv_return(dto.getD_return_qty(), dto.getExreceiving_cd());
			if(update_return > 0) {
				final_result = update_return;
			}
			else {
				final_result = -1;
			}
		}	
		return final_result;
	}
	
	
	
	//가입고 리스트
	@Override
	public List<ex_receiving_joined_dto> select_ex_receiving(Map<String, Object> data_arr) {
		if((data_arr.get("start_date") != "") && (data_arr.get("end_date") != "")) {
			String startdt = (String) data_arr.get("start_date");
			startdt += " 00:00:00";
			data_arr.put("start_date", startdt); 
			
			String enddt = (String) data_arr.get("end_date");
			enddt += " 23:59:59";
			data_arr.put("end_date", enddt);
		}
		
		List<ex_receiving_joined_dto> exrecv_list = iorepo.select_ex_receiving(data_arr);
		return exrecv_list;
	}
	
	
	//입고요청 조회
	@Override
	public List<input_request_dto> select_inreq_search(Map<String, Object> data_arr) {
		if((data_arr.get("start_date") != "") && (data_arr.get("end_date") != "")) {
			String startdt = (String) data_arr.get("start_date");
			startdt += " 00:00:00";
			data_arr.put("start_date", startdt); 
			
			String enddt = (String) data_arr.get("end_date");
			enddt += " 23:59:59";
			data_arr.put("end_date", enddt);
		}
		
		List<input_request_dto> ir_search = iorepo.select_inreq_search(data_arr);
		return ir_search;
	}
	
	//입고요청 수정
	@Override
	public int update_inreq(Map<String, Object> inrequest) {
		int result = iorepo.update_inreq(inrequest);
		return result;
	}
	
	//상품명 페이징 + 검색
	@Override
	public List<product_dto_lhwtemp> select_product_limit(Map<String, Object> product) {
		List<product_dto_lhwtemp> pdlist_part = iorepo.select_product_limit(product);
		return pdlist_part;
	}
	
	//상품명 개수
	@Override
	public Integer select_product_total(String pd_nm, String pd_cd) {
		Integer total = iorepo.select_product_total(pd_nm, pd_cd);
		return total;
	}
	
	
	//매입처 페이징 + 검색
	@Override
	public List<supplier_info_dto_lhwtemp> select_supplier_limit(Map<String, Object> supplier) {
		List<supplier_info_dto_lhwtemp> splist_part = iorepo.select_supplier_limit(supplier);
		return splist_part;
	}
	/*
	//매입처 페이징
	@Override
	public List<supplier_info_dto_lhwtemp> select_supplier_limit(Integer startpg, Integer page_size) {
		List<supplier_info_dto_lhwtemp> sp_list_part = iorepo.select_supplier_limit(startpg, page_size);
		return sp_list_part;
	}
	*/
	//매입처 개수
	@Override
	public Integer select_supplier_total(String comp_nm, String comp_cd) {
		Integer total = iorepo.select_supplier_total(comp_nm, comp_cd);
		return total;
	}
	
	//입고요청 데이터 삭제
	@Override
	public int delete_inreq(String request_idx) {
		int result = iorepo.delete_inreq(request_idx);
		return result;
	}
	
	//입고요청 데이터 리스트
	@Override
	public List<input_request_dto> select_inreq() {
		List<input_request_dto> ir_list = iorepo.select_inreq();
		return ir_list;
	}
	
	//입고요청 데이터 등록
	@Override
	public int input_req_insert(input_request_dto dto) {
		dto.setRequest_cd(this.make_inreqcode());
		
		dto.setRequest_st("대기"); //dto에 값이 없으면 오류남
		int result = iorepo.input_req_insert(dto);
		return result;
	}
	

	//상품 정보 가져오기(select 옵션)
	@Override
	public List<Map<String, Object>> select_product() {
		List<Map<String, Object>> select_pd = iorepo.select_product();
		return select_pd;
	}
	
	//데이터베이스 서버시간 불러오기
	@Override
	public String get_time() {
		String time = iorepo.get_time();
		return time;
	}
	
	//운영자 이름 검색하기 => 조회용
	@Override
	public List<String> search_operator_nm(String operator_nm) {
		List<String> op_id = iorepo.search_operator_nm(operator_nm);
		return op_id;
	}
	
	//운영자 이름 검색하기 => 출력용
	@Override
	public String search_one_id(String operator_id) {
		String op_name = iorepo.search_one_id(operator_id);
		return op_name;
	}
	
	//입고요청 데이터 등록시 입고요청코드 랜덤생성
	public String make_inreqcode() {
		//서버 시간
		String server_time = this.get_time();
	
		//랜덤숫자 4개 생성
		int w = 0;
		String randnum = "";
		
		while(w < 4) {
			int pc = (int)(Math.ceil(Math.random()*9));
			randnum += pc;
			w++;
		}
		
		String code = "IR-"+ server_time + randnum;
		
		return code;
	}
	
	//반송 데이터 등록시 입고요청코드 랜덤생성
	public String make_returncode() {
		//서버 시간
		String server_time = this.get_time();
		
		//랜덤숫자 4개 생성
		int w = 0;
		String randnum = "";
			
		while(w < 4) {
			int pc = (int)(Math.ceil(Math.random()*9));
			randnum += pc;
			w++;
		}
			
		String code = "DR-"+ server_time + randnum;
			
		return code;
	}
	
	//입고 등록시 입고코드 랜덤생성
		public String make_recvcode() {
			//서버 시간
			String server_time = this.get_time();
			
			//랜덤숫자 4개 생성
			int w = 0;
			String randnum = "";
				
			while(w < 4) {
				int pc = (int)(Math.ceil(Math.random()*9));
				randnum += pc;
				w++;
			}
				
			String code = "RE-" +  server_time +randnum;
				
			return code;
		}
	
		//출고 데이터 등록시 출고등록코드 랜덤생성
		public String make_outenrollcode() {
			//서버 시간
			String server_time = this.get_time();
			
			//랜덤숫자 4개 생성
			int w = 0;
			String randnum = "";
				
			while(w < 4) {
				int pc = (int)(Math.ceil(Math.random()*9));
				randnum += pc;
				w++;
			}
				
			String code = "OE-" + server_time + randnum;
				
			return code;
		}
	
	//로트번호 중복 카운트
	@Override
	public String select_lot_count(String lot_no) {
		String lot_count = iorepo.select_lot_count(lot_no);
		return lot_count;
	}	
	
	//입고 시 로트번호 생성하기
	public String make_lotno(String product_cd, String make_dt, String inventory_dt, String type) {
		String makedate = make_dt.replaceAll("-", "");
		String ivdate = inventory_dt.replaceAll("-", "");
		String lot_no = "";
		if(type.equals("납품")) {
			lot_no = product_cd + "-" + "M" + makedate.substring(2) + "-" + "R" + ivdate.substring(2);
		}
		else if(type.equals("반품")){
			lot_no = product_cd + "-" + "M" + makedate.substring(2) + "-" + "R" + ivdate.substring(2) + "-RT";
		}
		
		int count = Integer.parseInt(this.select_lot_count(lot_no));
		
		if (count > 0) {
		       lot_no += "-" + String.format("%03d", count + 1);  // 카운트가 있으면 "-001", "-002" 형태로 추가
		}
	
		return lot_no;
	}
	
	
	
}

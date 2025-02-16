package kr.co.pikpak.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import kr.co.pikpak.dto.deliver_enroll_dto;
import kr.co.pikpak.dto.deliver_return_joined_dto;
import kr.co.pikpak.dto.ex_receiving_dto;
import kr.co.pikpak.dto.input_request_dto;
import kr.co.pikpak.dto.input_request_state_dto;
import kr.co.pikpak.repo.DeliveryRepo;

@Service
public class DeliveryServiceImpl implements DeliveryService{
	
	@Autowired
	DeliveryRepo delrepo;
	
	@Override
	public String select_current_supplier(String trader_id) {
		String supplier_cd = delrepo.select_current_supplier(trader_id);
		return supplier_cd;
	}
	
	//서버시간
	@Override
	public String get_time_deli() {
		String server_time = delrepo.get_time_deli();
		return server_time;
	}
	
	//반송현황
	@Override
	public List<deliver_return_joined_dto> select_return_joined(Map<String, Object> data_arr) {
		if((data_arr.get("start_date") != "") && (data_arr.get("end_date") != "")) {
			String startdt = (String) data_arr.get("start_date");
			startdt += " 00:00:00";
			data_arr.put("start_date", startdt); 
			
			String enddt = (String) data_arr.get("end_date");
			enddt += " 23:59:59";
			data_arr.put("end_date", enddt);
		}

		List<deliver_return_joined_dto> d_return = delrepo.select_return_joined(data_arr);
		return d_return;
	}
	
	//입고요청 '완료' 변경
	@Override
	public int update_finished_inreq(String request_cd) {
		int result = delrepo.update_finished_inreq(request_cd);
		return result;
	}
	
	//모든 납품등록 완료 request_cd
	@Override
	public List<String> select_delivered_finish() {
		List<String> request_code = delrepo.select_delivered_finish();
		return request_code;
	}
	
	/*
	//가입고 로트번호 생성 위해
	@Override
	public Integer find_exrecvdt(String exrecv_dt) {
		return null;
	}
	
	//등록일자에 따라 뒤에 001,002붙이기 + 오전...오후..?
		//등록일자 정리
		String exrecv_dt_sub =  dto.getExreceiving_dt().substring(0, 10);
		String exrecv_dt = exrecv_dt_sub.replaceAll("-", "");
		
		//로트번호 생성
		String lot_no = dto.getProduct_cd() + "-M" + dto.getMake_dt().replaceAll("-", "") + "-E" + exrecv_dt;
		
		
		dto.setLot_no(lot_no);
		System.out.println("로트번호: " + lot_no);
	*/
	
	//가입고 동시에 납품등록 업데이트
	@Override
	public int deliver_update_nm(String departure_dt, String update_id, String deliver_cd) {
		int result = delrepo.deliver_update_nm(departure_dt, update_id, deliver_cd);
		return result;
	}
	
	//가입고 등록
	@Override
	public int insert_ex_receiving(ex_receiving_dto dto) {
		//랜덤 고유번호 넣기
		dto.setExreceiving_cd(this.make_exreceiving_code());
		
		//상태 기본 데이터
		dto.setExreceiving_st("대기");
		
		//타입 기본 데이터
		dto.setExreceiving_type("납품");
		
		int final_result = 0;
		
		int result = delrepo.insert_ex_receiving(dto);
		
		//가입고 insert 성공시
		if(result > 0) {
			try {
				
				int update_delienroll = this.deliver_update_nm(dto.getDeparture_dt(), dto.getOperator_id(), dto.getDeliver_cd());
				
				
				//납품등록 업데이트도 성공시
				if(update_delienroll > 0) {
					final_result = result; //성공
				}
				else {
					final_result = -1; //업데이트 실패
				}
			}
			catch(Exception e) {
				//System.out.println("납품등록 업데이트: " + e);
				e.printStackTrace();
			}
		}
		else {
			final_result = -1; //등록 실패
		}
		return final_result;
	}
	
	
	//입고요청 거절
	@Override
	public int update_inreq_reject(String request_idx) {
		int result = delrepo.update_inreq_reject(request_idx);
		return result;
	}
	
	
	//납품등록 삭제
	@Override
	public int delete_deliver_enroll(String deliver_idx) {
		int result = delrepo.delete_deliver_enroll(deliver_idx);
		return result;
	}
	
	//납품등록 현황
	@Override
	public List<deliver_enroll_dto> select_deliver_enroll(Map<String, Object> data_arr) {
		if((data_arr.get("start_date") != "") && (data_arr.get("end_date") != "")) {
			String startdt = (String) data_arr.get("start_date");
			startdt += " 00:00:00";
			data_arr.put("start_date", startdt); 
			
			String enddt = (String) data_arr.get("end_date");
			enddt += " 23:59:59";
			data_arr.put("end_date", enddt);
		}
		
		List<deliver_enroll_dto> deliver_list = delrepo.select_deliver_enroll(data_arr);
		return deliver_list;
	}
	
	
	//납품등록
	@Override
	public int insert_deliver_enroll(deliver_enroll_dto dto) {
		//String supplier_cd = "C001";
		//dto.setSupplier_cd(supplier_cd); //업체코드
		
		//납품요청코드 랜덤생성
		dto.setDeliver_cd(this.make_delienrollcode());
		
		dto.setDeliver_st("대기");
				
		int result = delrepo.insert_deliver_enroll(dto);
		return result;
	}
	
	//현재 입고요청된 리스트 특정 회사에 대해서 가져오기
	@Override
	public List<input_request_state_dto> select_inreq_deliv(Map<String, Object> data_arr) { //String supplier_cd
		if((data_arr.get("start_date") != "") && (data_arr.get("end_date") != "")) {
			String startdt = (String) data_arr.get("start_date");
			startdt += " 00:00:00";
			data_arr.put("start_date", startdt); 
			
			String enddt = (String) data_arr.get("end_date");
			enddt += " 23:59:59";
			data_arr.put("end_date", enddt);
		}
		
		List<input_request_state_dto> ir_list = delrepo.select_inreq_deliv(data_arr);
		return ir_list;
	}

	//납품등록 데이터 등록시 납품등록코드 랜덤생성
	public String make_delienrollcode() {
	//서버 시간
	String server_time = this.get_time_deli();
	
	//랜덤숫자 4개 생성
	int w = 0;
	String randnum = "";
			
	while(w < 4) {
		int pc = (int)(Math.ceil(Math.random()*9));
		randnum += pc;
		w++;
	}
			
	String code = "DE-"+ server_time + randnum;
			
	return code;
	}
	
	//가입고 등록시 가입고코드 랜덤생성
	public String make_exreceiving_code() {
		//서버 시간
		String server_time = this.get_time_deli();
			
		//랜덤숫자 4개 생성
		int w = 0;
		String randnum = "";
				
		while(w < 4) {
			int pc = (int)(Math.ceil(Math.random()*9));
			randnum += pc;
			w++;
		}
				
		String code = "ER-"+ server_time + randnum;
				
		return code;
	}
	
}

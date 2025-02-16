package kr.co.pikpak.service;

import java.util.List;
import java.util.Map;

import kr.co.pikpak.dto.deliver_enroll_dto;
import kr.co.pikpak.dto.deliver_return_joined_dto;
import kr.co.pikpak.dto.ex_receiving_dto;
import kr.co.pikpak.dto.input_request_dto;
import kr.co.pikpak.dto.input_request_state_dto;

public interface DeliveryService {
	//서버시간
	public String get_time_deli();
	
	//회사 코드 가져오기
	public String select_current_supplier(String trader_id);
	
	//입고요청 현황
	//public List<input_request_state_dto> select_inreq_deliv(String supplier_cd);
	public List<input_request_state_dto> select_inreq_deliv(Map<String, Object> data_arr);
	
	//납품등록
	public int insert_deliver_enroll(deliver_enroll_dto dto);
	
	//납품등록 현황
	//public List<deliver_enroll_dto> select_deliver_enroll(String supplier_cd);
	public List<deliver_enroll_dto> select_deliver_enroll(Map<String, Object> data_arr);
	
	//납품등록 삭제
	public int delete_deliver_enroll(String deliver_idx);
	
	//입고요청 거절
	public int update_inreq_reject(String request_idx);
	
	//가입고 등록
	public int insert_ex_receiving(ex_receiving_dto dto);
	
	//가입고 등록 전 로트번호 생성 위해
	//public Integer find_exrecvdt(String exrecv_dt);
	
	//가입고 등록과 동시에 납품등록 상태변화
	public int deliver_update_nm(String departure_dt, String update_id, String deliver_cd);
	
	//납품등록 모두 완료
	public List<String> select_delivered_finish();
	
	//입고요청 '완료' 변경
	public int update_finished_inreq(String request_cd);
	
	//반송현황
	public List<deliver_return_joined_dto> select_return_joined(Map<String, Object> data_arr);
}

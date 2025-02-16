package kr.co.pikpak.repo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pikpak.dto.deliver_enroll_dto;
import kr.co.pikpak.dto.deliver_return_joined_dto;
import kr.co.pikpak.dto.ex_receiving_dto;
import kr.co.pikpak.dto.input_request_dto;
import kr.co.pikpak.dto.input_request_state_dto;

@Mapper
public interface DeliveryRepo {
	
	//서버시간
	String get_time_deli();
	
	//회사코드 가져오기
	String select_current_supplier(String trader_id);
	
	//입고요청 현황
	//List<input_request_state_dto> select_inreq_deliv(String supplier_cd);
	List<input_request_state_dto> select_inreq_deliv(Map<String, Object> data_arr);
	
	//납품등록
	int insert_deliver_enroll(deliver_enroll_dto dto);
	
	//납품등록 현황 
	//List<deliver_enroll_dto> select_deliver_enroll(String supplier_cd);
	List<deliver_enroll_dto> select_deliver_enroll(Map<String, Object> data_arr);

	//납품등록 삭제
	int delete_deliver_enroll(String deliver_idx);
	
	//입고요청 거절 시 상태변화
	int update_inreq_reject(String request_idx);
	
	//배송 확정시 가입고 등록
	int insert_ex_receiving(ex_receiving_dto dto);
	
	//가입고 등록과 동시에 납품등록 상태와 업데이트 담당자 변경
	int deliver_update_nm(String departure_dt ,String update_id, String deliver_cd);
	
	//가입고 등록 하기전 등록일자 같은 거 찾기(로트번호 생성 위해)
	//Integer find_exrecvdt(String exrecv_dt);
	
	//입고요청에 대한 모든 납품등록 완료된 request_cd
	List<String> select_delivered_finish();
	
	//입고요청 완료로 변경
	int update_finished_inreq(String request_cd);
	
	//반송 현황 select
	List<deliver_return_joined_dto> select_return_joined(Map<String, Object> data_arr);
}

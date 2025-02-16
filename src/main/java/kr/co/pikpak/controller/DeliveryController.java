package kr.co.pikpak.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.ServletResponse;
import kr.co.pikpak.dto.deliver_enroll_dto;
import kr.co.pikpak.dto.deliver_return_joined_dto;
import kr.co.pikpak.dto.ex_receiving_dto;
import kr.co.pikpak.dto.input_request_dto;
import kr.co.pikpak.dto.input_request_state_dto;
import kr.co.pikpak.service.DeliveryService;

@Controller
public class DeliveryController {
	
	PrintWriter pw = null;
	
	@Autowired
	DeliveryService delservice;
	
	
	//납품등록에서 배송 버튼 클릭 시 가입고 등록
	@PostMapping("delivery/insert_exreceiving")
	public String insert_exreceiving(ServletResponse res,
			@ModelAttribute("exreceiving") ex_receiving_dto dto,
			@SessionAttribute(name = "activeUserID", required = false) String operator_id) {
		//넘어오는 값 : request_cd, deliver_cd, supplier_cd, product_cd, exreceiving_qty, exreceiving_size, exreceiving_area, departure_dt(배송일시), make_dt
		//만들어야하는 값 : exreceiving_cd, exreceiving_st, exreceiving_id , operator_id, operator_nm
		//DB에서 들어가는 값 or null 값 : exreceiving_idx, update_dt, update_id, update_nm 
		res.setContentType("text/html;charset=utf-8");
		
		try {
			this.pw = res.getWriter();
			
			//사용자 이름 세션
			//제조사 측 납품등록이자, 물류회사측 가입고 목록에 보일 것
			dto.setOperator_id(operator_id);
			
			int result = delservice.insert_ex_receiving(dto);
			
			if(result > 0) {
				this.pw.print("<script>"
						+ "alert('정상적으로 등록되었습니다.');"
						+ "location.href = './deliveryenroll';"
						+ "</script>");
				
				List<String> request_code = delservice.select_delivered_finish();
				
				//배송일자 departure_dt를 가지고 와서 deliver_enroll에도 같이 업데이트 시키기
				if(request_code.contains(dto.getRequest_cd())) {
					String request_cd = dto.getRequest_cd();
					
					int update = delservice.update_finished_inreq(request_cd);
				}
			}
			
		}
		catch(Exception e) {
			this.pw.print("<script>"
					+ "alert('데이터베이스 문제로 등록되지 못하였습니다.');"
					+ "location.href = './deliveryenroll';"
					+ "</script>");
			e.printStackTrace();
		}
		finally {
			this.pw.close();
		}
		
		return null;
	}
	
	
	//입고요청 거절 => 세션가지고 와
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/reject_deliverylist")
	public String reject_deliverylist(ServletResponse res, 
			@RequestParam(defaultValue = "", required = false) String request_idx) {
		try {
			this.pw = res.getWriter();
			int result = delservice.update_inreq_reject(request_idx);
			if(result > 0) {
				this.pw.print("ok");
			}
		} 
		catch (Exception e) {
			this.pw.print("error");
		}
		finally {
			this.pw.close();
		}
		return null;
	}
	
	
	//납품등록 삭제 => 세션가지고 와?
	@PostMapping("/delivery/delete_deliveryok")
	public String delete_deliveryok(ServletResponse res,
			@RequestParam(defaultValue = "", required = false) String del_each_ck[],
			@RequestParam(defaultValue = "", required = false) String deliver_idx) {
		res.setContentType("text/html;charset=utf-8");
		try {
			this.pw = res.getWriter();
			//System.out.println(del_each_ck.length);
			//System.out.println(deliver_idx);
			String idx_data[] = deliver_idx.split(",");
			if(idx_data.length == del_each_ck.length) {
				int result = delservice.delete_deliver_enroll(deliver_idx);
				
				if(result > 0) {
					this.pw.print("<script>"
							+ "alert('정상적으로 삭제되었습니다.');"
							+ "location.href='./deliveryenroll';"
							+ "</script>");
				}
			}	
		}
		catch(Exception e) {
			System.out.println(e);
			this.pw.print("<script>"
					+ "alert('데이터베이스 문제로 삭제되지 못하였습니다.');"
					+ "location.href='./deliveryenroll';"
					+ "</script>");
		}
		finally {
			this.pw.close();
		}
		return null;
	}
	
	
	
	//납품등록
	@PostMapping("/delivery/delivery_enrollok")
	public String delivery_enrollok(ServletResponse res, @ModelAttribute("deliver") deliver_enroll_dto dto,
			@SessionAttribute(name = "activeUserID", required = false) String operator_id) {
		//프론트에서 넘어오는 값 : request_cd, prodcut_cd, product_nm, deliver_qty, make_dt, expect_dt, deliver_size,supplier_cd =>deliver_area 없앨 예정(일단 null)
		//여기서 넣어야하는 값 : deliver_cd, deliver_st, operator_nm, operator_id
		//쿼리문에서 넣는 값 or null 값 : deliver_idx, deliver_dt, update_id, update_nm, update_dt
		res.setContentType("text/html;charset=utf-8");
		
		try {
			this.pw = res.getWriter();
			
			//세션 id
			dto.setOperator_id(operator_id);
		
			int result = delservice.insert_deliver_enroll(dto);
			
			if(result > 0) {
				this.pw.print("<script>"
						+ "alert('정상적으로 등록되었습니다');"
						+ "location.href='./deliveryenroll';"
						+ "</script>");
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(e);
			this.pw.print("<script>"
					+ "alert('데이터베이스 문제로 등록되지 못하였습니다');"
					+ "location.href='./inreqstate';"
					+ "</script>");
		}
		finally {
			this.pw.close();
		}
		
		return null;
	}
	
	//반송현황 서치
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/returnstate_search")
	public ResponseEntity<List<deliver_return_joined_dto>> returnstate_search(
			@RequestBody Map<String, Object> data_arr,
			@SessionAttribute(name = "activeUserID", required = false) String trader_id){
		
		//해당 id가 어디 소속인지 가져와야함
		String supplier_cd = delservice.select_current_supplier(trader_id);
		
		
		data_arr.put("supplier_cd", supplier_cd);
		try {
			List<deliver_return_joined_dto> return_search = delservice.select_return_joined(data_arr);
			return ResponseEntity.ok(return_search);
		}
		catch(Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	
	//반송현황
	@GetMapping("delivery/returnstate")
	public String returnstate() {
		
		return null;
	}
	
	//입고요청현황 리스트 검색
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/inreqstate_search")
	public ResponseEntity<List<input_request_state_dto>> inreqstate_search(
			@RequestBody Map<String, Object> data_arr,
			@SessionAttribute(name = "activeUserID", required = false) String trader_id) {
		List<input_request_state_dto> ir_state_search = null;
		
		//세션에서 회사정보
		String supplier_cd = delservice.select_current_supplier(trader_id);

		try {
			//data_arr : start_date, end_date, request_st, product_cd
			
			//회사정보를 넣어주기
			data_arr.put("supplier_cd", supplier_cd);
			
			//쿼리 실행
			ir_state_search = delservice.select_inreq_deliv(data_arr);
			return ResponseEntity.ok(ir_state_search);
		}
		catch(Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	
	//입고요청현황
	@GetMapping("/delivery/inreqstate")
	public String inreqstate() {
		
		return null;
	}
	
	//납품등록 배송확정 페이지 리스트 동적 생성
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/deliverenroll_search")
	public ResponseEntity<List<deliver_enroll_dto>> deliverenroll_search(@RequestBody Map<String, Object> data_arr,
			@SessionAttribute(name = "activeUserID", required = false) String trader_id) {
		//배열 파싱해서 잘 받으려면 RequestBody 필수..
		List<deliver_enroll_dto> deli_enroll_search = null;
		
		//세션 id로 회사코드 검색
		String supplier_cd = delservice.select_current_supplier(trader_id);
		
		try {
			//data_arr : start_date, end_date, deliver_st, product_cd
			
			//회사정보를 넣어주기
			data_arr.put("supplier_cd", supplier_cd);
			
			//쿼리 실행
			deli_enroll_search = delservice.select_deliver_enroll(data_arr);
			return ResponseEntity.ok(deli_enroll_search);
		}
		catch(Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	//납품 등록
	@GetMapping("/delivery/deliveryenroll")
	public String deliveryenroll() {
		
		return null;
	}
}
